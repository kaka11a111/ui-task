@smoke @user-management
Feature: Admin User Management
  As an OrangeHRM administrator
  I want to create and find system users
  So that user accounts can be managed reliably

  Scenario Outline: Create a new system user and verify it in search results
    Given I open the OrangeHRM login page
    When I log in using the credentials suggested on the login page
    Then I should see the Dashboard
    When I navigate to Admin User Management
    And I select Users from User Management
    And I click Add to open the new user form
    And I build a user with role "<role>", employee "<employee>", status "<status>", password "<password>"
    Then the user should be created successfully
    When I search for the newly created user by username
    Then the user should appear in the Records Found table

    Examples:
      | role | employee | status  | password   |
      | Admin  | a   | Enabled  | Test@99999 |
