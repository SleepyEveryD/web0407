package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AlbumPhotoDAO {
    private Connection connection;

    // Constructor to initialize the database connection
    public AlbumPhotoDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * 添加照片到专辑
     *
     * @param photoId 照片 ID
     * @param albumId 专辑 ID
     * @throws SQLException 如果数据库操作失败
     */
    public void addPhotoToAlbum(int photoId, int albumId) throws SQLException {
        String sql = "INSERT INTO album_photo (photo_id, album_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, photoId);
            statement.setInt(2, albumId);
            statement.executeUpdate();

        }

    }
}
