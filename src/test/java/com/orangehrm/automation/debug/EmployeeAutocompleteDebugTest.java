package com.orangehrm.automation.debug;

import com.orangehrm.automation.config.ConfigReader;
import com.orangehrm.automation.driver.DriverManager;
import com.orangehrm.automation.pages.LoginPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

class EmployeeAutocompleteDebugTest {

    @AfterEach
    void tearDown() {
        DriverManager.quitDriver();
    }

    @Test
    void debugEmployeePick() {
        WebDriver driver = DriverManager.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        LoginPage loginPage = new LoginPage().open(ConfigReader.getBaseUrl());
        loginPage.login(loginPage.readSuggestedCredentials());

        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/admin/saveSystemUser");
        wait.until(ExpectedConditions.urlContains("saveSystemUser"));

        WebElement roleDropdown = driver.findElement(
                By.xpath("(//label[normalize-space()='User Role']/following::div[contains(@class,'oxd-select-wrapper')])[1]"));
        roleDropdown.click();
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class,'oxd-select-dropdown')]//span[normalize-space()='ESS']"))).click();

        By inputBy = By.xpath("//label[normalize-space()='Employee Name']/following::input");
        By groupBy = By.xpath(
                "//label[normalize-space()='Employee Name']/ancestor::div[contains(@class,'oxd-input-group')]");

        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputBy));
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        input.sendKeys("Peter");

        wait.until(d -> d.findElements(By.cssSelector("[role='listbox'] [role='option']")).stream()
                .anyMatch(el -> el.isDisplayed()
                        && el.getText().trim().contains(" ")
                        && !el.getText().toLowerCase().contains("searching")));

        input.sendKeys(Keys.ARROW_DOWN);
        input.sendKeys(Keys.ENTER);
        System.out.println("GROUP after keyboard: " + driver.findElement(groupBy).getText());

        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        input.sendKeys("Ranga");
        wait.until(d -> d.findElements(By.cssSelector("[role='listbox'] [role='option']")).stream()
                .anyMatch(el -> el.isDisplayed() && el.getText().contains("Ranga")));
        WebElement option = driver.findElements(By.cssSelector("[role='listbox'] [role='option']")).stream()
                .filter(el -> el.getText().contains("Ranga"))
                .findFirst()
                .orElseThrow();
        WebElement span = option.findElement(By.tagName("span"));
        new Actions(driver).moveToElement(span).pause(Duration.ofMillis(300)).click().perform();
        System.out.println("GROUP after span click: " + driver.findElement(groupBy).getText());
    }
}
