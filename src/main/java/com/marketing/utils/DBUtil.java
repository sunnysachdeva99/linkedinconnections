package com.marketing.utils;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.google.common.util.concurrent.Uninterruptibles;
import com.marketing.beans.ExcelData;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteOpenMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DBUtil {
    private static Connection conn;
    private static final String JDBC_DRIVER ="org.sqlite.JDBC";
    private static final String DB_URL="jdbc:sqlite:src/main/resources/MyConnections.db";

    public static Connection createConnection(){
        try {
            if(conn ==null){
                DbUtils.loadDriver(JDBC_DRIVER);
                conn =  java.sql.DriverManager.getConnection(DB_URL);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public void writeRawData(int id, String url) throws SQLException {
        //Connection conn = null;
        QueryRunner queryRunner;
        try {
            if(this.conn ==null){
                this.conn =createConnection();
            }
            queryRunner = new QueryRunner();
           // DbUtils.loadDriver(JDBC_DRIVER);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            //conn = DriverManager.getConnection(DB_URL);
            int insertedRecords = queryRunner.update(this.conn,
                    "INSERT INTO raw(id,url,date) VALUES (?,?,?)", id,url,localDate);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.close(this.conn);

        }

    }

    public void writeRawData(Set<String> hSet) throws SQLException {
        //Connection conn = null;
        AtomicInteger count=new AtomicInteger(0);
        QueryRunner queryRunner;
        try {
            createConnection();
            queryRunner = new QueryRunner();
            // DbUtils.loadDriver(JDBC_DRIVER);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            //conn = DriverManager.getConnection(DB_URL);
            for(String url: hSet){
                int insertedRecords = queryRunner.update(this.conn,
                        "INSERT INTO raw(url,date) VALUES (?,?)", url,localDate);
                Uninterruptibles.sleepUninterruptibly(100, TimeUnit.MILLISECONDS);
                System.out.println(count.incrementAndGet()+ "  "+url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.close(this.conn);
            System.out.println(conn.isClosed());
        }

    }

    public List<ExcelData> getRawData() throws SQLException {
        Connection conn = null;
        QueryRunner queryRunner;
        List<ExcelData> results=null;
        try {
            createConnection();
            queryRunner = new QueryRunner();
            DbUtils.loadDriver(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL);
            String sql ="SELECT ROWID,url FROM Raw";
            ResultSetHandler<List<ExcelData>> resultHandler = new BeanListHandler<ExcelData>(ExcelData.class);
            results = queryRunner.query(conn, sql, resultHandler);
            if(results == null){
                return  Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.close(conn);

        }
        return  results;
    }


    public void writeFinalData(String name,String url,String matchLevel,String processed) throws FilloException, SQLException {
        Connection conn = null;
        QueryRunner queryRunner;
        try {
            queryRunner = new QueryRunner();
            DbUtils.loadDriver(JDBC_DRIVER);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            LocalDate localDate = LocalDate.now();
            conn = DriverManager.getConnection(DB_URL);
            int insertedRecords = queryRunner.update(conn,
                    "INSERT INTO final(name,url,matchLevel,processed,date) VALUES (?,?,?,?,?)", name,url,matchLevel,processed,localDate);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.close(conn);

        }
    }




}
