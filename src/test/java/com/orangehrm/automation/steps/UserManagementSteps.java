package com.orangehrm.automation.steps;

import com.orangehrm.automation.config.ConfigReader;
import com.orangehrm.automation.context.TestContext;
import com.orangehrm.automation.pages.AddUserPage;
import com.orangehrm.automation.pages.DashboardPage;
import com.orangehrm.automation.pages.LoginPage;
import com.orangehrm.automation.pages.UserManagementPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class UserManagementSteps {

    private final TestContext testContext;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private UserManagementPage userManagementPage;
    private AddUserPage addUserPage;
    private AddUserPage.NewUserData newUserData;

    public UserManagementSteps(TestContext testContext) {
        this.testContext = testContext;
    }

    @Given("I open the OrangeHRM login page")
    public void iOpenTheOrangeHrmLoginPage() {
        loginPage = new LoginPage().open(ConfigReader.getBaseUrl() + "/auth/login");
    }

    @When("I log in using the credentials suggested on the login page")
    public void iLogInUsingSuggestedCredentials() {
        LoginPage.LoginCredentials credentials = loginPage.readSuggestedCredentials();
        dashboardPage = loginPage.login(credentials);
    }

    @Then("I should see the Dashboard")
    public void iShouldSeeTheDashboard() {
        assertThat(dashboardPage.isLoaded())
                .as("Dashboard page should be visible after login")
                .isTrue();
    }

    @When("I navigate to Admin User Management")
    public void iNavigateToAdminUserManagement() {
        dashboardPage.openAdminModule();
        userManagementPage = new UserManagementPage();
        userManagementPage.openUserManagementMenu();
    }

    @And("I select Users from User Management")
    public void iSelectUsersFromUserManagement() {
        userManagementPage.openUsersTab();
        assertThat(userManagementPage.isUsersListLoaded())
                .as("System Users list should be visible with Add button")
                .isTrue();
    }

    @And("I click Add to open the new user form")
    public void iClickAddToOpenTheNewUserForm() {
        addUserPage = userManagementPage.openAddUserForm();
        assertThat(addUserPage.isLoaded())
                .as("Add User form should be displayed")
                .isTrue();
    }

    @And("I build a user with role {string}, employee {string}, status {string}, password {string}")
    public void iCreateANewUserWithValidRequiredData(String role, String employee, String status, String password) {
        newUserData = addUserPage.buildDefaultUser(role, employee, status, password);
        testContext.setCreatedUsername(newUserData.username());
        testContext.setCreatedPassword(newUserData.password());
        userManagementPage = addUserPage.createUser(newUserData);
    }

    @Then("the user should be created successfully")
    public void theUserShouldBeCreatedSuccessfully() {
        assertThat(userManagementPage.isCreationConfirmed())
                .as("Success toast or System Users list should confirm user creation")
                .isTrue();
    }

    @When("I search for the newly created user by username")
    public void iSearchForTheNewlyCreatedUserByUsername() {
        userManagementPage.searchByUsername(testContext.getCreatedUsername());
    }

    @Then("the user should appear in the Records Found table")
    public void theUserShouldAppearInTheRecordsFoundTable() {
        AddUserPage.NewUserData user = userManagementPage.getAllUsers().get(0);
        assertThat(user.username())
                .as("Username")
                .isEqualTo(testContext.getCreatedUsername());
    }
}
