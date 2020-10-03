package com.marketing.pages;


import com.google.common.util.concurrent.Uninterruptibles;
import com.marketing.utils.LinkedInDriver;
import org.apache.commons.collections4.CollectionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LoginPage extends BasePage{

    public LoginPage(LinkedInDriver driver) {
        super(driver);
    }


    private By txt_Search= By.xpath("//input[@placeholder='Search']");
    private By txt_Login = By.id("session_key");
    private By txt_Password = By.id("session_password");
    private By btn_Login = By.xpath("//button[contains(text(),'Sign')]");
    private By lbl_TotalConnections=By.xpath("//h1[@class='t-18 t-black t-normal']");
        private By lst_AllConnections=By.xpath("//li/a[@data-control-name='connection_profile']");

    public void loginAs(String userName,String password) throws Exception {
        this.driver.sendKeys(txt_Login,userName);
        driver.sendKeys(txt_Password,password);
        driver.clickLocator(btn_Login);
       // return new HomePage(driver);
    }

    public LoginPage openConnections(){
        driver.navigateTo("https://www.linkedin.com/mynetwork/invite-connect/connections/");
        return this;
    }

    private int getTotalConnections(){
        String text =driver.getText(lbl_TotalConnections);
        System.out.println(text);
        int totalCon=Integer.parseInt(text.split("\\s+")[0].replace(",",""));
        return totalCon;
    }

    public void sentMessage(List<String> existingList){
        List<WebElement> list_All = null;
        int counter =0;
        while(true){
            list_All = driver.findElements(lst_AllConnections);
            System.out.println("find all got "+list_All.size());

            List<String> newList=list_All.stream()
                    .map(element -> driver.getAttribute(element,"href"))
                    .collect(Collectors.toList());
            boolean IsEqual=CollectionUtils.isEqualCollection(existingList,newList);



            List<String> toProcess= new ArrayList<>(CollectionUtils.removeAll(newList,existingList)) ;

            if(!toProcess.isEmpty()){

            }else{
                driver.scrollToEnd();
                driver.waitForPageLoad(10);
                Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
                driver.scrollUpByJS(100);
            }

        }

    }



    public List<String> getAllHref() throws InterruptedException {

        List<String> lstHref= null;
        List<WebElement> list_All = null;
        try {
            int totalConnections =5;
                    //getTotalConnections();
            System.out.println("Total Connections :: "+totalConnections);
            lstHref = new ArrayList<>();
            list_All = null;
            if(totalConnections>1){
                 list_All = driver.findElements(lst_AllConnections);
                while(true){
                    driver.scrollToEnd();
                    //driver.scrollUpByJS(500);
                    driver.waitForPageLoad(30);
                    Uninterruptibles.sleepUninterruptibly(7, TimeUnit.SECONDS);
                    list_All = driver.findElements(lst_AllConnections);
                    System.out.println("found "+list_All.size()+ " total connectionls :: "+totalConnections);
                    driver.scrollUpByJS(100);
                    if(list_All.size() >= totalConnections){
                        System.out.println("Total count matched !!!!");
                        break;
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            for(WebElement element: list_All){
                String href= driver.getAttribute(element,"href");
                lstHref.add(href);
            }
        }

        return lstHref;
    }


}
