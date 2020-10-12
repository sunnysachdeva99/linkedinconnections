package com.marketing.pages;


import com.google.common.util.concurrent.Uninterruptibles;
import com.marketing.utils.DBUtil;
import com.marketing.utils.LinkedInDriver;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LoginPage extends BasePage{

    public LoginPage(LinkedInDriver driver) {
        super(driver);
    }
    Logger logger= LogManager.getLogger(LoginPage.class);

    private By txt_Search= By.xpath("//input[@placeholder='Search']");
    private By txt_Login = By.id("session_key");
    private By txt_Password = By.id("session_password");
    private By btn_Login = By.xpath("//button[contains(text(),'Sign')]");
    private By lbl_TotalConnections=By.xpath("//h1[@class='t-18 t-black t-normal']");
    private By lst_AllConnections=By.xpath("//li/a[@data-control-name='connection_profile']");
    private By lnk_SearchWithfilters=By.linkText("Search with filters");
    private By btnClear=By.xpath("//button[contains(@aria-label,'Clear')]");
    private By lst_All_First_Connections=By.xpath("//a[@data-control-name='search_srp_result']");
   // private By lst_All_New_Connect=By.xpath("//button[text()='Connect']/parent::div/parent::div[@class='search-result__actions']/preceding-sibling::div[contains(@class,'image')]//a[@data-control-name='search_srp_result']");
    private By btnNext=By.xpath("//button[@aria-label='Next']");

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
        logger.info("Total Connections :: "+text);
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



    public Set<String> getAllHref() throws InterruptedException {
        List<String> lstHref= null;
        List<WebElement> list_All = null;
        Set<String> hSet= new HashSet<String>();
        try {
            int totalConnections =getTotalConnections();
            System.out.println("Total Connections :: "+totalConnections);
            lstHref = new ArrayList<>();
            list_All = null;
            if(totalConnections>1){
                 list_All = driver.findElements(lst_AllConnections);
                while(true){
                    driver.scrollToEnd();
                    driver.waitForPageLoad(30);
                    Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
                    list_All = driver.findElements(lst_AllConnections);
                    hSet=list_All.stream()
                            .map(e-> driver.getAttribute(e,"href"))
                            .collect(Collectors.toSet());
                    System.out.println("found "+list_All.size()+ " total connection is :: "+totalConnections);
                    driver.scrollUpByJS(100);
                    if(totalConnections - list_All.size() <=2){
                        System.out.println("Total count matched !!!!");
                        break;
                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return hSet;
    }


    public Set<String> getSecondLevelConnections(int page) throws Exception {
        Set<String> sHrefs= null;
        List<WebElement> list_All = null;
        logger.info("Clicking Search with filter link");
        driver.clickLocator(lnk_SearchWithfilters);
        applyNewConnectionFilter();
        String url = driver.getCurrentUrl();
        logger.info("Current url "+url);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        url =url+"&page=";
        int counter=1;
        sHrefs=new HashSet<>();
        list_All=driver.findElements(lst_All_First_Connections);

        if(list_All.size() > page){

        }else{

        }

        while(true & counter <=page){
            driver.navigateTo(url+counter);
            driver.waitForPageLoad(30);
            driver.waitForLocatorToBeVisible(btnClear);
            if(IsNextButtonEnabled()){
                for(int index=0;index<=2;index++){
                    driver.scrollDownByJS(500);
                    list_All=driver.findElements(lst_All_First_Connections);
                    sHrefs.addAll(list_All.stream()
                            .map(e -> driver.getAttribute(e,"href"))
                            .peek(System.out::println)
                            .collect(Collectors.toSet()));
                    counter++;
                    System.out.println("Found "+sHrefs.size());
                }

            }

        }
        return sHrefs;
    }

    private void applyFirstLevelFilter(){
        driver.navigateTo("https://www.linkedin.com/search/results/people/?facetNetwork=%5B%22F%22%5D&origin=FACETED_SEARCH");
        driver.waitForPageLoad(20);
        driver.waitForLocatorToBeVisible(btnClear);
    }


    private boolean IsNextButtonEnabled(){
        boolean isEnabled=false;
        try{
            driver.scrollDownByJS(1000);
            driver.waitForLocatorToBeVisible(btnClear);
            Uninterruptibles.sleepUninterruptibly(1,TimeUnit.SECONDS);
            String attribute = driver.getAttribute(btnNext,"disabled");
            if(attribute.isEmpty()){
                isEnabled=true;
            }
            driver.scrollUpByJS(1000);
        }catch (Exception e){
            isEnabled=true;
        }

        return isEnabled;
    }


    private void applyNewConnectionFilter(){
        driver.navigateTo("https://www.linkedin.com/search/results/people/?facetIndustry=%5B%2296%22%2C%224%22%5D&facetNetwork=%5B%22S%22%5D&origin=FACETED_SEARCH");
        driver.waitForPageLoad(20);
    }


    public Set<String> getAllHref(int maxLimit) throws InterruptedException {
        List<String> lstHref= null;
        List<WebElement> list_All = null;
        Set<String> hSet= new HashSet<String>();
        try {
            int totalConnections =getTotalConnections();
            System.out.println("Total Connections :: "+totalConnections);
            lstHref = new ArrayList<>();
            list_All = null;
            if(totalConnections>1){
                list_All = driver.findElements(lst_AllConnections);

                if(list_All.size() >=maxLimit){
                    hSet=list_All.stream()
                            .map(e-> driver.getAttribute(e,"href"))
                            .collect(Collectors.toSet());
                }else{
                    int counter=0;
                    while(true){
                        driver.scrollToEnd();
                        driver.waitForPageLoad(30);
                        Uninterruptibles.sleepUninterruptibly(5, TimeUnit.SECONDS);
                        list_All = driver.findElements(lst_AllConnections);
                        if(list_All.size() > maxLimit){
                            hSet=list_All.stream()
                                    .map(e-> driver.getAttribute(e,"href"))
                                    .collect(Collectors.toSet());
                            break;
                        }else{
//                            hSet=list_All.stream()
//                                    .map(e-> driver.getAttribute(e,"href"))
//                                    .collect(Collectors.toSet());
//                            System.out.println("found "+list_All.size()+ " total connection is :: "+totalConnections);
                            driver.scrollUpByJS(150);
                        }

//                        if(list_All.size() - maxLimit <=2){
//                            System.out.println("Total count matched !!!!");
//                            break;
//                        }

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return hSet;
    }

}
