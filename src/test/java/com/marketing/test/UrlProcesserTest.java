package com.marketing.test;

import com.codoid.products.exception.FilloException;
import com.marketing.beans.ExcelData;
import com.marketing.pages.LoginPage;
import org.testng.annotations.Test;

import java.util.List;

public class UrlProcesserTest extends BaseTest{

    private List<ExcelData> data;

    @Test
    public void sendMessage() throws Exception {
        if(outputMode.equalsIgnoreCase("EXCEL")){
            data=excelReader.getProcessedRecords();
        }else if(outputMode.equalsIgnoreCase("DB")){
            data=dbUtil.getRawData();
        }
        String keyword = excelReader.getKeywords();
        String [] keywords = keyword.split("/");
        List<String> exclusionList=excelReader.getExclusionList();
        if(outputMode.equalsIgnoreCase("EXCEL")){
            profilePage.sendMessageToValidProfiles(data,keywords,excelWriter,exclusionList);
        }else if(outputMode.equalsIgnoreCase("DB")){
            profilePage.sendMessageToValidProfiles(data,keywords,dbUtil,exclusionList);
        }






    }
}
