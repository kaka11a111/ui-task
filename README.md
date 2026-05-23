# OrangeHRM UI Automation (Java + BDD + POM)

End-to-end UI automation for the [OrangeHRM demo](https://opensource-demo.orangehrmlive.com/web/index.php/auth/login) using **Selenium 4**, **Cucumber (BDD)**, and the **Page Object Model**.

## Test flow

1. Open the login URL
2. Read and use the credentials shown on the login page
3. Navigate to **Admin > User Management**
4. Select **Users**, then click **Add** to open the create-user form
5. Fill all required fields and save
6. Verify successful creation
7. Search by the new username
8. Verify the user appears in **Records Found**

## Prerequisites

- **JDK 11+**
- **Maven 3.9+**
- **Google Chrome** (default browser; WebDriverManager downloads the matching driver)

## Project structure

```
src/test/java/com/orangehrm/automation/
├── config/          # config.properties loader
├── context/         # shared scenario data (created username)
├── driver/          # WebDriver lifecycle (ThreadLocal)
├── hooks/           # @Before / @After (screenshot on failure)
├── pages/           # Page Object Model
├── runners/         # JUnit Platform + Cucumber suite
└── steps/           # Cucumber step definitions

src/test/resources/
├── features/        # Gherkin feature files
├── config.properties
└── cucumber.properties
```

## Run tests

```bash
cd orangehrm-ui-automation
mvn clean test
```

Run with a specific tag:

```bash
mvn clean test -Dcucumber.filter.tags=@smoke
```

## Configuration

Edit `src/test/resources/config.properties`:

| Property | Description |
|----------|-------------|
| `base.url` | OrangeHRM login URL |
| `browser` | `chrome` (default), `firefox`, or `edge` |
| `explicit.wait.seconds` | Explicit wait timeout |

## Reports

- HTML: `target/cucumber-reports/cucumber.html`
- Screenshots on failure: `screenshots/` and attached to the Cucumber report

## Design notes

- **BDD**: scenarios in Gherkin; steps delegate to page objects
- **POM**: locators and UI actions live in `pages/`; steps contain assertions only
- **Credentials**: read dynamically from the login page (no hard-coded secrets in code)
