package com.marketing.test;

import com.marketing.managers.DriverManager;
import com.marketing.managers.LocalDriverFactory;
import com.marketing.pages.LoginPage;
import com.marketing.pages.ProfilePage;
import com.marketing.utils.DBUtil;
import com.marketing.utils.ExcelReader;
import com.marketing.utils.ExcelWriter;
import com.marketing.utils.LinkedInDriver;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.List;

public class BaseTest {

    protected WebDriver webDriver;
    protected LinkedInDriver driver;
    protected ExcelReader excelReader;
    protected ExcelWriter excelWriter;
    protected DBUtil dbUtil;
    private String inputXlPath=System.getProperty("user.dir")+"/src/main/resources/Input.xlsx";
    protected LoginPage loginPage;
    protected ProfilePage profilePage;
    protected String outputMode="";
    @BeforeSuite
    public void setUp() throws Exception {
        webDriver= LocalDriverFactory.createInstance("chrome",false,30);
        DriverManager.setWebDriver(webDriver);
        driver= new LinkedInDriver(DriverManager.getDriver());
        driver.navigateTo("https://www.linkedin.com");

        excelReader = new ExcelReader(inputXlPath);
        dbUtil   = new DBUtil();
        HashMap<String,String> map= excelReader.getCredentials();
        outputMode=map.get("outputMode");
        loginPage= new LoginPage(driver);
        loginPage.loginAs(map.get("login"),map.get("password"));
        profilePage= new ProfilePage(driver);
        excelWriter= new ExcelWriter(inputXlPath);
    }

    @AfterSuite
    public void tearDown() throws Exception {
        loginPage.logout();
        driver.quitBrowser();
    }

}
