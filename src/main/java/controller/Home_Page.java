package controller;

import beans.Album;
import beans.Photo;
import beans.User;
import dao.AlbumDAO;
import dao.PhotoDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.DBConnection;
import utils.Util;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/homepage")
public class Home_Page extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private Connection connection;
    private TemplateEngine templateEngine;


    public void init() throws UnavailableException {
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
        try {
            connection = DBConnection.getConnection(getServletContext());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        //TODO usare gia fatto o modificare forward error
    }



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

        List<Album> myalbumList=null;
        List<Album> otherAlbumList=null;
        List<Photo> myImageList=null;

        try {
            //loading albums
            AlbumDAO albumDAO = new AlbumDAO(DBConnection.getConnection(getServletContext()));
            PhotoDAO photoDAO = new PhotoDAO(DBConnection.getConnection(getServletContext()));
            try {
                myalbumList = albumDAO.getAlbumByUser(username);
                otherAlbumList = albumDAO.getAlbumNotByUser(username);
                myImageList = photoDAO.getAllPhotos(username);
                final WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
                ctx.setVariable("myalbumList", myalbumList);
                ctx.setVariable("otherAlbumList", otherAlbumList);
                ctx.setVariable("username", username);
                ctx.setVariable("myImageList", myImageList);
                String html = templateEngine.process("home_page.html", ctx);

                response.setContentType("text/html");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();

                out.println(html);
                // loading my images

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }





    }
}
