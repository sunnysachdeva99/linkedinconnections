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
                chromeOptions.addArguments("window-size=1920,1080");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.addArguments("--verbose");
                chromeOptions.addArguments("--whitelisted-ips=''");
                chromeOptions.addArguments("--remote-debugging-port=9222");
                chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);

            }
            driver = new ChromeDriver(chromeOptions);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(40, TimeUnit.SECONDS);
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
