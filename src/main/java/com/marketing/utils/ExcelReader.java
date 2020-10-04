package com.marketing.utils;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.marketing.beans.ExcelData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ExcelReader {

    private String wrkBook;

    public void createConnection(String wrkBook){
        this.wrkBook=wrkBook;
    }

    public ExcelReader (String wrkBook){
        this.wrkBook=wrkBook;
    }

    public HashMap<String,String> getCredentials() throws FilloException {
        Fillo fillo = new Fillo();
        Connection connection = fillo.getConnection(this.wrkBook);
        String query ="select * from login where id=1";
        Recordset recordset=connection.executeQuery(query);
        HashMap<String,String> map = new HashMap<>();
        while(recordset.next()){
            //System.out.println(recordset.getField("Login")+ " -- "+recordset.getField("Password"));
            map.put("login",recordset.getField("username"));
            map.put("password",recordset.getField("password"));
        }

        recordset.close();
        connection.close();
        return map;
    }

    public List<ExcelData> getProcessedRecords() throws FilloException {
        ExcelData data=null;
        Fillo fillo = new Fillo();
        Connection connection = fillo.getConnection(this.wrkBook);
        String query ="select * from raw";
        Recordset recordset = null;
        List<ExcelData> lst=null;
        try {
            recordset=connection.executeQuery(query);
            lst = new ArrayList<>();
            while (recordset.next()) {
                data=new ExcelData();
                data.setId(recordset.getField("id"));
                data.setUrl(recordset.getField("url"));
                lst.add(data);
            }
        }catch (Exception e){
            recordset.close();
            connection.close();
        }
        if(lst == null){
           return  Collections.emptyList();
        }
        return lst;
    }

    public String getKeywords() throws FilloException {
        Fillo fillo = new Fillo();
        String keyword="";
        Connection connection = fillo.getConnection(this.wrkBook);
        String query ="select relatedKeyword from keywords where id=1";
        Recordset recordset=connection.executeQuery(query);
        HashMap<String,String> map = new HashMap<>();
        while(recordset.next()){
            keyword=recordset.getField("relatedKeyword");
        }

        recordset.close();
        connection.close();
        return keyword;
    }


    public List<String> getExclusionList() throws FilloException {
        Fillo fillo = new Fillo();
        String keyword="";
        List<String> exclusionList = new ArrayList<>();
        Connection connection = fillo.getConnection(this.wrkBook);
        String query ="select url from exclusions";
        Recordset recordset=connection.executeQuery(query);
        HashMap<String,String> map = new HashMap<>();
        while(recordset.next()){
            exclusionList.add(recordset.getField("url"));
        }

        recordset.close();
        connection.close();
        return exclusionList;
    }


}
