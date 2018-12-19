package com.luban.utils;


import java.io.*;
import java.sql.*;
import java.util.Properties;

public class DBUtil {

    /**
     * 根据类路径下的.properties的配置文件，
     * 获取className,url,userName,passWord
     * @return Connection
     */
    public static Connection getConnection(){
        String className = "";
        String url = "";
        String userName = "";
        String passWord = "";
        InputStream stream = DBUtil.class.getClassLoader().getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        try {
            properties.load(stream);
            className = properties.getProperty("jdbc.className");
            url = properties.getProperty("jdbc.url");
            userName = properties.getProperty("jdbc.userName");
            passWord = properties.getProperty("jdbc.passWord");
        } catch (IOException e) {
            e.printStackTrace();
        }

        loadDriver(className);

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url,userName,passWord);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public static void loadDriver(String className){
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }




    public static void executeSql(String sql){
        Connection connection = getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static boolean tableIsExist(String tableName){
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select column_name from information_schema.columns where table_schema='epcc' and table_name='" + tableName + "'");
            boolean next = resultSet.next();
            if (next){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();

        }finally {
            closeConnection(connection,statement,null);
        }
        return false;
    }


    public static void closeConnection(Connection connection, Statement statement, ResultSet resultSet){
        try{
            if(connection!=null){
                connection.close();
            }
            if(statement!=null){
                statement.close();
            }
            if(resultSet!=null){
                resultSet.close();
            }
        }catch (Exception e){

            e.printStackTrace();
        }
    }




}
