import controller.Register;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

class ConnectionHandlerTest {
    @Test
    public  void userTableTest() {
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/mydatabase?useSSL=false";

        // 数据库的用户名与密码
        final String USER = "root";
        final String PASS = "Yang0914.";
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动器
            Class.forName(JDBC_DRIVER);

            // 打开一个连接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println("实例化声明...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM user";
            ResultSet rs = stmt.executeQuery(sql);

            // 处理结果集
            while (rs.next()) {
                // 通过列名检索
                String email = rs.getString("email");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String surname = rs.getString("surname");

                // 输出数据
                System.out.print("Email: " + email);
                System.out.print(", Password: " + password);
                System.out.print(", Name: " + name);
                System.out.println(", Surname: " + surname);
            }

            // 关闭资源
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("数据库连接测试结束。");
    }
    @Test
    public  void groupTableTest() {
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/mydatabase?useSSL=false";

        // 数据库的用户名与密码
        final String USER = "root";
        final String PASS = "Yang0914.";
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动器
            Class.forName(JDBC_DRIVER);

            // 打开一个连接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println("实例化声明...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM `group`";
            ResultSet rs = stmt.executeQuery(sql);

            // 处理结果集
            while (rs.next()) {
                // 通过列名检索
                String groupId = rs.getString("group_id");
                String title = rs.getString("title");
                Integer max_people = rs.getInt("max_people");
                Integer min_people = rs.getInt("min_people");
                Date date_creation = rs.getDate("date_creation");
                String creater = rs.getString("creator");


                // 输出数据
                System.out.print("groupId: " + groupId);
                System.out.print(", title: " + title);
                System.out.print(", max_people: " + max_people);
                System.out.print(", min_people: " + min_people);
                System.out.print(", date_creation: " + date_creation);
                System.out.println(",creater:" + creater);
                System.out.println();

            }

            // 关闭资源
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("数据库连接测试结束。");
    }
    @Test
    public void user_groupTableTest() {

        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/mydatabase?useSSL=false";

        // 数据库的用户名与密码
        final String USER = "root";
        final String PASS = "Yang0914.";
        Connection conn = null;
        Statement stmt = null;
        try {
            // 注册 JDBC 驱动器
            Class.forName(JDBC_DRIVER);

            // 打开一个连接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println("实例化声明...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT * FROM `user_group`";
            ResultSet rs = stmt.executeQuery(sql);

            // 处理结果集
            while (rs.next()) {
                // 通过列名检索
                String groupId = rs.getString("group_id");
                String email = rs.getString("email");

                // 输出数据
                System.out.print("groupId: " + groupId);
                System.out.print(", email: " + email);

            }
            // 关闭资源
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("数据库连接测试结束。");
    }

    }




