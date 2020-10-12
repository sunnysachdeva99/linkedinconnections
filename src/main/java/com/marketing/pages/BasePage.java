package com.marketing.pages;


import com.google.common.util.concurrent.Uninterruptibles;
import com.marketing.utils.DBUtil;
import com.marketing.utils.LinkedInDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.util.concurrent.TimeUnit;

public class BasePage {

    protected LinkedInDriver driver;
    Logger logger= LogManager.getLogger(BasePage.class);
    public BasePage(LinkedInDriver driver){
        this.driver=driver;
    }
    protected By btn_MessageDown = By.xpath(("//li-icon[@type='chevron-down-icon' and @class='artdeco-button__icon']"));
    protected By sessionClear=By.xpath("//button[contains(text(),'sessions')]");
    protected String sessionClearUrl="https://www.linkedin.com/psettings/sessions";
    private By txt_Password_Reset=By.id("verify-password");
    private By lnk_End_All_Sessions=By.xpath("//button[contains(text(),'End all sessions')]");
    private  By lnkMe=By.xpath("//img[@width=24]");
    private By lnk_SignOut=By.linkText("Sign Out");

    public ProfilePage navigateToProfilePage(String url){
        logger.info("navigating to :: "+url);
        driver.navigateTo(url);
        driver.waitForPageLoad(30);
        return new ProfilePage(driver);
    }



    public void logout() throws Exception {
        driver.clickLocator(lnkMe);
        Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
        driver.clickLocatorByJS(lnk_SignOut);
//        driver.navigateTo(sessionClearUrl);
//        driver.clickLocator(sessionClear);
//        driver.pauseExecutionFor(2000);
//        //driver.sendKeys(txt_Password_Reset,password);
//        driver.sendKeysLocatorByJS(txt_Password_Reset,password);
//        driver.clickLocator(lnk_End_All_Sessions);
//        driver.pauseForRandomDurationBetween(1,3);
    }





}
