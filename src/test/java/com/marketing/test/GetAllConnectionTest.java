package com.marketing.test;

import com.codoid.products.exception.FilloException;
import org.testng.annotations.Test;
import sun.lwawt.macosx.CSystemTray;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class GetAllConnectionTest extends BaseTest{

    @Test
    public void getUrls() throws Exception {
        loginPage.openConnections();
        Set<String> hrefs=loginPage.getAllHref();
        System.out.println(hrefs.size());
        AtomicInteger count=new AtomicInteger(0);

        if(outputMode.equalsIgnoreCase("EXCEL")){
            hrefs.stream()
                .forEach(href-> {
                    try {
                        //excelWriter.writeRawData(count.incrementAndGet(),href);
                        dbUtil.writeRawData(count.incrementAndGet(), href);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        }else if (outputMode.equalsIgnoreCase("DB")){
            dbUtil.writeRawData(hrefs);
        }else {
            throw new Exception("Please mention outputIn in Input.xlsx (login sheet)");
        }

    }
}
