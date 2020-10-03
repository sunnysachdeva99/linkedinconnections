package com.marketing.managers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class RemoteDriverFactory {

    public static WebDriver createInstance(String browserName) throws MalformedURLException {
        RemoteWebDriver driver = null;
        URL url;
        DesiredCapabilities dcap;
        if (browserName.toLowerCase().contains("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();

        } else if (browserName.toLowerCase().contains("internet")) {
            WebDriverManager.iedriver().setup();
            driver = new InternetExplorerDriver();

        } else if (browserName.toLowerCase().contains("chrome")) {
            dcap = DesiredCapabilities.chrome();
            dcap.setCapability("enableVNC", true);
            dcap.setBrowserName("chrome");
            dcap.setVersion("80.0");
            url = new URL("http://localhost:4444/wd/hub");
            driver = new RemoteWebDriver(url, dcap);
            driver.setFileDetector(new LocalFileDetector());
        }

        return driver;
    }
}
