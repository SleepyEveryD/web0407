package controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.sql.SQLException;

import dao.UserDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.DBConnection;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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

        // 获取表单数据
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String passwordConfirmation = request.getParameter("passwordConfirmation");

        // 打印日志
        System.out.println("test> register " + username + " " + email + " " + password + " " + passwordConfirmation);

        // 检查密码是否匹配
        if (!passwordConfirmation.equals(password)) {
            session.setAttribute("registerError", "Please enter the same passwords");
            templateEngine.process("register.html", webContext, response.getWriter());
            return;
        }

        // 检查邮箱格式是否有效
        if (!isValidEmail(email)) {
            session.setAttribute("registerError", "Invalid email format");
            templateEngine.process("register.html", webContext, response.getWriter());
            return;
        }

        try {
            UserDAO userDAO = new UserDAO(DBConnection.getConnection(getServletContext()));
            if (userDAO.checkUsernameExist(username)) {
                session.setAttribute("registerError", "Username already taken");
                templateEngine.process("register.html", webContext, response.getWriter());
                return;
            }
            // 检查邮箱是否已经注册
            if (userDAO.checkUserExist(email)) {
                session.setAttribute("registerError", "Email already registered");
                templateEngine.process("register.html", webContext, response.getWriter());
            } else {
                // 注册用户
                userDAO.registerUser(username, email, password);
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
