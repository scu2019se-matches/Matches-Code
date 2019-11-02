package util;

import java.sql.*;

public class DatabaseHelper
{
    // 数据库URL
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8";

    // 用户名
    private static final String DB_USR = "root";

    // 密码
    private static final String DB_PASS = "1234";

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
        ResultSet ret = null;
        try {
            ret = st.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
