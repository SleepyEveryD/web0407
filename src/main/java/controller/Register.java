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


@WebServlet("/register")
public class Register extends HttpServlet {
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
        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String passwordConfirmation = request.getParameter("passwordConfirmation");
        System.out.println("test> register" + name + " " + surname + " " + email + " " + password + " " + passwordConfirmation);
        if (!passwordConfirmation.equals(password)){ session.setAttribute("registerError", "Please enter same passwords");
        templateEngine.process("register.html", webContext, response.getWriter());}
        if (!isValidEmail(email)) {
            session.setAttribute("registerError", "Invalid email format");
            templateEngine.process("register.html", webContext, response.getWriter());
            return;
        }
        try {
            UserDAO userDAO = new UserDAO(ConnectionHandler.getConnection(getServletContext()));
            if(userDAO.checkUserExist(email)){
                session.setAttribute("registerError", "Email already registered");
                templateEngine.process("register.html", webContext, response.getWriter());

            }
            else {
                userDAO.registerUser(name, surname, email, password);
                session.removeAttribute("registerError");
                response.sendRedirect("login");
                System.out.println("register successful");
            }
        } catch (IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
    }
    private boolean isValidEmail(String email) {
        // 使用正则表达式验证电子邮件格式
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        return email != null && email.matches(emailRegex);
    }




    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebContext webContext = new WebContext(request, response, getServletContext(), request.getLocale());
        webContext.setVariable("loggedIn", false);
        HttpSession session= request.getSession();
        session.removeAttribute("registerError");
        templateEngine.process("register.html", webContext, response.getWriter());
    }
}
