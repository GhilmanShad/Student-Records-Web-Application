
const { Builder, By, until } = require('selenium-webdriver');
const assert = require('node:assert/strict');

async function testValidLogin() {
    const username = process.env.TEST_USERNAME;
    const password = process.env.TEST_PASSWORD;

    if (!username || !password) {
        throw new Error('Set TEST_USERNAME and TEST_PASSWORD first');
    }

    const driver = await new Builder()
        .forBrowser('chrome')
        .build();

    try {
        await driver.get('http://localhost:8080/login');

        await driver.findElement(By.id('username'))
            .sendKeys(username);

        await driver.findElement(By.id('password'))
            .sendKeys(password);

        await driver.findElement(
            By.css('button[type="submit"]')
        ).click();

        await driver.wait(
            until.urlContains('/students'),
            10000
        );

        assert.match(
            await driver.getCurrentUrl(),
            /\/students(?:[/?#]|$)/
        );

        console.log('SEL-02: PASS - Valid login successful');

    } catch (error) {
        console.error('SEL-02: FAIL', error.message);
        process.exitCode = 1;
    } finally {
        await driver.quit();
    }
}

testValidLogin();
