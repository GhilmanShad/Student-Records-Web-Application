
const { Builder, By, until } = require('selenium-webdriver');
const assert = require('node:assert/strict');

async function testInvalidLogin() {
    const driver = await new Builder()
        .forBrowser('chrome')
        .build();

    try {
        await driver.get('http://localhost:8080/login');

        await driver.findElement(By.id('username'))
            .sendKeys('invalid_sqe_user');

        await driver.findElement(By.id('password'))
            .sendKeys('invalid_sqe_password');

        await driver.findElement(
            By.css('button[type="submit"]')
        ).click();

        await driver.wait(
            until.urlContains('/login?error'),
            10000
        );

        const errorMessage = await driver.wait(
            until.elementLocated(By.css('.error-message')),
            10000
        );

        assert.match(
            await errorMessage.getText(),
            /Invalid username or password/i
        );

        console.log('SEL-01: PASS - Invalid login rejected');

    } catch (error) {
        console.error('SEL-01: FAIL', error.message);
        process.exitCode = 1;
    } finally {
        await driver.quit();
    }
}

testInvalidLogin();
