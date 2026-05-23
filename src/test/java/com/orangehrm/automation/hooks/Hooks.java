package com.orangehrm.automation.hooks;

import com.orangehrm.automation.driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;

public class Hooks {

    @Before
    public void setUp() {
        DriverManager.getDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            captureScreenshot(scenario);
        }
        DriverManager.quitDriver();
    }

    private void captureScreenshot(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();
        if (driver instanceof TakesScreenshot) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
            try {
                Path dir = Path.of("screenshots");
                Files.createDirectories(dir);
                Path file = dir.resolve(scenario.getName().replaceAll("\\W+", "_") + ".png");
                Files.write(file, screenshot);
            } catch (Exception ignored) {
                // Best-effort file save; Cucumber attachment is primary
            }
        }
    }
}
