package controller;

import beans.Album;
import beans.User;
import dao.AlbumDAO;
import utils.DBConnection;
import utils.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/homepage")
public class Home_Page extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null ){
            request.getSession().setAttribute("loginError", "You have not logined in.");
            response.sendRedirect("login");
            return;
        }else {
            System.out.println("You are logged in.");
        }

        String username = user.getUsername();
        System.out.println("looking for albums of user: " + username);

        List<Album> myalbumList=null;
        List<Album> otherAlbumList=null;
        try {
            AlbumDAO albumDAO = new AlbumDAO(DBConnection.getConnection(getServletContext()));
            try {
                myalbumList = albumDAO.getAlbumByUser(username);
                otherAlbumList = albumDAO.getAlbumNotByUser(username);
                String htmlContent = Util.generateHomePageHtml(myalbumList, otherAlbumList);
                response.setContentType("text/html;charset=UTF-8");
                // 获取输出流
                PrintWriter out = response.getWriter();
                // 将生成的HTML字符串写入响应
                out.println(htmlContent);
                // 关闭输出流
                out.close();


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }



    }
}
