package com.luban.utils;

import com.luban.annotations.Id;
import com.luban.annotations.Table;
import java.io.File;
import java.lang.reflect.Field;


public class GenerateTables {
    public static void main(String[] args) {

        createTable();
    }
    /**
     * 根据类路径下的entity创建表
     */
    public static void createTable(){
        String path = GenerateTables.class.getResource("/").getPath();
        File file = new File(path);
        String bastPath = file.getPath();
        doScanPackages(file, bastPath);
    }

    public static void doScanPackages(File file,String bastPath){
        if(file.isDirectory()){
            for (File listFile : file.listFiles()) {
                doScanPackages(listFile,bastPath);
            }
        }else{
            String fileExtension = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            //只处理后缀名是class的文件
            if("class".equals(fileExtension)){
               processClass(file,bastPath);
            }
        }
    }

    private static void processClass(File file,String bastPath) {
            String path = file.getPath().replace(bastPath,"");
            //得到ClassName
            String className = path.substring(1,path.lastIndexOf("."));
            className = className.replace("\\",".");
            //根据类名得到类的类对象
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            boolean present = clazz.isAnnotationPresent(Table.class);
            if(present){
                //
                String tableName = null;
                Table annotation = clazz.getAnnotation(Table.class);
                if(annotation.name().equals("")){
                    tableName = clazz.getSimpleName();
                }else{
                    tableName = annotation.name();
                }

                String sql = doGenerateSql(tableName, clazz);
                //判断表是否存在
                boolean isExist = DBUtil.tableIsExist(tableName);
                String dropTable = "drop table " +tableName;
                if(isExist){
                    DBUtil.executeSql(dropTable);
                }
                DBUtil.executeSql(sql);
            }
    }

    private static String doGenerateSql(String tableName,Class<?> clazz) {
        String sql ="create table " +tableName +"(\n";
        String columnSql = "";
        //得到所有的属性
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String typeName = field.getType().getSimpleName();
            String columnName = field.getName();
            String sqlType = "";
            switch (typeName){
                case "String" :
                    sqlType = "varchar(255)";
                    break;
                case "Integer" :
                    sqlType = "int(32)";
                    break;
                case "Date" :
                    sqlType = "datetime";
                    break;
                case "boolean" :
                    sqlType = "tinyInt";
                    break;
                default:
                    sqlType ="varchar(255)";
            }
            columnSql += columnName+" " +sqlType;

            if(field.isAnnotationPresent(Id.class)){
                columnSql += " primary key ,\n";
            }else{
                columnSql +=",\n";
            }

        }
        columnSql = columnSql.substring(0,columnSql.lastIndexOf(","));
        sql+=columnSql + "\n)";

        System.out.println("建表语句:\n"+sql);
        return sql;
    }

}
