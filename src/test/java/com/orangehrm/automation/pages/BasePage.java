package com.orangehrm.automation.pages;

import com.orangehrm.automation.config.ConfigReader;
import com.orangehrm.automation.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getExplicitWaitSeconds()));
    }

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void waitForNotVisible(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        waitClickable(locator).click();
    }

    protected void clickByJs(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    protected void sendKeys(By locator, CharSequence... keys) {
        waitVisible(locator).sendKeys(keys);
    }

    protected String getText(By locator) {
        return waitVisible(locator).getText();
    }

    protected String getUrl() {
        return driver.getCurrentUrl();
    }

    protected boolean isElementDisplayed(By locator) {
        return waitVisible(locator).isDisplayed();
    }

    protected void type(By locator, String text) {
        WebElement field = waitVisible(locator);
        field.sendKeys(text);
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    protected List<WebElement> findAll(By locator) {
        return driver.findElements(locator);
    }
}
