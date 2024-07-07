package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import beans.User;
import dao.UserDAO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.ConnectionHandler;



import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet("/login")
public class Login extends HttpServlet {
    TemplateEngine templateEngine;

    @Override
    public void init() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        WebContext webContext = new WebContext(request, response, getServletContext(), request.getLocale());
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            UserDAO userDAO = new UserDAO(ConnectionHandler.getConnection(getServletContext()));
            if (userDAO.loginUser(email,password)==null) {
                session.setAttribute("loginError", "Wrong email or password please check");
                templateEngine.process("login.html", webContext, response.getWriter());
            } else {
                User user = userDAO.loginUser(email,password);
                session.setAttribute("user", user);
                session.removeAttribute("loginError");
                response.sendRedirect("index.html");
                MainController.addOnlineUser(user);
            }
        } catch (IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //check login
        System.out.println("checking login");
        User user = (User) request.getSession().getAttribute("user");
        if (user!= null){
            System.out.println("test> onlinePlayer" + MainController.getOnlineUsers().get(0).getEmail());
            System.out.println(user.getEmail());

        }else {
            System.out.println("Test> user is null");
        }

        if (!MainController.getOnlineUsers().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))){
            System.out.println(" you are offline");
            request.getSession().setAttribute("loginError", "You have not logined in.");
            response.sendRedirect("login.html");
        }else{
            System.out.println(" you are online");
            response.sendRedirect("index.html");
        }

    }
}