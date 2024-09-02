import controller.Register;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

class DBConnectionTest {
    @Test
    public void userTableTest() {
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
                String username = rs.getString("username");


                // 输出数据
                System.out.print("Email: " + email);
                System.out.print(", Password: " + password);
                System.out.println(", username: " + username);

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
    public void photoTable() {
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/mydatabase?useSSL=false";

        // 数据库的用户名与密码
        final String USER = "root";
        final String PASS = "Yang0914.";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 注册 JDBC 驱动器
            Class.forName(JDBC_DRIVER);

            // 打开一个连接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // 执行查询
            System.out.println("实例化声明...");
            stmt = conn.createStatement();
            String sql = "SELECT * FROM immagini";
            rs = stmt.executeQuery(sql);

            // 处理结果集
            System.out.println("ID | Title | Upload Date | Description | IDUser | Path");
            System.out.println("-----------------------------------------------------------");
            while (rs.next()) {
                // 通过列名检索数据
                int id = rs.getInt("ID");
                String title = rs.getString("title");
                java.sql.Date uploadDate = rs.getDate("upload_date");
                String description = rs.getString("description");
                String idUser = rs.getString("IDUser");
                String path = rs.getString("path");

                // 打印每一行的数据
                System.out.printf("%d | %s | %s | %s | %s | %s%n",
                        id, title, uploadDate, description, idUser, path);
            }

        } catch (SQLException se) {
            // 处理 JDBC 错误
            se.printStackTrace();
        } catch (Exception e) {
            // 处理 Class.forName 错误
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        System.out.println("数据库连接测试结束。");
    }
    @Test
    public void albumPhotoTable() {
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://localhost:3306/mydatabase?useSSL=false";
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

            // 实例化声明
            System.out.println("实例化声明...");
            stmt = conn.createStatement();

            // 查询 album_photo 表中的所有记录
            String sql = "SELECT * FROM album_photo";
            ResultSet rs = stmt.executeQuery(sql);

            // 处理结果集
            while (rs.next()) {
                int albumId = rs.getInt("album_id");
                int photoId = rs.getInt("photo_id");


                // 打印每个 album 的 id 以及对应的 immagini 的 id
                System.out.println("Album ID: " + albumId + " | Immagini ID: " + photoId);
            }

            // 关闭结果集
            rs.close();

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




