package com.orangehrm.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPage extends BasePage {

    private static final By USERNAME_INPUT = By.name("username");
    private static final By PASSWORD_INPUT = By.name("password");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By SUGGESTED_USER_NAME = By.xpath(
            "//p[contains(normalize-space(),'Username') and contains(normalize-space(),':')]");
    private static final By SUGGESTED_USER_PASSWORD = By.xpath(
            "//p[contains(normalize-space(),'Password') and contains(normalize-space(),':')]");

    public LoginPage open(String url) {
        driver.get(url);
        waitVisible(USERNAME_INPUT);
        return this;
    }

    public static final class LoginCredentials {
        private final String username;
        private final String password;

        public LoginCredentials(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String username() {
            return username;
        }

        public String password() {
            return password;
        }
    }

    public LoginCredentials readSuggestedCredentials() {
        String user = getText(SUGGESTED_USER_NAME);
        String pass = getText(SUGGESTED_USER_PASSWORD);
        String username = extractCredential(user, "Username : ");
        String password = extractCredential(pass, "Password : ");
        return new LoginCredentials(username, password);
    }

    private String extractCredential(String text, String pattern) {
        return text.replace(pattern, "");
    }

    public DashboardPage login(LoginCredentials credentials) {
        type(USERNAME_INPUT, credentials.username());
        type(PASSWORD_INPUT, credentials.password());
        click(LOGIN_BUTTON);
        wait.until(ExpectedConditions.urlContains("/dashboard"));
        return new DashboardPage();
    }
}
