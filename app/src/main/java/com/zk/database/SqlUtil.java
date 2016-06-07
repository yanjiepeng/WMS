package com.zk.database;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Administrator on 2016/6/7.
 */
public class SqlUtil {

    public static Connection conn ;
    private static final String IP_ADDRESS = "192.168.1.102" ;
    private static final String URL = "jdbc:mysql://"
            + IP_ADDRESS
            + ":3306/nc?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8";
    private static final String USER = "root" ;
    private static final String PASSWORD = "123456" ;


    /*
      获得连接
     */

    private static Connection openConnection (String url , String user , String password ) {

        Connection conn = null ;

        final String  DRIVER_NAME = "com.mysql.jdbc.Driver" ;
        try {
            Class.forName(DRIVER_NAME) ;
            conn = DriverManager.getConnection(url , user ,password);
            if (conn != null) {
                TAG.MYSQL_CONNECT_FLAG = true ;
                Log.w("SQL" , "连接成功");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            conn = null;
        }

        return conn;
    }

    public  static void OnConn () {
        conn = openConnection(URL ,USER ,PASSWORD);
        Log.i("tag", 1 + "");
    }





}
