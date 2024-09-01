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
    public int createAlbum(String title, String username) throws SQLException {
        String query = "INSERT into album (title, IDUser) VALUES(?, ?)";
        int code = 0;
        PreparedStatement pstatement = null;
        try {
            pstatement = connection.prepareStatement(query);
            pstatement.setString(1, title);
            pstatement.setString(2, username);
            code = pstatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e1) {
                System.out.println("create album error");
            }
        }
        return code;
    }





    }


