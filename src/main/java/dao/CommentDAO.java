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

    /**
     * Adds a comment to the database.
     *
     * @param photoId ID of the photo to which the comment is added
     * @param userId  ID of the user who is adding the comment
     * @param text    The comment text
     * @throws SQLException If an SQL error occurs
     */
    public void addComment(int photoId, String userId, String text) throws SQLException {
        String query = "INSERT INTO comment (IDPhoto, IDUser, text) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, photoId);
            ps.setString(2, userId);
            ps.setString(3, text);
            ps.executeUpdate();
        }
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


}

