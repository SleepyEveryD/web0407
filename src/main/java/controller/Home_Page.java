package controller;

import beans.Group;
import beans.User;
import dao.GroupDAO;
import dao.UserDAO;
import org.thymeleaf.context.WebContext;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/myActivity")
public class Home_Page extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        if (user == null ||!MainController.getOnlineUsers().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))){
            request.getSession().setAttribute("loginError", "You have not logined in.");
            response.sendRedirect("login");
            return;
        }


        String email = user.getEmail();
        System.out.println("email is "+email);

        Set<Group> userGroups=null;
        Set<Group> validUserGroups=new HashSet<>();
        try {
            GroupDAO groupDAO = new GroupDAO(ConnectionHandler.getConnection(getServletContext()));
            try {

                userGroups = groupDAO.getGroupsForUser(email);
                for (Group group : userGroups) {
                    int groupId = group.getGroupId();
                    if (groupDAO.isGroupValid(groupId) && validUserGroups.stream().noneMatch(g -> g.getGroupId() == groupId)) {
                        validUserGroups.add(group);
                    }
                }


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        String html = Util.home_page;
        System.out.println(html);

        String [][] matrix   = new String[validUserGroups.size()][2];
        System.out.println("test> Home_Page > validuserGroup size" + validUserGroups.size());
        int i=0;
        for(Group group:validUserGroups){
            matrix[i][1] = "GroupName: "+group.getTitle();
            matrix[i][0] = "Id: "+String.valueOf(group.getGroupId());
            i++;
        }
        String htmlContent = Util.writeTableToHtml("home_page", matrix,null,null);
        response.setContentType("text/html;charset=UTF-8");
        // 获取输出流
        PrintWriter out = response.getWriter();
        // 将生成的HTML字符串写入响应
        out.println(htmlContent);
        // 关闭输出流
        out.close();
    }
}
