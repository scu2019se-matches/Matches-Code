package util;

import java.sql.*;

public class DatabaseHelper
{
    // 数据库IP
    private static final String DB_IP = "127.0.0.1";
    // 端口号
    private static final String DB_PORT = "3306";
    // 数据库名
    static final String DB_NAME = "test";
    // 用户名
    private static final String DB_USR = "root";
    // 密码
    private static final String DB_PASS = "1234";

    private static final String DB_URL = String.format(
            "jdbc:mysql://%s:%s/%s?%s",
            DB_IP, DB_PORT, DB_NAME, "useUnicode=true&characterEncoding=UTF-8");

    private Connection conn = null;
    private Statement st = null;

    public DatabaseHelper()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {

            conn = DriverManager.getConnection(DB_URL, DB_USR, DB_PASS);
            st = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public void dispose()
    {
        try {
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行SQL语句
     * @param sql 要执行的语句
     * @return 是否执行成功
     */
    public boolean execute(String sql)
    {
        System.out.printf("DatabaseHelper.execute sql = %s\n", sql);
        boolean ret = false;
        try {
            ret = st.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 执行SQL查询语句
     * @param sql 要执行的查询语句
     * @return 查询结果的集合
     */
    public ResultSet executeQuery(String sql)
    {
        System.out.printf("DatabaseHelper.executeQuery sql = %s\n", sql);
        ResultSet ret = null;
        try {
            ret = st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
