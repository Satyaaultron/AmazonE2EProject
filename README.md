Amazon E2E Selenium Test:

This project automates an end-to-end test flow on Amazon India using Selenium WebDriver with Java.

Prerequisites:

Ensure you have the following installed on your system:

Java Development Kit (JDK) (Recommended: JDK 17 or later)

Download: Oracle JDK

Verify installation:

Apache Maven (if using Maven for dependencies)

Download: Maven

Verify installation:

Google Chrome & ChromeDriver

Download Chrome: Google Chrome

Download ChromeDriver: ChromeDriver

Ensure ChromeDriver version matches your installed Chrome version.

Git (for cloning the repository)

Download: Git

Verify installation:

Clone & Setup Project

Open a terminal or command prompt.

Clone the repository:

Navigate to the project folder:

Run the Test

If using Maven, run the following command:

If running manually in an IDE (like Eclipse or IntelliJ):

Open AmazonTest.java

Right-click and select Run as Java Application

Handling CAPTCHA

If Amazon asks for CAPTCHA, the test will pause.

Manually solve the CAPTCHA within 20 seconds.

The test will automatically continue after CAPTCHA is resolved.

Expected Output

The test will:

Open Amazon India.

Search for "iPhone 13" under the Electronics category.

Click on the first result and validate a new tab opens.

Navigate to the Apple Store page.

Select "Apple Watch SE (GPS + Cellular)".

Hover over the watch image and click the "Quick Look" button.

Validate the correct product details are displayed.

Troubleshooting

Element Not Found Errors: Increase WebDriverWait duration in AmazonTest.java.

ChromeDriver Version Mismatch: Ensure ChromeDriver matches your Chrome browser version.

Test Fails on CAPTCHA: Increase manual waiting time in the script.

Contributors:

Satyam Borgaonkar

