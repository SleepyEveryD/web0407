package dao;

import beans.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCrypt;

@WebServlet(name = "UserDAO")
public class UserDAO extends HttpServlet {
    private static final String SELECT_ALL_USERS = "SELECT* FROM Group WHERE GroupId=? ORDER BY surname";

    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean checkUserExist(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM `user` WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }


    public void registerUser(String username, String email, String password) throws SQLException {
        String sql = "INSERT INTO `user` (username, email, password) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = null;

        try {
            // 开始事务
            connection.setAutoCommit(false);

            // 准备 SQL 语句
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, hashPassword(password)); // 假设你有一个方法来哈希密码

            // 执行 SQL 语句
            preparedStatement.executeUpdate();

            // 提交事务
            connection.commit();
        } catch (SQLException e) {
            handleSQLException(e);
        } finally {
            // 确保资源被释放
            closeResources(preparedStatement);
        }
    }

    private String hashPassword(String password) {
        // 使用 BCrypt 对密码进行哈希处理
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return hashedPassword;
    }

    private boolean verifyPassword(String password, String storedHashedPassword) {
        return BCrypt.checkpw(password, storedHashedPassword);
    }


    // 通用的SQL错误处理和回滚方法
    private void handleSQLException(SQLException e) throws SQLException {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
                throw new SQLException("Rollback failed!", rollbackEx);
            }
        }
        throw new SQLException("Error occurred while processing SQL statement", e);
    }

    private void closeResources(PreparedStatement preparedStatement) {
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public User getUserInfo(String email, String password) throws SQLException {
        String query = "SELECT * FROM `user` WHERE email=? AND password=?";
        User retrievedUser = new User();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            String encryptedPassword = DigestUtils.sha512Hex(password);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, encryptedPassword);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    retrievedUser.setEmail(resultSet.getString("email"));
                    retrievedUser.setPassword(resultSet.getString("password"));
                    retrievedUser.setUsername(resultSet.getString("username"));
                    return retrievedUser;
                }
            }
        }
        return null;
    }

    //get other users apart from the logged in user
    public ArrayList<User> getAllUser() throws SQLException {
        String query = "SELECT * FROM `user` ";
        ArrayList<User> usersList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User retrievedUser = new User();
                    retrievedUser.setEmail(resultSet.getString("email"));
                    retrievedUser.setPassword(resultSet.getString("password"));
                    retrievedUser.setUsername(resultSet.getString("username"));
                    usersList.add(retrievedUser);
                }
            }
            return usersList;
        }
    }

    public List<User> getAllMembers(int groupId) {
        List<User> menbers = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)) {
            preparedStatement.setInt(1, groupId);
            try (ResultSet result = preparedStatement.executeQuery();) {
                if (result.next()) {

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public User loginUser(String email, String password) throws SQLException {
        String query = "SELECT password FROM `user` Where email=? ";
        User loginUser = new User();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // fill the first and second parameter
            String encryptedPassword = hashPassword(password);
            System.out.println(encryptedPassword);
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedHashedPassword = resultSet.getString("password");
                    if (verifyPassword(password, storedHashedPassword)) {
                        loginUser.setEmail(email);
                        loginUser.setUsername(email);
                        return loginUser;
                    }
                }

            }
        }
        return null;
    }

}
