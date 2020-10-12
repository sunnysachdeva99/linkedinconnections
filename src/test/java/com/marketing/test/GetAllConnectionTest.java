package com.marketing.test;

import com.codoid.products.exception.FilloException;
import com.marketing.utils.DBUtil;
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
        Set<String> hrefs=loginPage.getAllHref(40);
        dbUtil.writeRawData(hrefs);


    }
}
