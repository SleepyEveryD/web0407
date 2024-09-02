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
        String query = "SELECT i.ID AS photo_id, i.title AS photo_title, i.upload_date AS photo_upload_date, " +
                "i.description AS photo_description, i.IDUser AS photo_user, i.path AS photo_path " +
                "FROM album_photo ap " +
                "JOIN immagini i ON ap.photo_id = i.ID " +
                "WHERE ap.album_id = ? " +
                "ORDER BY i.upload_date DESC " +
                "LIMIT ?, 5"; // 修正 LIMIT 子句

        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = connection.prepareStatement(query);
            pstatement.setInt(1, album_id);
            pstatement.setInt(2, start-1); // 设置 LIMIT 的 offset
            result = pstatement.executeQuery();

            while (result.next()) {
                Photo photo = new Photo();
                photo.setId_image(result.getInt("photo_id")); // 使用正确的列名
                photo.setTitle(result.getString("photo_title")); // 使用正确的列名
                photo.setUploadDate(result.getDate("photo_upload_date")); // 使用正确的列名
                photo.setPath(result.getString("photo_path")); // 使用正确的列名
                photo.setId_user(result.getString("photo_user")); // 使用正确的列名和数据类型

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

    public int getTotalPageCountByAlbum(int albumId) throws SQLException {

        int totalCount = 0;
        int photosPerPage = 5;  // 每页的照片数量
        String query = "SELECT COUNT(DISTINCT i.ID) AS total_count " +
                "FROM album_photo ap " +
                "JOIN immagini i ON ap.photo_id = i.ID " +
                "WHERE ap.album_id = ?";
        ResultSet result = null;
        PreparedStatement pstatement = null;

        try {
            // 创建 PreparedStatement 对象
            pstatement = connection.prepareStatement(query);
            pstatement.setInt(1, albumId);

            // 执行查询
            result = pstatement.executeQuery();

            // 获取总照片数
            if (result.next()) {
                totalCount = result.getInt("total_count");
            }

        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            // 关闭 ResultSet
            try {
                if (result != null) {
                    result.close();
                }
            } catch (Exception e1) {
                throw new SQLException(e1);
            }

            // 关闭 PreparedStatement
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e2) {
                throw new SQLException(e2);
            }
        }

        // 计算总页数
        int totalPageCount = (int) Math.ceil((double) totalCount / photosPerPage);
        return totalPageCount;
    }

    /**
     * Retrieves the details of a specific photo by its ID.
     * @param photoId the ID of the photo.
     * @return the photo details.
     * @throws SQLException if an SQL error occurs.
     */
    public Photo getPhotoById(int photoId) throws SQLException {
        Photo photo = null;
        String query = "SELECT ID, title, description, path, upload_date, IDUser " +
                "FROM immagini " +
                "WHERE ID = ?";
        ResultSet result = null;
        PreparedStatement pstatement = null;

        try {
            pstatement = connection.prepareStatement(query);
            pstatement.setInt(1, photoId);
            result = pstatement.executeQuery();

            if (result.next()) {

                photo = new Photo();
                photo.setId_image(result.getInt("ID"));
                photo.setTitle(result.getString("title"));
                photo.setDescritpion(result.getString("description"));
                photo.setPath(result.getString("path"));
                photo.setUploadDate(result.getDate("upload_date"));
                photo.setId_user(result.getString("IDUser"));
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
        return photo;
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
                photo.setId_image(result.getInt("ID"));
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