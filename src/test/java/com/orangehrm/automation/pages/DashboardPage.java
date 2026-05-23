package com.orangehrm.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DashboardPage extends BasePage {

    private static final By ADMIN_MENU = By.xpath("//span[normalize-space()='Admin']/parent::a");

    public boolean isLoaded() {
        try {
            wait.until(ExpectedConditions.and(
                    ExpectedConditions.urlContains("/dashboard"),
                    ExpectedConditions.visibilityOfElementLocated(ADMIN_MENU)));
            return true;
        } catch (TimeoutException ex) {
            return false;
        }
    }

    public void openAdminModule() {
        click(ADMIN_MENU);
        wait.until(ExpectedConditions.urlContains("/admin/"));
    }
}
