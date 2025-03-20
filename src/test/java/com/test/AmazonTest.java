package com.test;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AmazonTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		Actions actions = new Actions(driver);

		try {
			// Step 1: Open Amazon India Website Aplication
			driver.get("https://www.amazon.in/");
			driver.manage().window().maximize();
			
			List<WebElement> captchaElements = driver.findElements(By.xpath("//h4[text()='Type the characters you see in this image:']"));

			if (!captchaElements.isEmpty() && captchaElements.get(0).isDisplayed())
			{
				System.out.println("CAPTCHA detected! Need to Solve it manually");

				// Wait until the main Amazon page loads after CAPTCHA is solved
				new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//img[contains(@src, 'captcha')]"))); // Wait up to 20 secondds for CAPTCHA resolution
				System.out.println("CAPTCHA resolved.");
			}

			// Step 2: Click on Electronics from dropdown & search iPhone 13
			Select select  = new Select(driver.findElement(By.id("searchDropdownBox")));
			select.selectByVisibleText("Electronics");
			WebElement searchBox = driver.findElement(By.id("twotabsearchtextbox"));
			searchBox.sendKeys("iPhone 13");


			// Wait for dropdown suggestions
			List<WebElement> suggestions = wait.until(
					ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".s-suggestion")));

			// Validate dropdown suggestions
			boolean allRelated = suggestions.stream().allMatch(e -> e.getText().toLowerCase().contains("iphone"));
			assert allRelated : "Not all suggestions are related to iPhone";

			// Step 3: Search specific variant iPhone 13 128GB and select from dropdown
			searchBox.clear();
			searchBox.sendKeys("iPhone 13 128GB");

			// Ensure old suggestions are stale before fetching new ones
			try {
				wait.until(ExpectedConditions.stalenessOf(suggestions.get(0)));
			} catch (Exception ignored) {
				System.out.println("Old elements already stale or not present.");
			}

			// Fetch new suggestions after ensuring old ones are gone
			suggestions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
					By.xpath("//div[contains(@class,'s-suggestion')]")));

			// Retry mechanism to handle intermittent stale elements
			boolean clicked = false;
			for (int i = 0; i < 3; i++) { // Retry up to 3 times
				try {
					WebElement firstSuggestion = wait.until(ExpectedConditions.elementToBeClickable(suggestions.get(0)));
					firstSuggestion.click();
					clicked = true;
					break; // Exit loop if successful
				} catch (org.openqa.selenium.StaleElementReferenceException e) {
					System.out.println("Retrying due to stale element...");
					suggestions = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[contains(@class,'s-suggestion')]")));
				}
			}

			// Step 4: Click on searched product & validate new tab opens
			WebElement firstProduct = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("(//span[contains(text(),'Apple iPhone 13 (128GB)')])[1]")));
			String mainWindow = driver.getWindowHandle();
			firstProduct.click();

			// Switch to new tab
			for (String handle : driver.getWindowHandles()) {
				if (!handle.equals(mainWindow)) {
					driver.switchTo().window(handle);
					break;
				}
			}
			assert driver.getTitle().contains("iPhone 13") : "New tab did not open correctly"; //throws error msg when assertion fails

			// Step 5: Click on Visit the Apple Store link
			WebElement appleStoreLink = wait.until(ExpectedConditions.elementToBeClickable(By.partialLinkText("Visit the Apple Store")));
			appleStoreLink.click();

			// Step 6: Click on Apple Watch dropdown and select "Appple Watch SE (GPS + Cellular)"

			WebElement appleWatchLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//span[text()='Apple Watch'])[1]")));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", appleWatchLink);

			WebElement desiredWatch = driver.findElement(By.xpath("//span[text()='Apple Watch SE (GPS + Cellular)']"));
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", desiredWatch);

			// Locate the product image
			WebElement watchImage = driver.findElement(By.xpath("(//img[@data-testid='image'])[3]"));

			// Perform hover action
			Actions actions2 = new Actions(driver);
			actions.moveToElement(watchImage).perform();

			// Wait for Quick Look button to be visible
			WebDriverWait wait2 = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement quickLookButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//button[@data-testid='quick-look-button'])[1]")));

			// Click on Quick Look button
			quickLookButton.click();

			// Step 8: Verify modal is related to same product
			WebElement modalTitle = driver.findElement(By.xpath("//div[@class='QuickLookExpansion__modal-innerWrapper__CM8U9']")); // Change ID accordingly
			assert modalTitle.getText().contains("Apple Watch SE") : "Incorrect product in modal";
			System.out.println("Passed");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} finally 
		{
			// Cleanup
			driver.quit();
		}
	}
}