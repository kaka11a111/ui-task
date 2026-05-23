package com.orangehrm.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class UserManagementPage extends BasePage {

    private static final By TOPBAR_USER_MANAGEMENT = By.xpath(
            "//nav[contains(@class,'oxd-topbar-body-nav')]//span[normalize-space()='User Management']");
    private static final By USERS_LIST = By.xpath(
            "//a[normalize-space()='Users']");
    private static final By ADD_BUTTON = By.xpath(
            "//button[contains(@class,'--label-add') or normalize-space()='Add']");
    private static final By SYSTEM_USERS_HEADER = By.xpath(
            "//h6[contains(normalize-space(),'System Users') or normalize-space()='Users']");
    private static final By USERNAME_SEARCH = By.xpath(
            "//label[normalize-space()='Username']/parent::div/following-sibling::div//input");
    private static final By SEARCH_BUTTON = By.xpath(
            "//button[normalize-space()='Search' or normalize-space()=' Search ']");
    private static final By RECORDS_FOUND = By.xpath(
            "//span[contains(@class,'oxd-text--span') and contains(.,'Records Found')]");
    private static final By TABLE_ROWS = By.cssSelector(".oxd-table-body .oxd-table-row");
    private static final By SUCCESS_TOAST = By.xpath("//div[@class='oxd-toast-start']");
    private static final By ADD_USER_TITLE = By.xpath("//h6[normalize-space()='Add User']");
    private static By rowsLocator = By.xpath("//div[@class='oxd-table-body']//div[@class='oxd-table-card']");

    public void openUserManagementMenu() {
        waitClickable(TOPBAR_USER_MANAGEMENT).click();
    }

    public void openUsersTab() {
        click(USERS_LIST);
        waitUntilUsersListLoaded();
    }

    public boolean isUsersListLoaded() {
        try {
            waitUntilUsersListLoaded();
            return true;
        } catch (TimeoutException ex) {
            return false;
        }
    }

    public AddUserPage openAddUserForm() {
        click(ADD_BUTTON);
        waitVisible(ADD_USER_TITLE);
        return new AddUserPage();
    }

    public boolean isCreationConfirmed() {
        return isUsersListLoaded() && (isSuccessToastDisplayed() || getUrl().contains("viewSystemUsers"));
    }

    public boolean isSuccessToastDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(SUCCESS_TOAST));
            return true;
        } catch (Exception ex) {
            return !findAll(SUCCESS_TOAST).isEmpty();
        }
    }

    public void searchByUsername(String username) {
        type(USERNAME_SEARCH, username);
        click(SEARCH_BUTTON);
        wait.until(d -> !findAll(TABLE_ROWS).isEmpty());
    }

    private void waitUntilUsersListLoaded() {
        wait.until(ExpectedConditions.and(
                ExpectedConditions.urlContains("viewSystemUsers"),
                ExpectedConditions.or(
                        ExpectedConditions.visibilityOfElementLocated(SYSTEM_USERS_HEADER),
                        ExpectedConditions.elementToBeClickable(ADD_BUTTON))));
    }

    public String getCellValue(WebElement row, String columnName) {
        String xpath = String.format(
                ".//div[@role='cell'][count(//div[@role='columnheader'][text()='%s']/preceding-sibling::div)+1]",
                columnName
        );

        return row.findElement(By.xpath(xpath)).getText().trim();
    }

    public List<AddUserPage.NewUserData> getAllUsers() {
        List<AddUserPage.NewUserData> users = new ArrayList<>();
        wait.until(ExpectedConditions.visibilityOfElementLocated(rowsLocator));

        List<WebElement> rows = driver.findElements(rowsLocator);

        for (WebElement row : rows) {
            AddUserPage.NewUserData user = new AddUserPage.NewUserData(
                    getCellValue(row, "User Role"),
                    getCellValue(row, "Employee Name"),
                    getCellValue(row, "Status"),
                    getCellValue(row, "Username"),
                    null
            );

            users.add(user);
        }

        return users;
    }
}
