package com.marketing.test;

import com.codoid.products.exception.FilloException;
import com.marketing.beans.ExcelData;
import com.marketing.pages.LoginPage;
import org.testng.annotations.Test;

import java.util.List;

public class UrlProcesserTest extends BaseTest{



    @Test
    public void sendMessage() throws Exception {
        List<ExcelData> data=excelReader.getProcessedRecords();
        String keyword = excelReader.getKeywords();
        String [] keywords = keyword.split("/");

        profilePage.sendMessageToValidProfiles(data,keywords,excelWriter);



    }
}
