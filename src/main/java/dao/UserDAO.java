package dao;

import beans.User;
import controller.MainController;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "UserDAO")
public class UserDAO extends HttpServlet {
    private static final String SELECT_ALL_USERS ="SELECT* FROM Group WHERE GroupId=? ORDER BY surname";

    private final Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public boolean checkUserExist(String email) throws SQLException {
        String query = "SELECT * FROM user WHERE email=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    return true;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return false;
        }
    }
    public void registerUser (String name, String surname, String email, String password) throws SQLException {
        String encryptedPassword = DigestUtils.sha512Hex(password);
        String query = "INSERT INTO user (email, password, name, surname) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, encryptedPassword);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, surname);
            preparedStatement.executeUpdate();
        }
    }


    public User getUserInfo (String email, String password) throws SQLException {
        String query = "SELECT * FROM user WHERE email=? AND password=?";
        User retrievedUser = new User();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            String encryptedPassword = DigestUtils.sha512Hex(password);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, encryptedPassword);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    retrievedUser.setEmail(resultSet.getString("email"));
                    retrievedUser.setPassword(resultSet.getString("password"));
                    retrievedUser.setName(resultSet.getString("name"));
                    retrievedUser.setSurname(resultSet.getString("surname"));
                    return retrievedUser;
                }
            }
        }
        return null;
    }

    //get other users apart from the logged in user
    public ArrayList<User> getAllUser() throws SQLException {
        String query = "SELECT * FROM user ";
        ArrayList<User> usersList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User retrievedUser = new User();
                    retrievedUser.setEmail(resultSet.getString("email"));
                    retrievedUser.setPassword(resultSet.getString("password"));
                    retrievedUser.setName(resultSet.getString("name"));
                    retrievedUser.setSurname(resultSet.getString("surname"));
                    usersList.add(retrievedUser);
                }
            }
            return usersList;
        }
    }
    public List<User> getAllMembers(int groupId){
        List<User> menbers = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS)){
            preparedStatement.setInt(1,groupId);
            try(ResultSet result = preparedStatement.executeQuery();){
                if(result.next()) {

                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


   public User loginUser(String email,String password) throws SQLException {
       String query = "SELECT * FROM user Where email=? And password=?";
       User loginUser = new User();
       try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
           String encryptedPassword = DigestUtils.sha512Hex(password);
           preparedStatement.setString(1, email);
           preparedStatement.setString(2, encryptedPassword);
           try (ResultSet resultSet = preparedStatement.executeQuery()) {
               if (resultSet.next()) {
                   loginUser.setEmail(resultSet.getString("email"));
                   loginUser.setPassword(resultSet.getString("password"));
                   loginUser.setName(resultSet.getString("name"));
                   loginUser.setSurname(resultSet.getString("surname"));
                   System.out.println("test> UserDAO.java <loginUser returned> email: " + loginUser.getEmail() + " password: " + loginUser.getPassword() + " name: " + loginUser.getName() + " surname: " + loginUser.getSurname());
                   return loginUser;
               }
               else return null;
           }
       }


     }
   }
