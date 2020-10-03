package com.marketing.utils;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class ExcelWriter {

    private String wrkBook;

    public ExcelWriter(String wrkBook){
        this.wrkBook=wrkBook;
    }

    public void writeRawData(int id,String href) throws FilloException {
        System.out.println(id);
        Fillo fillo = new Fillo();
        Connection connection = fillo.getConnection(this.wrkBook);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
        String query = "INSERT INTO raw (id,url,date) VALUES('"+id+"','"+href+"','"+localDate+"')";
        System.out.println(query);
        connection.executeUpdate(query);
        connection.close();
    }


    public void writeFinalData(String id,String name,String url,String matchLevel,String processed) throws FilloException {
        Fillo fillo = new Fillo();
        Connection connection = fillo.getConnection(this.wrkBook);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDate localDate = LocalDate.now();
       // String query = "Update raw Set processed='True' where id = '"+id+"'";
        String query = "INSERT INTO final (id,name,url,date,matchLevel,processed) VALUES('"+id+"','"+name+"','"+url+"','"+localDate+"','"+matchLevel+"','"+processed+"')";
        System.out.println(query);
        connection.executeUpdate(query);
        connection.close();
    }

//    public void writeOutput(LinkedinOutput output) throws FilloException {
//        Fillo fillo = new Fillo();
//        String separator="','";
//        Connection connection = fillo.getConnection(this.wrkBook);
//        //firstName,lastName,title,titleLevel,department,url,city,state
//        String query ="INSERT INTO data(Source,Company,firstName,lastName,title,titleLevel,department,linkedinUrl,city,state,timezone,country,keyword,matchLevel,deepMatchLevel)" +
//                " VALUES('"+output.getSource()+"','"+output.getCompany()+"','"+output.getFirstName()+"','"
//                +output.getLastName()+"','"+output.getTitle()+"','"+output.getTitleLevel()+"','"+output.getDepartment()+
//                "','"+output.getLinkedinUrl()+"','"+output.getCity()+"','"+output.getState()+"','"+output.getTimeZone()+"','"
//                +output.getCountry()+"','"+output.getKeyword()+"','"+output.getMatchLevel()+"','"+output.getDeepMatchLevel()+"')";
//        System.out.println(query);
//        connection.executeUpdate(query);
//
//        connection.close();
//
//    }
//
//
//    public void writeRawOutput(RawOutput output)  {
//        Fillo fillo = new Fillo();
//        String separator="','";
//        Connection connection = null;
//        try {
//            connection = fillo.getConnection(this.wrkBook);
//            //firstName,lastName,title,titleLevel,department,url,city,state
//            String query ="INSERT INTO raw(Source,Company,linkedinUrl,title,isprocessed,keyword,country)" +
//                    " VALUES('"+output.getSource()+"','"+output.getCompanyName()+"','"+output.getUrl()+"','"
//                    +output.getTitle()+"','"+output.isProcessed()+"','"+output.getKeyword()+"','"+output.getCountry()+"')";
//            System.out.println(query);
//            connection.executeUpdate(query);
//        } catch (FilloException e) {
//            e.printStackTrace();
//        }
//
//        connection.close();
//
//    }

}
