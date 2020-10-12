package com.marketing.pages;

import com.google.common.util.concurrent.Uninterruptibles;
import com.marketing.beans.ExcelData;
import com.marketing.utils.DBUtil;
import com.marketing.utils.ExcelWriter;
import com.marketing.utils.LinkedInDriver;
import com.marketing.utils.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import sun.tools.java.Environment;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ProfilePage extends BasePage{
    Logger logger= LogManager.getLogger(ProfilePage.class);
    public ProfilePage(LinkedInDriver driver) {
        super(driver);
    }
    private By keyword_Under_HeroImage=By.xpath("//h2[contains(@class,'break-words')]");
    private String lbl_About="//header[h2[text()='About']]";
    private By lbl_Exp=By.xpath("//h2[text()='Experience']");
    private By lnk_Message=By.linkText("Message");
    private By btn_Send=By.xpath("//button[text()='Send']");
    private By lbl_FullName=By.xpath("//li[@class='inline t-24 t-black t-normal break-words']");
    private By txt_Message=By.xpath("//div[@class='msg-form__msg-content-container--scrollable scrollable']//p");
    private By lst_Skills=By.xpath("//span[@Class='pv-skill-category-entity__name-text t-16 t-black t-bold']");
    private By lbl_SKills=By.xpath("//h2[text()='Skills & Endorsements']");
    private By lnk_Skills_ShowMore=By.xpath("//h2[text()='Skills & Endorsements']/parent::div[@class='display-flex']/parent::div/following-sibling::div[@class='ember-view']//span[text()='Show more']");
    private By btn_Close_Msg = By.xpath("//button[contains(@data-control-name,'close')]");

    private String getMatchLevel(String[] variations) throws Exception {
        boolean IsMatched=false;
        WebElement element;
        String expUnderLatestCompany="";
        String expUnderAbout="";
        String expUnderHeroImage="";
        String matchLevel="";
        String expTextUnderLatestOrg="";
        _scrollTillExperience();

        //Match Levels :: Hero
        expUnderHeroImage=driver.getText(keyword_Under_HeroImage).toLowerCase();
        if(matchFound(expUnderHeroImage,variations)){
            matchLevel="Hero Image |";
        }else if(driver.IsLocatorVisible(By.xpath(lbl_About) )){
                _scrollTillAbout();
                String xpath=lbl_About+"/following-sibling::p//a[text()='see more']";
                Uninterruptibles.sleepUninterruptibly(1,TimeUnit.SECONDS);
                //can be improved
                driver.clickIfPresent(By.xpath(xpath));
                expUnderAbout=driver.getText(By.xpath(lbl_About+"/following-sibling::p")).toLowerCase();

                if(matchFound(expUnderAbout,variations)){
                    matchLevel=matchLevel+"About | ";
                }

            }
        else if(!expTextUnderLatestOrg.equalsIgnoreCase("")&& matchFound(expTextUnderLatestOrg,variations)){
            matchLevel=matchLevel+"Latest Exp | ";
        }
        else {
            if(_scrollTillSkills()){
                driver.clickLocator(lnk_Skills_ShowMore);
                List<WebElement> lstSkills=driver.findElements(lst_Skills);
                List<String> skills=lstSkills.stream()
                        .map(WebElement::getText)
                        .collect(Collectors.toList());
                String [] arrSkills =skills.toArray(new String[skills.size()]);

                outer:
                for(String skill:skills){
                    for(String keyword : variations){
                        System.out.println(skill+" === "+keyword);
                        if(skill.toLowerCase().contains(keyword.toLowerCase())){
                            matchLevel=matchLevel+" Skills |";
                            break outer;
                        }

                    }
                }
            }

        }

//        if(matchLevel.equalsIgnoreCase("")){
//            matchLevel="Weak match !!!";
//        }

        if(!matchLevel.isEmpty()){
            IsMatched=true;
        }
        return matchLevel;
    }

    public void sendMessageToValidProfiles(List<ExcelData> datas, String[] keywords, ExcelWriter writer,List<String> exclusionList) throws Exception {
        String matchLevel;
        String processed="Done";
        String firstName="";
        for(ExcelData data:datas){
            if(!exclusionList.contains(data.getUrl())){
                navigateToProfilePage(data.getUrl());
                matchLevel=getMatchLevel(keywords);
                if(!matchLevel.isEmpty()){
                    try {
                        firstName = getFullName()[0];
                        sendMessage(firstName);
                    } catch (Exception e) {
                        processed="Something went wrong";
                        e.printStackTrace();
                    }finally {
                        writer.writeFinalData(data.getId(),firstName,data.getUrl(),matchLevel,processed);
                    }
                }
            }

        }

    }



    public void sendMessageToValidProfiles(List<ExcelData> datas, String[] keywords, DBUtil dbUtil, List<String> exclusionList) throws Exception {
        String matchLevel;
        String processed="Done";
        String firstName="";
        for(ExcelData data:datas){
            if(!exclusionList.contains(data.getUrl())){
                navigateToProfilePage(data.getUrl());
                matchLevel=getMatchLevel(keywords);
                if(!matchLevel.isEmpty()){
                    try {
                        firstName = getFullName()[0];
                        sendMessage(firstName);
                    } catch (Exception e) {
                        processed="Something went wrong";
                        e.printStackTrace();
                    }finally {
                        dbUtil.writeFinalData(data.getId(),firstName,matchLevel,processed);
                    }
                }
            }

        }

    }


    public void sendMessageToValidProfiles(List<ExcelData> datas, String[] keywords, DBUtil dbUtil, List<String> exclusionList, int maxLimit) throws Exception {
        String matchLevel;
        String processed="Done";
        String firstName="";
        int counter=1;
        for(ExcelData data:datas){
            if(!exclusionList.contains(data.getUrl()) && counter<=maxLimit){
                navigateToProfilePage(data.getUrl());
                matchLevel=getMatchLevel(keywords);
                if(!matchLevel.isEmpty()){
                    try {
                        firstName = getFullName()[0];
                        sendMessage(firstName);
                    } catch (Exception e) {
                        processed="Something went wrong";
                        e.printStackTrace();
                    }finally {
                        dbUtil.writeFinalData(data.getId(),firstName,matchLevel,processed);
                    }
                }
            }
            counter++;
        }

    }

    private void _scrollTillExperience(){
        int counter =0;
        driver.scrollDownByJS(500);
        while(true){
            counter++;
            if(driver.IsLocatorPresent(lbl_Exp)){
                break;
            }else{
                driver.scrollDownByJS(500);
            }
            if(counter==3){
                break;
            }
        }
    }

    private void _scrollTillAbout(){
        int counter =0;
        driver.scrollDownByJS(300);
        while(true){
            counter++;
            if(driver.IsLocatorPresent(By.xpath(lbl_About))){
                break;
            }else{
                driver.scrollDownByJS(500);
            }
            if(counter==3){
                break;
            }
        }
    }

    private boolean _scrollTillSkills(){
        boolean IsPresent=false;
        int counter =1;
        driver.scrollDownByJS(500);
        while(true){
            counter++;
            if(driver.IsLocatorPresent(lbl_SKills)){
                IsPresent=true;
                break;
            }else{
                driver.scrollDownByJS(500);
            }
            if(counter==2){
                break;
            }
        }
        return  IsPresent;
    }

    private boolean matchFound(String text, String[] list){
        boolean IsFound =false;
        text=text.toLowerCase();
        for(String s:list){
            s=s.toLowerCase();
            if(text.contains(s)){
                IsFound=true;
                break;
            }
        }

        return IsFound;
    }




    private void sendMessage(String name) throws Exception {
        driver.moveToLocator(lnk_Message);
        Uninterruptibles.sleepUninterruptibly(2,TimeUnit.SECONDS);
        driver.waitForLocatorToBeClickable(lnk_Message);
        driver.clickLocator(lnk_Message);
        Uninterruptibles.sleepUninterruptibly(3,TimeUnit.SECONDS);
        //System.out.println(driver.getAttribute(btn_Send,"disabled"));
        String msg = Message.msg;
        msg=String.format(msg,name);
        System.out.println(msg);
       // driver.clickLocatorByJS(txt_Message);
        Uninterruptibles.sleepUninterruptibly(1,TimeUnit.SECONDS);
        //driver.sendKeysLocatorByJS(txt_Message,msg);
        driver.sendKeys(txt_Message,msg);
        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
        if(IsSendButtonEnabled()){
            System.out.println("Send button enabled");
            driver.clickLocatorByJS(btn_Send);
        }
        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
        driver.clickLocatorByJS(btn_Close_Msg);
        System.out.println("Done");
    }

    private String[] getFullName(){
        String fullName=driver.getText(lbl_FullName);
        fullName=fullName.replace(",","");
        fullName=fullName.replace("'","''");
        String[] names=fullName.split("\\s+");
        return names;
    }

    private boolean IsSendButtonEnabled() {
        boolean IsEnabled=false;
        int counter=1;
        while (counter <=10){
            try{
               String status= driver.getAttribute(btn_Send,"disabled");
                System.out.println("Send Button disabled attribute : "+status);
               if(status.isEmpty()){
                   IsEnabled=true;
                   break;
               }else{
                   Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
               }
            }catch (Exception e){
                IsEnabled=true;

            }finally {
                counter++;
            }
        }
        return IsEnabled;
    }

}
