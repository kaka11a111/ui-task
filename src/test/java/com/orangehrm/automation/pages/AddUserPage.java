package com.orangehrm.automation.pages;

import org.openqa.selenium.By;

import java.util.UUID;

public class AddUserPage extends BasePage {

    private static final By USER_ROLE_DROPDOWN = By.xpath(
            "(//label[normalize-space()='User Role']/following::div[contains(@class,'oxd-select-wrapper')])[1]");
    private static final By EMPLOYEE_NAME_INPUT = By.xpath(
            "//label[normalize-space()='Employee Name']/following::input");
    private static final By STATUS_DROPDOWN = By.xpath(
            "(//label[normalize-space()='Status']/following::div[contains(@class,'oxd-select-wrapper')])[1]");
    private static final By USERNAME_INPUT = By.xpath(
            "//label[normalize-space()='Username']/following::input");
    private static final By PASSWORD_INPUT = By.xpath(
            "//label[normalize-space()='Password']/following::input");
    private static final By CONFIRM_PASSWORD_INPUT = By.xpath(
            "//label[normalize-space()='Confirm Password']/following::input");
    private static final By SAVE_BUTTON = By.xpath(
            "//button[@type='submit' and (normalize-space()='Save' or contains(@class,'--label-save'))]");
    private static final By EMPLOYEE_OPTIONS = By.cssSelector(".oxd-autocomplete-dropdown .oxd-autocomplete-option");
    private static final By SEARCHING = By.xpath("//div[text()='Searching....']");
    private static final By REQUIRED_ERRORS = By.cssSelector(".oxd-input-field-error-message");
    private static final By ADD_USER_FORM = By.xpath("//div[@class='orangehrm-card-container']");
    private static final By TOAST_MESSAGE = By.cssSelector(".oxd-toast--success");
    private static String value_dropdown = "//div[@role]//span[normalize-space()='%s']";

    public boolean isLoaded() {
        return isElementDisplayed(ADD_USER_FORM);
    }

    public static final class NewUserData {
        private final String userRole;
        private final String employeeSearch;
        private final String status;
        private final String username;
        private final String password;

        public NewUserData(String userRole, String employeeSearch, String status, String username, String password) {
            this.userRole = userRole;
            this.employeeSearch = employeeSearch;
            this.status = status;
            this.username = username;
            this.password = password;
        }

        public String userRole() {
            return userRole;
        }

        public String employeeSearch() {
            return employeeSearch;
        }

        public String status() {
            return status;
        }

        public String username() {
            return username;
        }

        public String password() {
            return password;
        }
    }

    public NewUserData buildDefaultUser(String role, String employee, String status, String password) {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        NewUserData user = new NewUserData(
                role,
                employee,
                status,
                "auto_user_" + uniqueSuffix,
                password
        );
        return user;
    }

    public UserManagementPage createUser(NewUserData user) {
        selectDropdownOption(USER_ROLE_DROPDOWN, user.userRole());
        selectFirstAvailableEmployee(user.employeeSearch());
        selectDropdownOption(STATUS_DROPDOWN, user.status());
        type(USERNAME_INPUT, user.username());
        type(PASSWORD_INPUT, user.password());
        type(CONFIRM_PASSWORD_INPUT, user.password());
        click(SAVE_BUTTON);
        waitVisible(TOAST_MESSAGE);

        UserManagementPage usersPage = new UserManagementPage();
        return usersPage;
    }

    private void selectFirstAvailableEmployee(String searchText) {
        type(EMPLOYEE_NAME_INPUT, searchText);
        waitVisible(SEARCHING);
        waitForNotVisible(SEARCHING);
        click(EMPLOYEE_OPTIONS);
    }

    protected void selectDropdownOption(By dropdownLocator, String optionText) {
        click(dropdownLocator);
        By option = By.xpath(String.format(value_dropdown, optionText));
        click(option);
    }
}
