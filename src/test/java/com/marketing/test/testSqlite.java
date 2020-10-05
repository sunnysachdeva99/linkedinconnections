package com.marketing.test;

import com.marketing.managers.DriverManager;
import com.marketing.utils.DBUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class testSqlite {

    @Test
    public void testConnection() throws ClassNotFoundException, SQLException {
        DBUtil dbUtil = new DBUtil();
        Set<String> s = new HashSet<>();
        s.add("https://web.whatsapp.com/");
        s.add("https://web.whatsapp2.com/");
        s.add("https://web.whatsapp3.com/");
        dbUtil.writeRawData(s);
    }
}
