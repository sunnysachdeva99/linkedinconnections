package com.marketing.managers;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.concurrent.TimeUnit;


public class LocalDriverFactory {

    public static WebDriver createInstance(String browserName,boolean IsHeadless ,int implicitWait) {
        WebDriver driver = null;

        if(browserName.equalsIgnoreCase("chrome")){
            WebDriverManager.chromedriver().setup();
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
            if(IsHeadless) {
                chromeOptions.addArguments("headless");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("enable-automation");
                chromeOptions.addArguments("window-size=1920,1080");
                chromeOptions.addArguments("--disable-infobars");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--verbose");
                chromeOptions.addArguments("--disable-browser-side-navigation"); //https://stackoverflow.com/a/49123152/1689770
                //chromeOptions.addArguments("--whitelisted-ips=''");
                //chromeOptions.addArguments("--remote-debugging-port=9222");
                chromeOptions.addArguments("--disable-gpu");
                chromeOptions.addArguments("enable-features=NetworkServiceInProcess");

                chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

            }
            driver = new ChromeDriver(chromeOptions);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(90, TimeUnit.SECONDS);
        }else if(browserName.equalsIgnoreCase("firefox")){
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
            FirefoxOptions fo = new FirefoxOptions();
            fo.setPageLoadStrategy(PageLoadStrategy.EAGER);
        } else if(browserName.equalsIgnoreCase("edge")){
            WebDriverManager.edgedriver().setup();
            driver= new EdgeDriver();
        }


        return driver;
    }
}
