package controller;

import beans.Comment;
import beans.Photo;
import beans.User;
import dao.CommentDAO;
import dao.PhotoDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.DBConnection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/imagePage")
public class ImagePage extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection;

    private TemplateEngine templateEngine;
    @Override
    public void init() throws ServletException {
        // Initialize the template engine and DAOs
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
        }}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("it's ImagePage doGet");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("./login");
            System.out.println("You need to login first");
            return;
        }
        System.out.println("user logged in");

        String idStr = request.getParameter("photoId");
        String albumIdStr = request.getParameter("albumId");
        System.out.println("ImagePage doGet: photoId: " + idStr + " albumId: " + albumIdStr);


        int photoID =-1;
        int albumId =-1;
        try {
            photoID = Integer.parseInt(idStr);
            albumId =Integer.parseInt(albumIdStr);


        } catch (NumberFormatException e) {
            response.sendRedirect("./homepage");
            return;
        }


        Photo photo;
        try {
            PhotoDAO photoDAO = new PhotoDAO(connection);
            photo = photoDAO.getPhotoById(photoID);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving photo", e);
        }

        if (photo == null) {
            response.sendRedirect("./homepage");
            return;
        }

        System.out.println("photo not null");

        List<Comment> comments;
        try {
            CommentDAO commentDAO = new CommentDAO(connection);
            comments = commentDAO.getCommentsByPhoto(photoID, albumId);
        } catch (SQLException e) {
            throw new ServletException("Error retrieving comments", e);
        }

        // Prepare Thymeleaf context and render the page
        boolean owner = photo!=null &&photo.getId_user().equals(user.getUsername());



        final WebContext webContext = new WebContext(request, response, getServletContext(), request.getLocale());
        webContext.setVariable("photo", photo);
        webContext.setVariable("albumId", albumIdStr);
        webContext.setVariable("comments", comments);
        webContext.setVariable("owner", owner);

        // Process and output the HTML
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        String html = templateEngine.process("ImagePage", webContext);
        try (PrintWriter out = response.getWriter()) {
            out.println(html);
        }
    }
}
