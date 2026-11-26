
const { Builder, By, until } = require('selenium-webdriver');
const assert = require('node:assert/strict');

async function testStudentSearch() {
    const username = process.env.TEST_USERNAME;
    const password = process.env.TEST_PASSWORD;

    if (!username || !password) {
        console.error('Set TEST_USERNAME and TEST_PASSWORD first');
        process.exitCode = 1;
        return;
    }

    const driver = await new Builder()
        .forBrowser('chrome')
        .build();

    try {
        // Step 1: Login
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

        // Step 2: Search by registration number
        const searchField = await driver.findElement(
            By.id('keyword')
        );

        await searchField.sendKeys('24P-0008');

        // Step 3: Wait for live search results
        await driver.wait(async () => {
            const rows = await driver.findElements(
                By.css('#results tbody tr')
            );

            if (rows.length !== 1) return false;

            const regNo = await rows[0]
                .findElement(By.css('td.code'))
                .getText();

            return regNo === '24P-0008';
        }, 10000, 'Expected student not found');

        // Step 4: Verify the returned student
        const row = await driver.findElement(
            By.css('#results tbody tr')
        );

        const studentName = await row.findElement(
            By.css('td.person a')
        ).getText();

        const regNo = await row.findElement(
            By.css('td.code')
        ).getText();

        assert.equal(studentName, 'Shahzad');
        assert.equal(regNo, '24P-0008');

        console.log('SEL-03: PASS - Student search successful');

    } catch (error) {
        console.error('SEL-03: FAIL', error.message);
        process.exitCode = 1;
    } finally {
        await driver.quit();
    }
}

testStudentSearch();
