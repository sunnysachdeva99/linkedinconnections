package com.marketing.test;

import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

public class InviteConnectionTest extends BaseTest{

    @Test
    public void inviteConnections() throws Exception {
        loginPage.openConnections();
        Set<String> set = loginPage.getSecondLevelConnections(1);


    }
}
