/**
 * 
 */
package com.marketing.utils;

import com.google.common.base.Function;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class LinkedInDriver {
	Logger logger= LogManager.getLogger(LinkedInDriver.class);

	private  WebDriver driver;
	private  FluentWait<WebDriver> wait;
	private WebElement element;
	private  int DEFAULT_TIMEOUT=5;
	private int POLLING_TIME=100;

	//protected ExtentTest test;

	public LinkedInDriver(WebDriver driver){
		this.driver=driver;
	}



//	public void launchBrowser() throws MalformedURLException {
//		String browserName=config.getProperty("browser");
//		logger.info("Launching browser :: "+browserName);
//		if (browserName.equals("FIREFOX") || browserName.equals("FF")) {
//			WebDriverManager.firefoxdriver().setup();
//			driver=new FirefoxDriver();
//		}else if(browserName.equalsIgnoreCase("chrome")) {
////			WebDriverManager.chromedriver().setup();
////			//System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+File.separator+"src"+File.separator+"test"+File.separator+"resources"+File.separator+"chromedriver.exe");
////			String downloadFilepath = PageConstants.OUTPUT_REPORT_PATH;
////			Map<String, Object> prefs = new HashMap<String, Object>();
////			prefs.put("download.default_directory",  downloadFilepath);
////			ChromeOptions options = new ChromeOptions();
////			options.setExperimentalOption("prefs", prefs);
////			driver = new ChromeDriver(options);
//
//			DesiredCapabilities dcap = DesiredCapabilities.chrome();
//			dcap.setCapability("enableVNC", true);
//			dcap.setBrowserName("chrome");
//			dcap.setVersion("80.0");
//
//
//			URL url = new URL("http://localhost:4444/wd/hub");
//			driver = new RemoteWebDriver(url, dcap);
//
//		}
//		else if(browserName.equalsIgnoreCase("IE")) {
//			WebDriverManager.iedriver().setup();
//			driver=new InternetExplorerDriver();
//		}
//		driver.manage().window().maximize();
//		//driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//		logger.info("Launched browser :: "+browserName);
//	}

	public void closeBrowser(){
		logger.info("Closing browser");
		driver.close();
	}

	public void quitBrowser(){
		logger.info("Quiting browser");
		driver.quit();
	}

	public void navigateTo(String url){
		logger.info("Navigating to url :: "+url);
		driver.navigate().to(url);
		waitForPageLoad(30);
	}

	public void clearCacheFromBrowser() throws InterruptedException{
		driver.manage().deleteAllCookies();
		driver.navigate().refresh();
		pauseExecutionFor(2000);
	}

	public WebElement findElement(By by) throws Exception{
		element=waitForElement(by);
		return element;
	}

	public List<WebElement> findElements(By by){
		List<WebElement> list;
		//turnOffImplicitWaits();
		list=driver.findElements(by);
		//turnOnImplicitWaits();
		return list;
	}

	public boolean IsLocatorVisible(By by) {
		boolean IsVisible=false;
		try{
			IsVisible=driver.findElement(by).isDisplayed();
		}catch(Exception e){
			logger.debug(by.toString()+" is not visible");
		}
		logger.info("Is Locator "+by.toString()+" visible :: "+IsVisible);
		return IsVisible;
	}


	/////
	public void IsLocatedDeleted(By by) {
		//boolean IsDeleted = false;
		do{  
			System.out.println("Waiting for object to be deleted");

		}while(IsLocatorVisible(by)); 

	}


	public boolean IsLocatorPresent(By by){
		boolean IsPresent=false;
		logger.info("Finding if element is present :: "+by.toString());
		if(this.findElements(by).size()>0){
			IsPresent=true;
		}
		logger.info("Is element present :: "+IsPresent);
		return IsPresent;
	}

	public void clickIfPresent(By by){
		boolean IsPresent=false;
		try{
			clickLocatorByJS(by);
		}catch (Exception e){

		}


	}

	//	private void turnOffImplicitWaits() {
	//		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
	//	}
	//
	//	private void turnOnImplicitWaits() {
	//		driver.manage().timeouts().implicitlyWait(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
	//	}

	public void waitForLocatorToBeClickable(By locator) {
		logger.info("Waiting for locator to be clickable :: "+locator,true);
		long startTime = System.currentTimeMillis();
		wait = new WebDriverWait(driver, DEFAULT_TIMEOUT,POLLING_TIME);
		wait.until(ExpectedConditions.elementToBeClickable(locator));
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("Finished waiting for locator to be clickable after " + totalTime + " milliseconds.",true);
	} 

	public void waitForLocatorToBeClickable(By locator, int timeOut) {
		logger.info("Waiting for locator to be clickable :: "+locator,true);
		wait = new WebDriverWait(driver, timeOut,POLLING_TIME);
		wait.until(ExpectedConditions.elementToBeClickable(locator));
		logger.info("Waited for locator to be clickable: "+locator,true);
	} 

	public void waitForElementoBeClickable(WebElement webElement) {
		logger.info("Waiting for element to be clickable :: "+webElement,true);
		long startTime = System.currentTimeMillis();
		wait = new WebDriverWait(driver, DEFAULT_TIMEOUT,POLLING_TIME);
		wait.until(ExpectedConditions.elementToBeClickable(webElement));
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("Finished waiting for locator to be clickable after " + totalTime + " milliseconds.",true);
	} 

	public void waitForElementToBeClickable(WebElement webElement, int timeOut) {
		logger.info("Waiting for Element to be clickable :: "+webElement,true);
		wait = new WebDriverWait(driver, timeOut,POLLING_TIME);
		wait.until(ExpectedConditions.elementToBeClickable(webElement));
		logger.info("Waited for locator to be clickable: "+webElement,true);
	} 

	public void waitForLocatorToBePresent(By by){
		logger.info("Waiting for locator to be Present :: "+by,true);
		wait=new WebDriverWait(driver, DEFAULT_TIMEOUT,POLLING_TIME);
		wait.until(ExpectedConditions.presenceOfElementLocated(by));
		logger.info("Waited for locator to be present: "+by);
	}

	public void waitForLocatorToBePresent(final By by, int timeOut,boolean ...failOnException) throws Exception{
		boolean IsException=false;
		try {
			logger.info("Waiting for locator to be Present :: "+by,true);
			wait=new WebDriverWait(driver, timeOut,POLLING_TIME);
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
			logger.info("Waited for locator to be present: "+by);
		} catch (Exception e) {
			IsException=true;
		}
		if(failOnException.length==0 && IsException){
			throw new Exception("Not Element "+by.toString() +"found in "+timeOut+" seconds");
		}
		if(failOnException.length!=0){
			if(failOnException[0]==true && IsException){
				throw new Exception("Not Element "+by.toString() +"found in "+timeOut+" seconds");
			}
		}

	}

	public void waitForLocatorToBeVisible(final By by){
		logger.info("Waiting for locator to be visible: "+by);
		long startTime = System.currentTimeMillis();
		wait=new WebDriverWait(driver, DEFAULT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("Finished waiting for locator to be visible after " + totalTime + " milliseconds.");
	}

	public void waitForLocatorToBeVisible(final By by,int timeOut){
		logger.info("Waiting for locator to be visible: "+by);
		long startTime = System.currentTimeMillis();
		wait=new WebDriverWait(driver, DEFAULT_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("Finished waiting for locator to be visible after " + totalTime + " milliseconds.");
	}

	public boolean waitForPageLoad() {
		logger.info("Waiting for Page to load",true);
		boolean isLoaded = false;
		long startTime = System.currentTimeMillis();
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript(
						"return document.readyState").equals("complete");
			}
		};
		wait = new WebDriverWait(driver, DEFAULT_TIMEOUT,POLLING_TIME);
		wait.until(pageLoadCondition);
		isLoaded = true;
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("Finished waiting for page to load after " + totalTime + " milliseconds.",true);
		return isLoaded;
	}


	public boolean waitForAjax(){
		logger.info("Waiting for AJAX to load");
		boolean isLoaded = false;
		long startTime = System.currentTimeMillis();
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript(
						"return (window.jQuery != null) && (jQuery.active === 0);").equals("true");
			}
		};
		wait = new WebDriverWait(driver, DEFAULT_TIMEOUT,POLLING_TIME).ignoring(TimeoutException.class);
		wait.until(pageLoadCondition);
		isLoaded = true;
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("Finished waiting for page to load after " + totalTime + " milliseconds.",true);
		return isLoaded;
	}

	public boolean waitForAjax(int timeOutInSeconds){
		logger.info("Waiting for AJAX to load");
		boolean isLoaded = false;
		try {
			long startTime = System.currentTimeMillis();
			ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
				public Boolean apply(WebDriver driver) {
					return ((JavascriptExecutor) driver).executeScript(
							"return (window.jQuery != null) && (jQuery.active === 0);").equals("true");
				}
			};
			wait = new WebDriverWait(driver, timeOutInSeconds,POLLING_TIME);
			wait.until(pageLoadCondition);
			isLoaded = true;
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			logger.info("Finished waiting for page to load after " + totalTime + " milliseconds.",true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isLoaded;
	}

	public boolean waitForPageLoad(int timeout) {
		logger.info("Waiting for Page to load",true);
		boolean isLoaded = false;
		long startTime = System.currentTimeMillis();
		ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript(
						"return document.readyState").equals("complete");
			}
		};
		wait = new WebDriverWait(driver, timeout,POLLING_TIME);
		wait.until(pageLoadCondition);
		isLoaded = true;
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("Finished waiting for page to load after " + totalTime + " milliseconds.",true);
		return isLoaded;
	}

	public void pauseExecutionFor(long lTimeInMilliSeconds) throws InterruptedException {
		logger.info("Waiting for "+lTimeInMilliSeconds+" millseconds",true);
		Thread.sleep(lTimeInMilliSeconds);
	}

	public void selectFromList(By by,String sValueToBeSelected) throws Exception {
		sValueToBeSelected=sValueToBeSelected.toUpperCase();
		List<WebElement> elements=findElements(by);
		String text="";
		boolean flag = false;
		for (WebElement e : elements) {
			text=e.getText().toUpperCase();
			logger.info("Found :: "+text+" . Selecting value :: "+sValueToBeSelected,true);
			if (text.equalsIgnoreCase(sValueToBeSelected)) {
				logger.info("Match Found !!! Clicking value :: "+sValueToBeSelected,true);
				clickElement(e);
				flag = true;
				break;
			}
		}
		if (flag == false) {
			throw new Exception("No match found in the list for value->"+ sValueToBeSelected);
		}
	}

	public void selectFromListByJS(By by,String sValueToBeSelected) throws Exception {
		sValueToBeSelected=sValueToBeSelected.toUpperCase();
		List<WebElement> elements=findElements(by);
		String text="";
		boolean flag = false;
		for (WebElement e : elements) {
			text=e.getText().toUpperCase();
			logger.info("Found :: "+text+" . Selecting value :: "+sValueToBeSelected,true);
			if (text.equalsIgnoreCase(sValueToBeSelected)) {
				logger.info("Match Found !!! Clicking value :: "+sValueToBeSelected,true);
				moveToElement(e);
				clickElementByJS(e);
				flag = true;
				break;
			}
		}
		if (flag == false) {
			throw new Exception("No match found in the list for value->"+ sValueToBeSelected);
		}
	}

	public void doubleClickFromListByAction(By by,String sValueToBeSelected) throws Exception {
		sValueToBeSelected=sValueToBeSelected.toUpperCase();
		List<WebElement> elements=findElements(by);
		String text="";
		boolean flag = false;
		for (WebElement e : elements) {
			text=e.getText().toUpperCase();
			logger.info("Found :: "+text+" . Selecting value :: "+sValueToBeSelected,true);
			if (text.equalsIgnoreCase(sValueToBeSelected)) {
				logger.info("Match Found !!! Clicking value :: "+sValueToBeSelected,true);
				//				moveToElement(e);
				pauseExecutionFor(2000);
				doubleClickElementByAction(e);
				//sendKeys(element, Keys.ENTER);
				flag = true;
				break;
			}
		}
		if (flag == false) {
			throw new Exception("No match found in the list for value->"+ sValueToBeSelected);
		}
	}


	public boolean IsElementTextPresentInList(By by, String value){
		boolean IsPresent=false;
		List<WebElement> elements=findElements(by);
		String text="";
		for (WebElement e : elements) {
			text=e.getText();
			logger.info("Found :: "+text+" . Selecting value :: "+value,true);
			if (text.equalsIgnoreCase(value)) {
				logger.info("Match Found !!! Clicking value :: "+value,true);
				IsPresent=true;
				break;
			}
		}
		return IsPresent;
	}

	public void clickLocatorByJS(By by) throws Exception {
		WebElement element = findElement(by);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		logger.info("Clicking Locator by using JScript "+ by.toString(),true);
		executor.executeScript("arguments[0].click();", element);
	}

	public String getTextByJS(WebElement element){
		String text="";
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		logger.info("Clicking Locator by using JScript "+ element);
		text=(String) executor.executeScript("return arguments[0].innerHTML;", element); 
		return text;
	}


	public String getTextByJS(By by) throws Exception{
		String text="";
		WebElement element = this.findElement(by);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		logger.info("Clicking Locator by using JScript "+ element);
		text=(String) executor.executeScript("return arguments[0].innerHTML;", element); 
		return text;
	}


	public void clickLocator(By by) throws Exception{
		element=findElement(by);
		waitForLocatorToBeClickable(by);
		logger.info("Clicking Locator :: "+by.toString());
		element.click();
		logger.info("Locator is clicked :: "+by.toString());

	}
	public void clickLocator(WebElement element) throws Exception{
		logger.info("Clicking Locator :: "+element.toString());
		element.click();
		logger.info("Locator is clicked :: "+element.toString());
	}


	public void submitLocator(By by) throws Exception{
		element=findElement(by);
		//waitForLocatorToBeVisible(by);
		logger.info("Clicking Locator :: "+by.toString());
		element.submit();
		logger.info("Locator is clicked :: "+by.toString());
	}

	public void clickLocatorByAction(By by) throws Exception{
		//waitForPageLoad();
		element=findElement(by);
		//waitForLocatorToBeVisible(by);
		logger.info("Clicking Locator by Action :: "+by.toString());
		Actions actions = new Actions(driver);
		actions.click().build().perform();
		logger.info("Locator is clicked by action :: "+by.toString());
	}

	public void clickElementByAction(WebElement element){
		logger.info("Clicking element by Action :: "+element.toString());
		Actions actions = new Actions(driver);
		actions.click(element).build().perform();
		logger.info("Element is clicked :: "+element.toString());
	}

	public void clickElement(WebElement element){
		logger.info("Clicking element :: "+element.toString());
		waitForElementoBeClickable(element);
		element.click();
		logger.info("Element is clicked :: "+element.toString());
	}

	public void clickElementByJS(WebElement eElement){
		logger.info("Clicking webelement by JS");
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		logger.info("Clicking Locator by using JScript "+ eElement);
		executor.executeScript("arguments[0].click();", element);
		logger.info("Element is clicked by JS :: "+element.toString());
	}

	public void clickLocatorAndWaitFor(By by, int timeOut) throws Exception{
		this.findElement(by).click();
		pauseExecutionFor(timeOut);
	}

	public void sendKeys(By by,String value) throws Exception{
		waitForLocatorToBeVisible(by);
		element=this.findElement(by);
		logger.info("Typing input :: "+value);
		element.clear();
		element.sendKeys(value);
	}

	public void sendKeys(WebElement element,String value) throws Exception{
		System.out.println(element==null);
		element.clear();
		element.sendKeys(value);
	}

	public void sendKeysLocatorByJS(By by,String text) throws Exception{
		logger.info("Typing text by JS :: "+text);
		element=this.findElement(by);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		logger.info("Clicking Locator by using JScript "+ element);
		executor.executeScript("arguments[0].value=arguments[1];", element,text);
		logger.info("Element is clicked by JS :: "+element.toString());
	}

	public void sendKeysElementByJS(WebElement eElement,String text){
		logger.info("Typing text by JS :: "+text);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		logger.info("Clicking Locator by using JScript "+ eElement);
		executor.executeScript("arguments[0].value=arguments[1];", eElement,text);
		logger.info("Element is clicked by JS :: "+element.toString());
	}

	public void sendKeys(By by,Keys key) throws Exception{
		waitForLocatorToBeVisible(by);
		element=this.findElement(by);
		logger.info("Sending keyboard input :: "+key);
		element.sendKeys(key);
	}

	public void enter() throws Exception{
		element.sendKeys(Keys.ENTER);
	}

	public void sendKeys(WebElement element,Keys key){
		logger.info("Sending keyboard input :: "+key);
		element.sendKeys(key);
	}

	public void clear(By by) throws Exception{
		element=findElement(by);
		logger.info("Clearing input field :: "+by.toString(),true);
		element.clear();
	}

	public String getText(By by){
		long startTime = System.currentTimeMillis();
		String text="";
		try {
			text=this.findElement(by).getText();
			logger.info("Text from "+ by+" is "+text);
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			logger.info("Finished getting text from locator in " + totalTime + " milliseconds.",true);
		} catch (Exception e) {
			logger.debug("Issue in finding element "+e.getMessage());
		}
		return text; 
	}

	public String getText(WebElement element){
		String text="";
		try{
			text=element.getText();
		}catch (Exception e){
			e.printStackTrace();
		}
		return text;
	}



	public boolean waitExplicitlyForLocatorPresence(By locator) throws InterruptedException {
		boolean flag = false;
		int time=DEFAULT_TIMEOUT;
		try {
			//turnOffImplicitWaits();
			for(int count=1; count<=time; count++) {
				logger.info("Checking for locator ::"+locator.toString()+"...Attempt number :: "+count);
				pauseExecutionFor(500);
				if(IsLocatorPresent(locator)) {
					flag = true;
					if(count == 4)
						logger.info("Succesfully found locator :: "+locator.toString()+" within "+count+" attempts");
					break;
				}
			}
		} finally {
			//turnOnImplicitWaits();
		}
		return flag;
	}

	public boolean waitExplicitlyForLocatorPresence(By locator, int time) throws InterruptedException {
		boolean flag = false;
		try {
			for(int count=1; count<=time; count++) {
				logger.info("Checking for locator ::"+locator.toString()+"...Attempt number :: "+count);
				pauseExecutionFor(500);
				//pauseExecutionFor(time);
				if(IsLocatorPresent(locator)) {
					flag = true;
					if(count == 4 || flag)
						logger.info("Succesfully found locator :: "+locator.toString()+" within "+count+" attempts");
					break;
				}
			}
		} finally {
		}
		return flag;
	}

	public String getAttribute(By by,String attribute) throws Exception{
		String value="";
		value=this.findElement(by).getAttribute(attribute);
		value=Optional.ofNullable(value).isPresent()?value:"";
		logger.info("Getting Attribute :: "+attribute+" from locator :: "+by.toString(),true);
		return value;
	}

	public String getAttribute(WebElement element,String attribute){
		String value="";
		value=element.getAttribute(attribute);
		return value;
	}

	public void selectValueFromOptions(By by, String valueToSelect) throws Exception{
		Select select = new Select(findElement(by));
		logger.info("Selecting Value :: "+ valueToSelect+" from locator :: "+ by.toString(),true);
		select.selectByValue(valueToSelect);
	}

	public void selectTextFromOptions(By by, String textToSelect) throws Exception{
		try {
			logger.info("Selecting :: "+textToSelect+" from locator "+by.toString());
			long startTime = System.currentTimeMillis();
			Select select = new Select(findElement(by));
			select.selectByVisibleText(textToSelect);
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			logger.info("Finished selecting text from drop down in " + totalTime + " milliseconds.");
		} catch (Exception e) {
			throw new Exception("Option with value ::"+textToSelect+" is not present in dropdown :: "+by.toString());
		}
	}

	public void selectTextFromOptions(WebElement element, String textToSelect){
		Select select = new Select(element);
		select.selectByVisibleText(textToSelect);
	}

	public void selectIndexFromOptions(By by, int indexToSelect) throws Exception{
		Select select = new Select(findElement(by));
		select.selectByIndex(indexToSelect);
	}

	public void selectIndexFromOptions(WebElement element, int indexToSelect){
		Select select = new Select(element);
		select.selectByIndex(indexToSelect);
	}

	public String getSelectedValueFromOptions(WebElement element){
		String text="";
		Select select = new Select(element);
		text=select.getFirstSelectedOption().getText();
		return text;
	}

	public String getSelectedValueFromOptions(By by) throws Exception{
		String text="";
		Select select = new Select(findElement(by));
		text=select.getFirstSelectedOption().getText();
		return text;
	}

	public void waitForLocatorToBeStale(By by) throws Exception{
		logger.info("Waiting for locator to stale :: "+by.toString(),true);
		wait=new WebDriverWait(driver, DEFAULT_TIMEOUT,POLLING_TIME);
		wait.until(ExpectedConditions.stalenessOf(findElement(by)));
	}

	public void waitForLocatorToBeDeleted(final By by){
		logger.info("Waiting for locator to be deleted :: "+by.toString());
		long startTime   = System.currentTimeMillis();
		wait=new WebDriverWait(driver, DEFAULT_TIMEOUT,POLLING_TIME);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver){
				return findElements(by).size()==0;
			}
		});
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("Finished waiting for locator to be deleted in " + totalTime + " milliseconds.");
	}

	public void waitForLocatorToBeDeleted(final By by,int time){
		logger.info("Waiting for locator to be deleted :: "+by.toString());
		long startTime   = System.currentTimeMillis();
		wait=new WebDriverWait(driver, time,POLLING_TIME);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver){
				return findElements(by).size()==0;
			}
		});
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("Finished waiting for locator to be deleted in " + totalTime + " milliseconds.");
	}
	public void switchToFrame(By by) throws Exception{
		logger.info("Switiching to frame :: "+by.toString(),true);
		driver.switchTo().frame(findElement(by));
	}

	public void switchToFrame(int frameIndex){
		logger.info("Switching to frame by index :: "+frameIndex,true);
		driver.switchTo().frame(frameIndex);
	}

	public void switchToFrame(String frameName){
		logger.info("Switching to frame by name :: "+frameName,true);
		driver.switchTo().frame(frameName);
	}

	public void switchToDefault(){
		logger.info("Switching to parent window");
		driver.switchTo().defaultContent();
		logger.info("Switched succesfully to defaul");
	}

	public String getScreenshot() throws IOException{
		String FullSnapShotFilePath = "";
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String sFilename = "";
		sFilename = "Screenshot-" + getDateTime() + ".png";
		FullSnapShotFilePath = System.getProperty("user.dir")+ "\\test-output\\ScreenShots\\" + sFilename;
		FileUtils.copyFile(scrFile, new File(FullSnapShotFilePath));
		return FullSnapShotFilePath;
	}

	public String getDateTime() {
		String sDateTime = "";
		try {
			SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
			Date now = new Date();
			String strDate = sdfDate.format(now);
			String strTime = sdfTime.format(now);
			strTime = strTime.replace(":", "-");
			sDateTime = "D" + strDate + "_T" + strTime;
		} catch (Exception e) {
			System.err.println(e);
		}
		return sDateTime;
	}

	public String getRandomNumber(){
		return ""+System.currentTimeMillis();
	}

	public String getTitle(){
		return driver.getTitle();
	}

	public void waitForFrameAndSwitchToIt(By by){
		logger.info("Waiting for frame and switching to :: "+ by.toString(),true);
		wait=new WebDriverWait(driver, DEFAULT_TIMEOUT,POLLING_TIME);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
	}

	public void waitForFrameAndSwitchToIt(By by, int timeOut){
		logger.info("Waiting for iFrame");
		long startTime = System.currentTimeMillis();
		wait=new WebDriverWait(driver, timeOut,POLLING_TIME);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("Switched to iFrame in " + totalTime + " milliseconds.");
	}

	public void waitForFrameAndSwitchToIt(int frameNumber){
		wait=new WebDriverWait(driver, DEFAULT_TIMEOUT,POLLING_TIME);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameNumber));
	}

	public void waitForFrameAndSwitchToIt(String frameName){
		wait=new WebDriverWait(driver, DEFAULT_TIMEOUT,POLLING_TIME);
		wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameName));
	}

	public void waitForDropDownToLoad(final By by) throws Exception{
		logger.info("Waiting for drop down to load");
		long startTime = System.currentTimeMillis();
		final Select dropList=new Select(findElement(by));
		wait=new WebDriverWait(driver, DEFAULT_TIMEOUT,POLLING_TIME);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver){
				return (!dropList.getOptions().isEmpty());
			}
		});
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("Finished waiting for drop down to load " + totalTime + " milliseconds.");
	}

	public void waitForDropDownToLoad(final By by, int timeOut) throws Exception{
		logger.info("Waiting for drop down to load");
		long startTime = System.currentTimeMillis();
		final Select dropList=new Select(findElement(by));
		wait=new WebDriverWait(driver, timeOut,POLLING_TIME);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver){
				return (!dropList.getOptions().isEmpty());
			}
		});
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("Finished waiting for drop down to load " + totalTime + " milliseconds.");
	}
	//	public void highlightElementBorder(WebElement element) {
	//		if(config.getProperty("highlight").equalsIgnoreCase("1")){
	//			try{
	//				for (int i = 0; i < 1; i++) {
	//					JavascriptExecutor js = (JavascriptExecutor) driver;
	//					js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
	//							element, "background: yellow;border: 4px solid red;");
	//					Thread.sleep(500L);
	//					js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
	//							element, "");
	//				}
	//			}catch(Exception e){
	//				e.printStackTrace();
	//			}
	//		}
	//
	//	}

	public int getNumberOfWindows(){
		Set<String>allHanles= driver.getWindowHandles();
		return allHanles.size();
	}

	public void switchToLastWindow(){
		Set<String>allHanles= driver.getWindowHandles();
		Iterator<String> iterator=allHanles.iterator();
		String lastHandle="";
		while(iterator.hasNext()){
			lastHandle=iterator.next();
		}
		logger.info("Switching to Last Window",true);
		driver.switchTo().window(lastHandle);
	}

	public void switchToWindow(String windowTitle) {
		Set<String> windows = driver.getWindowHandles();
		for (String window : windows) {
			driver.switchTo().window(window);
			if (driver.getTitle().contains(windowTitle)) {
				return;
			}
		}
	}

	public boolean IsCheckBoxSelected(By by) throws Exception{
		return findElement(by).isSelected();
	}

	public void refreshPage(){
		logger.info("Refreshing the page",true);
		driver.navigate().refresh();
	}

	public void waitForTitle(final String title){
		logger.info("Waiting for title :: "+title);
		wait=new WebDriverWait(driver, DEFAULT_TIMEOUT);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver){
				return driver.getTitle().equalsIgnoreCase(title);
			}
		});
		logger.info("Waited for title :: "+title);
	}

	public void doubleClickElement(WebElement element) throws InterruptedException{
		clickElement(element);
		pauseExecutionFor(500);
		clickElement(element);
	}

	public void doubleClickElementByAction(WebElement element) throws InterruptedException{
		logger.info("Clicking Element by Action :: "+element.toString());
		Actions action = new Actions(driver);
		action.doubleClick(element).build().perform();
	}

	public void doubleClickLocatorByAction(By by) throws Exception{
		logger.info("Double clicking Element by Action :: "+element.toString());
		WebElement element=findElement(by);
		Actions action = new Actions(driver);
		action.doubleClick(element).build().perform();
	}

	public void doubleClickElement(By by) throws Exception{
		element=findElement(by);
		clickElement(element);
		pauseExecutionFor(500);
		clickElement(element);
	}


	public void waitForAttributeChange( By by,final String attributeName,final String attributeValue) throws Exception{
		logger.info("Waiting for attribute to change to::"+attributeValue);
		final WebElement element=findElement(by);
		wait=new WebDriverWait(driver, DEFAULT_TIMEOUT);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver){
				return element.getAttribute(attributeName).equalsIgnoreCase(attributeValue);
			}
		});
		logger.info("Waited for attribute to change to::"+attributeValue);
	}

	public void waitForTextChange( By by,final String attributeValue) throws Exception{
		logger.info("Waiting for attribute to change to::"+attributeValue);
		final WebElement element=findElement(by);
		wait=new WebDriverWait(driver, DEFAULT_TIMEOUT);
		wait.until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver){
				return element.getText().equalsIgnoreCase(attributeValue);
			}
		});
		logger.info("Waited for attribute to change to::"+attributeValue);
	}


	public WebElement waitForElement(final By findBy) throws Exception {
		logger.info("Waiting for element :: "+findBy.toString());
		long startTime = System.currentTimeMillis();
		WebElement webElement = null;
		try{
			wait=new WebDriverWait(driver, DEFAULT_TIMEOUT,POLLING_TIME);
			webElement= wait.until(new Function<WebDriver, WebElement>() {
				public WebElement apply(WebDriver driver) {
					logger.info("Trying to find element " + findBy.toString(),true);                
					WebElement element = driver.findElement(findBy);
					return element;
				}
			});
			long endTime = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			logger.info("Finished finding element after waiting for " + totalTime + " milliseconds.");
		}catch(Exception e){
			logger.debug("Error while waiting for element ::"+findBy.toString());
			throw new Exception("Error while waiting for element ::"+findBy.toString());
		}
		return webElement;
	}

	public String getCurrentMonth(){
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
		return localCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
	}

	public String getCurrentYear(){
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
		return ""+localCalendar.get(Calendar.YEAR);
	}


	public String getFutureDateBy(int daysFromToday){
		String text="";
		Date dt = new Date();
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
		localCalendar.setTime(dt);
		localCalendar.add(Calendar.DATE, daysFromToday);
		dt = localCalendar.getTime();
		String month=new SimpleDateFormat("MMMM").format(dt);
		String date=new SimpleDateFormat("dd").format(dt);
		text=month+"#"+date;
		return text;
	}

	public String convertTimeFormat(String originalTime,String originalFormat, String targetFormat) throws ParseException{
		String targetTime="";
		SimpleDateFormat orgFormat = new SimpleDateFormat(originalFormat);
		SimpleDateFormat tarFormat = new SimpleDateFormat(targetFormat);
		Date date =orgFormat.parse(originalTime);
		targetTime=tarFormat.format(date);
		return targetTime;
	}

	public String incrementTimeBy(String startTime,String format,String calendarField ,int amount) throws ParseException{
		String newTime="";
		SimpleDateFormat time = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		//c.setTime(time.parse("12:30AM"));
		c.setTime(time.parse(startTime));
		if(calendarField.equalsIgnoreCase("MINUTE")){
			c.add(Calendar.MINUTE, amount);
		}else if(calendarField.equalsIgnoreCase("HOUR")){
			c.add(Calendar.HOUR, amount);
		}
		newTime=time.format(c.getTime());
		return newTime;
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} 
		catch (Exception e) {
			return false;
		} 
	}

	public void waitAndAcceptAlert(){
		wait= new WebDriverWait(driver, DEFAULT_TIMEOUT,POLLING_TIME);
		wait.until(ExpectedConditions.alertIsPresent());
		Alert al = driver.switchTo().alert();
		al.accept();
	}

	public void acceptAlert(){
		Alert al = driver.switchTo().alert();
		al.accept();
	}

	public String getCurrentUrl(){
		return driver.getCurrentUrl();
	}

	public void moveToElement(WebElement element){
		Actions builder = new Actions(driver); 
		builder.moveToElement(element).build().perform();
	}

	public void moveToElementByJS(WebElement element){
		((JavascriptExecutor)
				driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}


	public void moveToLocator(By by) throws Exception{
		WebElement element=this.findElement(by);
		Actions builder = new Actions(driver); 
		builder.moveToElement(element).perform();
	}

	public void moveToLocatorByJS(By by) throws Exception{
		logger.info("Scrolling to element :: "+by.toString());
		WebElement element=this.findElement(by);
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].scrollIntoView(true);",element);
	}

	public boolean retryingFindClick(By by) {
		boolean result = false;
		int attempts = 0;
		while(attempts < 3) {
			try {
				logger.info("Trying to click element....Attempt no :: "+attempts);
				driver.findElement(by).click();
				result = true;
				logger.info("Successfully clicked element in :: "+attempts+" attempts");
				break;
			} catch(StaleElementReferenceException  e) {

			}
			attempts++;
		}
		return result;
	}

	public void waitForElementoBeStale(WebElement webElement, int time) {
		logger.info("Waiting for element to be clickable :: "+webElement,true);
		long startTime = System.currentTimeMillis();
		wait = new WebDriverWait(driver, time,POLLING_TIME);
		wait.until(ExpectedConditions.stalenessOf(webElement));
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		logger.info("Finished waiting for locator to be clickable after " + totalTime + " milliseconds.");
	} 

	public void sendESCKey() {
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ESCAPE).build().perform();
	}

	public void scrollDownByJS(int distance) {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollBy(0, "+distance+")");
	}

	public void scrollUpByJS(int distance) {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		//System.out.println("window.scrollBy(0, -"+distance+")");
		jse.executeScript("window.scrollBy(0, -"+distance+")");
	}

	public void scrollToEnd() {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("window.scrollTo(0,document.body.scrollHeight);");
	}
	// Clicking some Element untill required elements are not visible on screen 
	public void waitForElementToBePresent(By loadLocator,By expectedLocator,int time) {
		try {
			do{
				driver.findElement(loadLocator).click();
				pauseExecutionFor(1000);
				waitExplicitlyForLocatorAbsence(By.xpath("//div[text()='Loading...']"),200);
				if(driver.findElements(expectedLocator).size()>0)
					break;
				else
					Thread.sleep(time);
			}while(driver.findElements(expectedLocator).size()==0);
		}
		catch(Exception e) {
			logger.info(e.getMessage());
		}
	}

	public void sendKeysByAction(By locator, String text) {
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(locator));
		actions.click();
		actions.sendKeys(text);
		actions.build().perform();
	}

	public ExpectedCondition<Boolean> absenceOfElementLocated(
			final By locator) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {
					driver.findElement(locator);
					return false;
				} catch (NoSuchElementException e) {
					return true;
				} catch (StaleElementReferenceException e) {
					return true;
				}
			}

			@Override
			public String toString() {
				return "element to not being present: " + locator;
			}
		};
	}

	public void sendDOWN_ENTERKey() throws InterruptedException {
		Actions action = new Actions(driver);
		action.sendKeys(Keys.DOWN).build().perform();
		Thread.sleep(2000);
		action.sendKeys(Keys.RETURN).build().perform();
	}

	public boolean waitExplicitlyForLocatorAbsence(By locator, int time) throws InterruptedException {
		boolean flag = false;
		try {
			for(int count=1; count<=time; count++) {
				logger.info("Checking for locator ::"+locator.toString()+"...Attempt number :: "+count);
				pauseExecutionFor(1000);
				if(!IsLocatorVisible(locator)) {
					flag = true;
					if(count == 4 || flag)
						logger.info("Succesfully deleted locator :: "+locator.toString()+" within "+count+" attempts");
					break;
				}
			}
		} finally {
		}
		return flag;
	}

	public void scrollTillElement(String path) {
		JavascriptExecutor js = (JavascriptExecutor)driver;
		WebElement Element = driver.findElement(By.xpath(path));
		//This will scroll the page till the element is found		
		js.executeScript("arguments[0].scrollIntoView();", Element);
	}

	public void sendDOWNKey() throws InterruptedException {
		Actions action = new Actions(driver);
		action.sendKeys(Keys.DOWN).build().perform();
		Thread.sleep(2000);
	}

	public void maximizeScreen() throws InterruptedException {
		driver.manage().window().maximize();
		Thread.sleep(2000);
	}

	public String getPageSource(){
		return driver.getPageSource();
	}

	public int getRandomNumberBetween(int x, int y){
		int rand = ThreadLocalRandom.current().nextInt(x,y);
		return rand;
	}

	public void pauseForRandomDurationBetween(int x, int y) throws InterruptedException {
		Thread.sleep(getRandomNumberBetween(x,y)*1000);
	}
	public void pauseForRandomDurationBetween() throws InterruptedException {
		Thread.sleep(getRandomNumberBetween(2,10)*1000);
	}
}
