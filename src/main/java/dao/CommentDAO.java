package dao;

import beans.Comment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CommentDAO {
    private Connection connection;

    public CommentDAO(Connection connection) {
        this.connection = connection;
    }
    public List<Comment> getCommentsByPhoto(int photo_id, int album_id) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT text, creation_time, IDUser FROM Comment WHERE IDPhoto = ? AND IDAlbum = ? ORDER BY creation_time DESC";
        ResultSet result = null;
        PreparedStatement pstatement = null;
        try {
            pstatement = connection.prepareStatement(query);
            pstatement.setInt(1, photo_id);
            pstatement.setInt(2, album_id);
            result = pstatement.executeQuery();
            while (result.next()) {
                Comment comment = new Comment();
                comment.setText(result.getString("text"));
                comment.setTimestamp(result.getTimestamp("creation_time"));
                comment.setUsername(result.getString("IDUser"));
                comments.add(comment);
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
        return comments;
    }

    /**
     * Adds a comment to the database.
     *
     * @param photoId ID of the photo to which the comment is added
     * @param userId  ID of the user who is adding the comment
     * @param text    The comment text
     * @throws SQLException If an SQL error occurs
     */

    public int addComment(int albumId, int photoId, String userId, String text) throws SQLException {
        String query = "INSERT INTO comment (IDAlbum, IDPhoto, IDUser, text) VALUES (?, ?, ?, ?)";
        int code = 0;

        // Use try-with-resources to automatically close PreparedStatement
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            // Set parameters
            ps.setInt(1, albumId);
            ps.setInt(2, photoId);
            ps.setString(3, userId);
            ps.setString(4, text);

            // Execute update and get affected rows count
            code = ps.executeUpdate();
        } catch (SQLException e) {
            // Log or handle the SQLException as needed
            System.err.println("Error adding comment: " + e.getMessage());
            throw new SQLException("Failed to add comment", e);
        }

        return code;
    }



    /**
     * Retrieves all comments for a given photo ID.
     *
     * @param photoId ID of the photo for which comments are to be retrieved
     * @return A list of comments for the given photo ID
     * @throws SQLException If an SQL error occurs
     */
    public List<Comment> getCommentsByPhotoId(int photoId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT username, text, time_stamp FROM comment WHERE id_image = ? ORDER BY time_stamp DESC";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, photoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment();
                    comment.setUsername(rs.getString("username"));
                    comment.setText(rs.getString("text"));
                    comment.setTimestamp(rs.getTimestamp("time_stamp"));
                    comments.add(comment);
                }
            }
        }
        return comments;
    }


    public void deleteCommentsByPhotoAndAlbum(int photo_id, int album_id) throws SQLException {
        String query = "DELETE FROM Comment WHERE IDPhoto = ? AND IDAlbum = ?";
        PreparedStatement pstatement = null;
        try {
            pstatement = connection.prepareStatement(query);
            pstatement.setInt(1, photo_id);
            pstatement.setInt(2, album_id);
            pstatement.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            try {
                if (pstatement != null) {
                    pstatement.close();
                }
            } catch (Exception e2) {
                throw new SQLException(e2);
            }
        }
    }


}

