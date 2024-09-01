package dao;


import beans.Photo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
     * @param user photo's uploader
     * @return code
     * @throws SQLException an error occurred
     */
    public int createPhoto(String title, String description, String path, String id_user) throws SQLException {
        String query = "INSERT into photo (title, description, path, IDUser) VALUES(?, ?, ?, ?)";
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

    public int getLastInsertedId() throws SQLException {
        String query = "SELECT MAX(ID) FROM photo"; // Assicurati che 'ID' sia il nome corretto della colonna ID nella tua tabella
        int lastInsertedId = 0;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                lastInsertedId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new SQLException("Errore durante il recupero dell'ultimo ID inserito.", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                System.out.println("Errore durante la chiusura delle risorse del database.");
            }
        }
        return lastInsertedId;
    }
}