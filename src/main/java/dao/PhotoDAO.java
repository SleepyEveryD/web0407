package dao;


import beans.Photo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhotoDAO {
    private Connection connection;
    public PhotoDAO(Connection connection) {
        this.connection = connection;
    }



    /**
     * used to get an interval [start, start+5] of photos from an album
     * @param album_id album used to retrieve the photos
     * @param start index of the first photo
     * @return list of selected photos
     * @throws SQLException an error occurred
     */
    public List<Photo> getPhotosByAlbum(int album_id, int start) throws SQLException {
        List<Photo> photos = new ArrayList<>();
        String query = "SELECT Photo.ID AS IDPhoto, title, upload_date, path FROM albumcontain JOIN photo ON albumcontain.IDPhoto = Photo.ID WHERE IDAlbum = ? ORDER BY upload_date DESC LIMIT ?, 5";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = connection.prepareStatement(query);
            pstatement.setInt(1, album_id);
            pstatement.setInt(2, start);
            result = pstatement.executeQuery();
            while (result.next()) {
                Photo photo = new Photo();
                photo.setId_Image(result.getInt("IDPhoto"));
                photo.setTitle(result.getString("title"));
                photo.setUploadDate(result.getDate("upload_date"));
                photo.setPath(result.getString("path"));
                photos.add(photo);
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e2) {
                throw new SQLException(e2);
            }
        }
        return photos;
    }

    public List<Photo> getAllPhotos(String username) throws SQLException {
        List<Photo> photos = new ArrayList<>();
        String query = "SELECT id, path, title, upload_date FROM immagini WHERE IDUser = ?"; // 添加 WHERE 子句来过滤结果

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            // 设置查询参数
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String path = rs.getString("path");
                    String title = rs.getString("title");
                    Date upload_date = rs.getDate("upload_date");
                    photos.add(new Photo(id, path, title, upload_date)); // 假设你有一个 Photo 类
                }
            }
        }
        return photos;
    }


    /**
     * used to get the photos related to a user which are not contained in the specified album
     * @param user_id user of reference
     * @param album_id the album whose photos should not be considered
     * @return list of photos
     * @throws SQLException an error occurred
     */
    public List<Photo> getPhotoByUserNotInAlbum(String user_id, int album_id) throws SQLException {
        List<Photo> photos = new ArrayList<>();
        String query = "SELECT Photo.ID, title FROM User JOIN photo ON username = IDUser WHERE Username = ? AND Photo.ID NOT IN (" +
                "SELECT PIA.IDPhoto FROM AlbumContain AS PIA WHERE PIA.IDAlbum = ?" +
                ");";

        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = connection.prepareStatement(query);
            pstatement.setString(1, user_id);
            pstatement.setInt(2, album_id);
            result = pstatement.executeQuery();
            while (result.next()) {
                Photo photo = new Photo();
                photo.setId_Image(result.getInt("ID"));
                photo.setTitle(result.getString("title"));
                photos.add(photo);
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e2) {
                throw new SQLException(e2);
            }
        }
        return photos;
    }

    /**
     * Adds a photo
     * @param title photo's title
     * @param description photo's description text
     * @param path photo's path
     * @return code
     * @throws SQLException an error occurred
     */
    public int createPhoto(String title, String description, String path, String id_user) throws SQLException {
        String query = "INSERT into immagini (title, description, path, IDUser) VALUES(?, ?, ?, ?)";
        int code = 0;
        PreparedStatement pstatement = null;
        try {
            pstatement = connection.prepareStatement(query);
            pstatement.setString(1, title);
            pstatement.setString(2, description);
            pstatement.setString(3, path);
            pstatement.setString(4, id_user);
            code = pstatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e1) {
                System.out.println("create photo error");
            }
        }
        return code;
    }

}