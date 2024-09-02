package dao;

import beans.Album;
import beans.User;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet(name = "GroupDAO")
public class AlbumDAO extends HttpServlet {
    private final Connection connection;

    public AlbumDAO(Connection connection) {
        this.connection = connection;
    }
    public List<Album> getAllAlbums() throws SQLException {
        List<Album> albums = new ArrayList<>();
        String query = "SELECT ID, title, username FROM Album ORDER BY creation_date DESC";
        try (PreparedStatement pstatement = connection.prepareStatement(query);
             ResultSet result = pstatement.executeQuery()) {

            while (result.next()) {
                Album album = new Album();
                album.setId_album(result.getInt("ID"));
                album.setTitle(result.getString("title"));
                album.setUsername(result.getString("username"));
                albums.add(album);
            }

        } catch (SQLException e) {
            // 记录异常日志或进行处理
            throw new SQLException("Error fetching albums", e);
            // in try-with-resources result and prepared statement  are closed automatically
        }

        return albums;
    }
    public List<Album> getAlbumNotByUser(String username) throws SQLException {
        List<Album> albums = new ArrayList<>();
        String query = "SELECT ID, title, creation_date FROM Album WHERE username <> ? ORDER BY creation_date DESC";

        // 使用 try-with-resources 来自动管理资源
        try (PreparedStatement pstatement = connection.prepareStatement(query)) {
            pstatement.setString(1, username);

            try (ResultSet result = pstatement.executeQuery()) {
                while (result.next()) {
                    Album album = new Album();
                    album.setId_album(result.getInt("ID"));
                    album.setTitle(result.getString("title"));
                    album.setCreation_date(result.getDate("creation_date"));
                    // username 字段从查询中删除，所以不再设置到 Album 对象中
                    albums.add(album);
                }
            }
        } catch (SQLException e) {
            // 记录日志信息或其他处理方式
            throw new SQLException("Error retrieving albums for user: " + username, e);
        }

        return albums;
    }
    public List<Album> getAlbumByUser(String username) throws SQLException {
        List<Album> albums = new ArrayList<>();
        String query = "SELECT ID, title, creation_date FROM Album WHERE username = ? ORDER BY creation_date DESC";

        // 使用 try-with-resources 来自动管理资源
        try (PreparedStatement pstatement = connection.prepareStatement(query)) {
            pstatement.setString(1, username);

            try (ResultSet result = pstatement.executeQuery()) {
                while (result.next()) {
                    Album album = new Album();
                    album.setId_album(result.getInt("ID"));
                    album.setTitle(result.getString("title"));
                    album.setCreation_date(result.getDate("creation_date"));
                    // username 字段从查询中删除，所以不再设置到 Album
                    albums.add(album);
                }
            }
        } catch (SQLException e) {
            // 记录日志信息或其他处理方式
            throw new SQLException("Error retrieving albums for user: " + username, e);
        }

        return albums;
    }

    /**
     * Creates a new album
     * @param title album's title
     * @param username creator
     * @return code
     * @throws SQLException sql error description
     */
    public int createAlbum(String title, String username, String description) throws SQLException {
        String query;
        if (description == null) {
            query = "INSERT INTO album (title, username) VALUES (?, ?)";
        } else {
            query = "INSERT INTO album (title, username, description) VALUES (?, ?, ?)";
        }
        int generatedId = -1; // 用于存储生成的 ID
        PreparedStatement pstatement = null;
        ResultSet generatedKeys = null;

        try {
            // 创建 PreparedStatement 对象，并指定返回自动生成的键
            pstatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstatement.setString(1, title);
            pstatement.setString(2, username);
            if (description != null) {
                pstatement.setString(3, description);
            }

            // 执行插入操作
            pstatement.executeUpdate();
            System.out.println("Album created successfully");

            // 获取生成的主键
            generatedKeys = pstatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1); // 获取第一个生成的主键
            }

        } catch (SQLException e) {
            System.err.println("Error while creating album: " + e.getMessage());
            throw new SQLException("Error while creating album.", e); // 抛出带详细信息的异常
        } finally {
            // 关闭 ResultSet 和 PreparedStatement
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error occurred while closing the ResultSet: " + closeEx.getMessage());
                }
            }
            if (pstatement != null) {
                try {
                    pstatement.close();
                } catch (SQLException closeEx) {
                    System.err.println("Error occurred while closing the PreparedStatement: " + closeEx.getMessage());
                }
            }
        }

        return generatedId;
    }


    public int removePhotoFromAlbum(int album_id, int photo_id) throws SQLException {
        String query = "DELETE FROM album_photo WHERE album_id = ? AND photo_id = ?";
        int code = 0;
        PreparedStatement pstatement = null;
        try {
            pstatement = connection.prepareStatement(query);
            pstatement.setInt(1, album_id);
            pstatement.setInt(2, photo_id);
            code = pstatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);
        } finally {
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e1) {
                System.out.println("remove from album error");
            }
        }
        return code;
    }
}


