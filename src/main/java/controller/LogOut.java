package controller;



import beans.User;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/logout")
public class LogOut extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response); // POST 请求和 GET 请求一样处理
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            MainController.removeOnlineUser(user); // 从在线用户列表中移除用户
            session.removeAttribute("user"); // 清除用户会话信息
            session.invalidate(); // 使会话失效，清除所有相关的会话数据

            System.out.println("User logged out successfully.");
        }


        response.sendRedirect(request.getContextPath() + "/index.html");
    }
}
