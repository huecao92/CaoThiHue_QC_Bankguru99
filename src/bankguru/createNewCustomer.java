package bankguru;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class createNewCustomer {
	
	WebDriver driver;
	String userName, passWord, name, dob, address, city, state, pin, phone, email, customerID, description;
	int amount;

	@BeforeClass
	public void beforeClass() {

		// Launch webbrowser
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.MILLISECONDS);
		driver.manage().window().maximize();
		
		// get URL cua app
		driver.get("http://demo.guru99.com/v4");
		
		// Assign values to variables
		userName = "mngr184796";
		passWord = "ApYbusu";
		name = "HueAutomation";
		dob = "03/02/1992";
		address = "123 D2";
		city = "HCM";
		state = "Binh Thanh";
		pin = "123456";
		phone = "123456789";
		email = "automation" + random() + "@gmail.com";
		amount = 10000;
		description = "Test";

	}

	@Test
	public void verifyNewCustomer() {

//===================Request 1: Verify Create New Customer============================================

		// Log in
		WebElement userTextbox = driver.findElement(By.xpath("//input[@name='uid']"));
		userTextbox.sendKeys(userName);
		
		WebElement passWordTextbox = driver.findElement(By.xpath("//input[@name='password']"));
		passWordTextbox.sendKeys(passWord);
		
		// Click on button Login
		WebElement loginButton = driver.findElement(By.xpath("//input[@value='LOGIN']"));
		loginButton.click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.MICROSECONDS);
		
		// Click New Customer after login successfully
		WebElement newUserlink = driver.findElement(By.xpath("//a[contains(text(),'New Customer')]"));
		newUserlink.click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.MICROSECONDS);
		
		// Enter customer info
		WebElement customerNameTextbox = driver.findElement(By.xpath("//input[@name='name']"));
		customerNameTextbox.sendKeys(name);

		WebElement femaleradio = driver.findElement(By.xpath("//input[@value=\"m\"]"));
		femaleradio.click();

		WebElement dobTextbox = driver.findElement(By.xpath("//input[@id='dob']"));
		removeAttributeInDOM(dobTextbox, "type");
		dobTextbox.sendKeys(dob);

		WebElement addressArea = driver.findElement(By.xpath("//textarea[@name='addr']"));
		addressArea.sendKeys(address);

		WebElement cityTextbox = driver.findElement(By.xpath("//input[@name='city']"));
		cityTextbox.sendKeys(city);

		WebElement stateTextbox = driver.findElement(By.xpath("//input[@name='state']"));
		stateTextbox.sendKeys(state);

		WebElement pinTextbox = driver.findElement(By.xpath("//input[@name='pinno']"));
		pinTextbox.sendKeys(pin);

		WebElement phoneNumberTextbox = driver.findElement(By.xpath("//input[@name='telephoneno']"));
		phoneNumberTextbox.sendKeys(phone);

		WebElement emailTextbox = driver.findElement(By.xpath("//input[@name='emailid']"));
		emailTextbox.sendKeys(email);

		WebElement passwordTextbox = driver.findElement(By.xpath("//input[@name='password']"));
		passwordTextbox.sendKeys(passWord);

		WebElement submitButton = driver.findElement(By.xpath("//input[@value='Submit']"));
		submitButton.click();

		// Verify create new customer successfully message
		WebElement createCustomerSuccessfullyMsg = driver.findElement(
				By.xpath("//p[@class = \"heading3\" and text()= \"Customer Registered Successfully!!!\"]"));
		Assert.assertTrue(createCustomerSuccessfullyMsg.isDisplayed());

		// get out customerID  after creating
		WebElement customerID = driver
				.findElement(By.xpath("//td[contains(text(),'Customer ID')]/following-sibling::td"));
		String cuID = customerID.getText();
		

// ==================== Request 2: Verify Create New Account with cuID in request 1 =======================================

		// Click on New Account link
		driver.findElement(By.xpath("//a[contains(text(),'New Account')]")).click();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.MICROSECONDS);

		// Enter new account info
		driver.findElement(By.xpath("//input[@name='cusid']")).sendKeys(cuID);

		Select accTypeDropdown = new Select(driver.findElement(By.xpath("//select[@name='selaccount']")));
		accTypeDropdown.selectByValue("Savings");

		WebElement initialDeposit = driver.findElement(By.xpath("//input[@name='inideposit']"));
		initialDeposit.sendKeys("10000");
		
		WebElement submitbtn = driver.findElement(By.xpath("//input[@value='submit']"));
		submitbtn.click();

		// Verify create new Account successfully message
		WebElement createAccountSuccessfullyMsg = driver
				.findElement(By.xpath("//p[@class = \"heading3\" and text()= \"Account Generated Successfully!!!\"]"));
		Assert.assertTrue(createAccountSuccessfullyMsg.isDisplayed());
		
		//get out account ID after creating
		WebElement accountID = driver
				.findElement(By.xpath("//td[contains(text(),'Account ID')]/following-sibling::td"));
		String accID = accountID.getText();
		

//=========================== Request 3: Verify Deposit works fine with accountID in request 2 =========================
		
		
		driver.findElement(By.xpath("//a[contains(text(),'Deposit')]")).click();

		// Enter deposit info
		driver.findElement(By.xpath("//input[@name='accountno']")).sendKeys(accID);
		driver.findElement(By.xpath("//input[@name='ammount']")).sendKeys(Integer.toString(amount));
		driver.findElement(By.xpath("//input[@name='desc']")).sendKeys(description);
		driver.findElement(By.xpath("//input[@value='Submit']")).click();

		// Verify Deposit successfully info
		WebElement depositSuccessfullyMsg = driver.findElement(By.xpath("//p[@class='heading3']"));
		Assert.assertEquals(depositSuccessfullyMsg.getText(), "Transaction details of Deposit for Account " + accID);
		
		WebElement depositAmount = driver.findElement(By.xpath("//td[contains(text(),'Amount Credited')]/following-sibling::td"));
		Assert.assertEquals(depositAmount.getText(), Integer.toString(amount));
		
		WebElement accountNo = driver.findElement(By.xpath("//td[contains(text(),'Account No')]/following-sibling::td"));
		Assert.assertEquals(accountNo.getText(), accID);
		
		WebElement transactionType = driver
				.findElement(By.xpath("//td[contains(text(),'Type of Transaction')]/following-sibling::td"));
		Assert.assertEquals(transactionType.getText(), "Deposit");
		
		WebElement desc = driver.findElement(By.xpath("//td[contains(text(),'Description')]/following-sibling::td"));
		Assert.assertEquals(desc.getText(), description);
	}

	private int random() {
		// TODO Auto-generated method stub
		Random random = new Random();
		int number = random.nextInt(9999);
		return number;
	}

	public void removeAttributeInDOM(WebElement element, String atributeName) {
		JavascriptExecutor javascript = (JavascriptExecutor) driver;
		javascript.executeScript("arguments[0].removeAttribute('" + atributeName + "')", element);

	}

	@AfterClass
	public void afterClass() {
		driver.quit();
	}

}
