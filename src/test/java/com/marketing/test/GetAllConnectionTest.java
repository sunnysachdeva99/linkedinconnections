package com.marketing.test;

import com.codoid.products.exception.FilloException;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GetAllConnectionTest extends BaseTest{

    @Test
    public void getUrls() throws InterruptedException, FilloException {
        loginPage.openConnections();
        List<String> hrefs=loginPage.getAllHref();
        System.out.println(hrefs.size());
        AtomicInteger count=new AtomicInteger(0);

        hrefs.stream()
                .forEach(href-> {
                    try {
                        excelWriter.writeRawData(count.incrementAndGet(),href);
                    } catch (FilloException e) {
                        e.printStackTrace();
                    }
                });
//        List<String> lst=excelReader.getProcessedRecords();
//        loginPage.sentMessage(lst);
//        System.out.println(lst.size());
        //System.out.println(href.size());
    }
}
