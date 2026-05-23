package com.orangehrm.automation.driver;

import com.orangehrm.automation.config.ConfigReader;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static WebDriver getDriver() {
        if (DRIVER.get() == null) {
            DRIVER.set(createDriver());
        }
        return DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }

    private static WebDriver createDriver() {
        String browser = ConfigReader.getBrowser().toLowerCase();
        WebDriver driver;
        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver(new FirefoxOptions());
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = new EdgeDriver();
                break;
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--start-maximized");
                options.addArguments("--disable-notifications");
                driver = new ChromeDriver(options);
                break;
        }

        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(ConfigReader.getPageLoadTimeoutSeconds()));
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        return driver;
    }
}
