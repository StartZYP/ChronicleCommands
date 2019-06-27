package com.github.mc;

import java.sql.*;

public class DaoTool {
    private static Connection connection = null;
    private static Statement statement = null;
    public DaoTool(String DatabasePath){
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:"+DatabasePath+".db");
            statement = connection.createStatement();
            String SuperGroupTable = "CREATE TABLE IF NOT EXISTS 'myTable'( ID INTEGER PRIMARY KEY  AUTOINCREMENT, GroupName VARCHAR(100),PlayerName VARCHAR(100), PlayerCount INTEGER, CreateTime DATETIME)";
            statement.executeUpdate(SuperGroupTable);
            statement.close();
        } catch ( Exception e ) {
        }
    }


    public static void AddData(String GroupName,String PlayerName,String PlayerCount){
        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            String sql = "INSERT INTO myTable (GroupName,PlayerName,PlayerCount,CreateTime)values('"+GroupName+"','"+PlayerName+"','"+PlayerCount+"',datetime('now','localtime'))";
            statement.executeUpdate(sql);
            connection.commit();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static void Updata(String GroupName,String PlayerName,String PlayerCount){
        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            String sql = "update myTable set PlayerCount='"+PlayerCount+"' where GroupName='"+GroupName+"' and PlayerName='"+PlayerName+"'";
            statement.executeUpdate(sql);
            connection.commit();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


    public static int GetPlayerCount(String GroupNmae,String PlayerNmae){
        int Count = -1;
        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            String GetSuperGroupSql = "select * from myTable where GroupName='"+GroupNmae+"' and PlayerName='"+PlayerNmae+"'";
            ResultSet rs = statement.executeQuery(GetSuperGroupSql);
            while (rs.next()){
                Count = rs.getInt("PlayerCount");
            }
            rs.close();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return Count;
    }

    public static String GetGroupPlayer(String GroupNmae){
        String info = "";
        try {
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            String GetSuperGroupSql = "select * from myTable where GroupName='"+GroupNmae+"'";
            ResultSet rs = statement.executeQuery(GetSuperGroupSql);
            while (rs.next()){
                info = info + "玩家名:"+rs.getString("PlayerName")+"---"+"次数:"+rs.getString("PlayerCount")+",";
            }
            rs.close();
            statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return info;
    }




}
