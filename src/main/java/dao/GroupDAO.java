package dao;

import beans.Group;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.*;
import java.util.*;
import java.sql.Date;
import java.time.LocalDate;

@WebServlet(name = "GroupDAO")
public class GroupDAO extends HttpServlet {
    private final Connection connection;

    public GroupDAO(Connection connection) {
        this.connection = connection;
    }

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





    public ArrayList<Group> groupsInvitations(String email) throws SQLException {
        /*
        ArrayList<Group> GroupInvitations = new ArrayList<>();
        String query = "SELECT * FROM `group` WHERE DATE_ADD(date_creation, INTERVAL duration_days DAY) >= ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setTime(1, new Time(System.currentTimeMillis()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Group Group = new Group();
                    Group.setGroupId(resultSet.getInt("group_id"));
                    Group.setTitle(resultSet.getString("title"));
                    Group.setMaxPeople(resultSet.getInt("max_people"));
                    Group.setMinPeople(resultSet.getInt("min_people"));
                    Group.setDuration(resultSet.getInt("duration_days"));
                    Group.setDate_creation(resultSet.getDate("date_creation"));
                    Gson gson = new Gson();
                    ArrayList<String> members= gson.fromJson(resultSet.getString("member_list"), new TypeToken<List<String>>() {
                    }.getType());
                    if (members.contains(email)) {
                        GroupInvitations.add(Group);
                    }
                }
            }
            return GroupInvitations;
        }

         */
        return null;
    }
    public Set<Group> getGroupsForUser(String email) throws SQLException {
        Set<Group> groups = new HashSet<>();
        String query = "SELECT g.group_id, g.title, g.max_people, g.min_people, g.duration_days, g.date_creation , g.creator " +
                "FROM `group` g " +
                "JOIN user_group ug ON g.group_id = ug.group_id " +
                "WHERE ug.email = ? OR g.creator = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            System.out.println("test> groupDao> getGroupsForUser> email" + email);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int groupId = resultSet.getInt("g.group_id");
                    System.out.println("test> groupID" + groupId);
                    String creator = resultSet.getString("g.creator");
                    String title = resultSet.getString("g.title");
                    int maxPeople = resultSet.getInt("g.max_people");
                    int minPeople = resultSet.getInt("g.min_people");
                    int durationDays = resultSet.getInt("g.duration_days");
                    Date dateCreation = resultSet.getDate("g.date_creation");


                    Group group = new Group(groupId, title, maxPeople, minPeople, durationDays, dateCreation);
                    groups.add(group);
                    System.out.println("test> Groupdao> groupId" + groupId+ "title" + title + "creator" + creator);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error fetching groups for user: " + email, e);
        }

        return groups;
    }



    public Group getGroupByID(int groupId) throws  SQLException{
        String query = "SELECT * FROM `group` WHERE group_id = ?";
        Group group = new Group();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, groupId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    group.setTitle(resultSet.getString("title"));
                    group.setDuration(resultSet.getInt("duration"));
                    group.setMinPeople(resultSet.getInt("minPeople"));
                    group.setMaxPeople(resultSet.getInt("maxPeople"));
                    group.setDate_creation(resultSet.getDate("date_creation"));
                    return group;
                }
            }
        }
        return null;
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

    /*

    public ArrayList<Group> GroupsCreated(int id) throws SQLException {
        ArrayList<Group> GroupList = new ArrayList<>();
        Date date = new Date();
        java.sql.Date date1 = new java.sql.Date(date.getTime());
        String query = "SELECT * FROM Group WHERE speakerid=? AND (date>? OR (date=? AND start_time>=?))";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setDate(2, date1);
            preparedStatement.setDate(3, date1);
            preparedStatement.setTime(4, new Time(System.currentTimeMillis()));
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Group Group = new Group();
                    Group.setId(resultSet.getInt("idGroup"));
                    Group.setSpeakerId(resultSet.getInt("speakerid"));
                    Group.setTopic(resultSet.getString("topic"));
                    Group.setDate(resultSet.getDate("date"));
                    Group.setStartTime(resultSet.getTime("start_time"));
                    Group.setEndTime(resultSet.getTime("end_time"));
                    Group.setCapacity(resultSet.getInt("capacity"));
                    GroupList.add(Group);
                }
            }
            return GroupList;
        }

     */
    }


