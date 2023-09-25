package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doMockSignUp(String firstName, String lastName, String userName, String password){
        // Create a dummy account for logging in later.

        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
        Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
    }
    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     *
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        doMockSignUp("Redirection","Test","RT","123");

        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }


    /**
     * PLEASE DO NOT DELETE THIS method.
     * Helper method for Udacity-supplied sanity checks.
     **/
    private void doLogIn(String userName, String password)
    {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));

    }



    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     *
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     *
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        doMockSignUp("URL","Test","UT","123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     *
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     *
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        // Create a test account
        doMockSignUp("Large File","Test","LFT","123");
        doLogIn("LFT", "123");

        // Try to upload an arbitrary large file
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String fileName = "upload5m.zip";

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }



    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }


    @Test
    public void testPageAuthen() {
        // Check the title of the home page
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());

        // Check the title of the login page
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());

        // Check the title of the sign-up page
        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());
    }
    @Test
    public void testLogin() {
        // Sign up a new user
        doMockSignUp("le", "dat", "dat", "123");

        // Log in with the created user
        doLogIn("dat", "123");

        // Find and click the logout button
        WebElement logoutButton = driver.findElement(By.id("logout-button"));
        logoutButton.click();

        // Check that after login, we are redirected to the home page
        Assertions.assertFalse(driver.getTitle().equals("Home"));
        Assertions.assertEquals("Login", driver.getTitle());
    }

    public void redirectToNotesTab() {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Navigate to the home page
        driver.get("http://localhost:" + this.port + "/home");

        // Wait for the "Notes" tab to become visible
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));

        // Click on the "Notes" tab
        driver.findElement(By.id("nav-notes-tab")).click();
    }
    public void SignUpAndLoginForCrudTest(){
        // Visit the sign-up page.
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        // Fill out credentials
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys("le");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys("dat");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys("dat");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys("123");

        // Attempt to sign up.
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

        driver.get("http://localhost:" + this.port + "/login");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys("dat");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys("123");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();

        webDriverWait.until(ExpectedConditions.titleContains("Home"));


    }

    @Test
    public void testCreateNote() {
        // Step 1: Sign up and log in
        SignUpAndLoginForCrudTest();

        // Step 2: Navigate to the "Notes" tab
        WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
        notesTab.click();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Step 3: Ensure the "Notes" tab is displayed
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes")));
        Assertions.assertTrue(driver.findElement(By.id("nav-notes")).isDisplayed());

        // Step 4: Click the "Add Note" button
        WebElement addNoteButton = driver.findElement(By.id("add-note"));
        addNoteButton.click();

        // Step 5: Enter a note title and description
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement inputTitle = driver.findElement(By.id("note-title"));
        inputTitle.click();
        inputTitle.sendKeys("NoteTitle");

        WebElement inputDescription = driver.findElement(By.id("note-description"));
        inputDescription.click();
        inputDescription.sendKeys("NoteDescription.");

        // Step 6: Submit the note
        WebElement submitNote = driver.findElement(By.id("submitNote"));
        submitNote.click();

        // Step 7: Redirect back to the "Notes" tab
        redirectToNotesTab();
    }


    @Test
    public void testUpdateNote() {
        // Step 1: Create a new note
        testCreateNote();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Step 2: Click the "Edit Note" button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-note")));
        WebElement btnEditNote = driver.findElement(By.id("edit-note"));
        btnEditNote.click();

        // Step 3: Modify the note title and description
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement inputTitle = driver.findElement(By.id("note-title"));
        inputTitle.click();
        inputTitle.clear();
        inputTitle.sendKeys("Test Update");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement inputDescription = driver.findElement(By.id("note-description"));
        inputDescription.click();
        inputDescription.clear();
        inputDescription.sendKeys("UpdateDescription");

        // Step 4: Submit the updated note
        WebElement submitNote = driver.findElement(By.id("submitNote"));
        submitNote.click();

        // Step 5: Redirect back to the "Notes" tab
        redirectToNotesTab();

        // Step 6: Verify that the updated note's description is displayed in the table
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
        Assertions.assertTrue(driver.findElement(By.id("table-description")).getText().contains("UpdateDescription"));
    }

    @Test
    public void testDeleteNote() throws InterruptedException {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Step 1: Create a new note
        testCreateNote();

        // Step 2: Click the "Delete Note" button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-note")));
        WebElement btnDeleteNote = driver.findElement(By.id("delete-note"));
        btnDeleteNote.click();

        // Step 3: Redirect back to the "Notes" tab
        redirectToNotesTab();
    }

    public void redirectToCredentialsTab(){
        driver.get("http://localhost:" + this.port + "/home");
        driver.findElement(By.id("nav-credentials-tab")).click();
    }

    @Test
    public void testCreateCredential() throws InterruptedException {
        // Step 1: Sign up and log in
        SignUpAndLoginForCrudTest();

        // Step 2: Navigate to the "Credentials" tab
        WebElement credentialsTab = driver.findElement(By.id("nav-credentials-tab"));
        credentialsTab.click();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
        String inputCredentialPassword = "123456";

        // Step 3: Click the "Add Credential" button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-cre")));
        WebElement addCredentialsButton = driver.findElement(By.id("add-cre"));
        addCredentialsButton.click();

        // Step 4: Fill out the credential information
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement inputURL = driver.findElement(By.id("credential-url"));
        inputURL.click();
        inputURL.sendKeys("https://fpt.com/");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement inputUsername = driver.findElement(By.id("credential-username"));
        inputUsername.click();
        inputUsername.sendKeys("datmadd");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement inputPassword = driver.findElement(By.id("credential-password"));
        inputPassword.click();
        inputPassword.sendKeys(inputCredentialPassword);

        // Step 5: Submit the credential
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitCre")));
        WebElement submitNote = driver.findElement(By.id("submitCre"));
        submitNote.click();

        // Step 6: Redirect back to the "Credentials" tab
        redirectToCredentialsTab();

        // Step 7: Verify that the credentials table contains one credential
        WebElement credentialsTable = driver.findElement(By.id("credentialTable"));
        List<WebElement> credList = credentialsTable.findElements(By.tagName("tbody"));

        Assertions.assertEquals(1, credList.size());
    }

    @Test
    public void testEditCredential() throws InterruptedException {
        // Step 1: Create a credential
        testCreateCredential();

        WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

        // Step 2: Click the "Edit Credential" button
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-cre")));
        WebElement editCredentialsButton = driver.findElement(By.id("edit-cre"));
        editCredentialsButton.click();

        // Step 3: Modify the URL of the credential
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement inputURL = driver.findElement(By.id("credential-url"));
        inputURL.click();
        inputURL.sendKeys("https://fpt.com.vn");

        // Step 4: Submit the updated credential
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submitCre")));
        WebElement submitCredential = driver.findElement(By.id("submitCre"));
        submitCredential.click();

        // Step 5: Redirect back to the "Credentials" tab
        redirectToCredentialsTab();
    }

    @Test
    public void testDeleteCredential() throws InterruptedException {
        // Step 1: Create a credential
        testCreateCredential();

        // Step 2: Click the "Delete Credential" button
        WebElement deleteCredentialButton = driver.findElement(By.id("delete-cre"));
        deleteCredentialButton.click();

        // Step 3: Redirect back to the "Credentials" tab
        redirectToCredentialsTab();
    }




}
