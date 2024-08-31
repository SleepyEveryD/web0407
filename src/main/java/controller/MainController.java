package controller;

import beans.User;

import java.util.ArrayList;
import java.util.List;

public class MainController {
    private static final List<User> onlineUsers = new ArrayList<>();
    public static List<User> getOnlineUsers() {
        return onlineUsers;
    }
    public static void addOnlineUser(User user) {
        onlineUsers.add(user);
    }
    public static void removeOnlineUser(User user) {
        onlineUsers.remove(user);
    }
}
