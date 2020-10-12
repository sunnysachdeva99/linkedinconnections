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
       // profilePage.sendM();
        int maxLimit=20;
        data=dbUtil.getRawData(maxLimit);
        String keyword = excelReader.getKeywords();
        String [] keywords = keyword.split("/");
        List<String> exclusionList=excelReader.getExclusionList();

        profilePage.sendMessageToValidProfiles(data,keywords,dbUtil,exclusionList);







    }
}
