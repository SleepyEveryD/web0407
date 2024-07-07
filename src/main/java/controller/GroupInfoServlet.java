package controller;

import beans.Group;
import beans.User;
import dao.GroupDAO;
import dao.UserDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.ConnectionHandler;
import utils.Util;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
@WebServlet("/GroupInfoServlet")
public class GroupInfoServlet extends HttpServlet {
    GroupDAO groupDAO;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        int groupId = Integer.parseInt(request.getParameter("groupId"));
        try {
            groupDAO = new GroupDAO(ConnectionHandler.getConnection(getServletContext()));
            try {
              Group  group = groupDAO.getGroupByID(groupId);
                response.setContentType("text/html");
                response.setCharacterEncoding("UTF-8");

                if (group != null) {
                    response.getWriter().println("<h1>Group: " + group.getTitle() + "</h1>");
                    response.getWriter().println("<h2>Members:"+ group.getMembers()+ "</h2>");
                    response.getWriter().println("<ul>");
                    for (User member : group.getMembers()) {
                        response.getWriter().println("<li>" + member + "</li>");
                    }
                    response.getWriter().println("</ul>");
                } else {
                    response.getWriter().println("<h1>Group not found</h1>");
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

}

