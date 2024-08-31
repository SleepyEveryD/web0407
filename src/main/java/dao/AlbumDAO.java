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
                album.setId_User(result.getString("username"));
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
                    album.setCreationDate(result.getDate("creation_date"));
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
                    album.setCreationDate(result.getDate("creation_date"));
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

/*
    public int registerGroup(String title, String creator, int max_people, int min_people, int duration_days, ArrayList<String> members) throws SQLException {
        // Register the group and get the generated group_id
        String insertGroupQuery = "INSERT INTO `group` (title, creator, max_people, min_people, duration_days, date_creation) VALUES (?, ?, ?, ?, ?, ?)";
        String insertUserGroupQuery = "INSERT INTO user_group (email, group_id) VALUES (?, ?)";
        int groupId = 0;


        try {
            long millis = System.currentTimeMillis();
            Date date_creation = new Date(millis);

            // Insert into `group` table
            try (PreparedStatement insertGroupStmt = connection.prepareStatement(insertGroupQuery, Statement.RETURN_GENERATED_KEYS)) {
                insertGroupStmt.setString(1, title);
                insertGroupStmt.setString(2, creator);
                insertGroupStmt.setInt(3, max_people);
                insertGroupStmt.setInt(4, min_people);
                insertGroupStmt.setInt(5, duration_days);
                insertGroupStmt.setDate(6, date_creation);
                insertGroupStmt.executeUpdate();

                // Retrieve the generated group_id
                try (ResultSet generatedKeys = insertGroupStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        groupId = generatedKeys.getInt(1);
                        System.out.println("Group registered successfully with ID: " + groupId);
                    } else {
                        throw new SQLException("Creating group failed, no ID obtained.");
                    }
                }
            }

            // Insert into `user_group` table for each member
            try (PreparedStatement insertUserGroupStmt = connection.prepareStatement(insertUserGroupQuery)) {
                for (String member : members) {
                    System.out.println("test> groupDao " + member);
                    insertUserGroupStmt.setString(1, member);
                    insertUserGroupStmt.setInt(2, groupId);
                    insertUserGroupStmt.executeUpdate();
                }
                System.out.println("Members added to user_group table successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error registering group.");
        }

        return groupId;
    }





    public Group getGroupByID(int groupId) throws  SQLException{
        String query = "SELECT * FROM `group` WHERE group_id = ?";
        Group group = new Group();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, groupId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    group.setCreator(resultSet.getString("creator"));
                    group.setTitle(resultSet.getString("title"));
                    group.setDuration(resultSet.getInt("duration_days"));
                    group.setMinPeople(resultSet.getInt("min_people"));
                    group.setMaxPeople(resultSet.getInt("max_people"));
                    group.setDate_creation(resultSet.getDate("date_creation"));
                    List<User> members = getMembersByGroupId(groupId);
                    group.setMembers(members);
                    return group;
                }
            }
        }
        return null;
    }

    private List<User> getMembersByGroupId(int groupId) throws SQLException {

        List<User> members = new ArrayList<>();
        // SQL 查询语句，从 group 表和 user 表中获取相关信息
        String query = "SELECT u.surname, u.name, u.email " +
                "FROM user u " +
                "JOIN user_group ug ON u.email = ug.email " +
                "WHERE ug.group_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // 设置查询参数
            preparedStatement.setInt(1, groupId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // 从结果集中获取各字段的值
                    String userID = resultSet.getString("user_id");
                    String name = resultSet.getString("username");
                    String email = resultSet.getString("email");

                    // 创建 User 对象，用于表示小组的成员
                    User member = new User();
                    member.setUsername(name);
                    member.setEmail(email);

                    // 将成员添加到成员列表中
                    members.add(member);
                }
            }
        }

        return members;  // 返回成员列表
    }

    public boolean isGroupValid(int groupId) throws SQLException {
        String query = "SELECT date_creation, duration_days FROM `group` WHERE group_id = ?";

        LocalDate currentDate = LocalDate.now(); // 获取当前日期

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, groupId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    LocalDate dateCreation = resultSet.getDate("date_creation").toLocalDate();
                    int durationDays = resultSet.getInt("duration_days");

                    // 计算有效期截止日期
                    LocalDate expirationDate = dateCreation.plusDays(durationDays);

                    // 比较截止日期和当前日期
                    return !currentDate.isAfter(expirationDate);
                }
            }
        }

        return false; // 如果未找到匹配的小组，也可以返回 false
    }

 */


    }


