package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Photo;
import beans.User;
import dao.AlbumDAO;
import dao.PhotoDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.DBConnection;



/**
 * Servlet implementation class CreateAlbum
 */
@WebServlet("/ShowImageSelection")
public class ShowImageSelection extends HttpServlet {
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
    }


    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");

        PhotoDAO photoDAO = new PhotoDAO(connection);


        List<Photo> photos;
        try {
            photos = photoDAO.getAllPhotos(user.getUsername()); // Assume this method returns all user photos
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            return;
        }

        WebContext ctx = new WebContext(request, response, getServletContext(), request.getLocale());
        ctx.setVariable("photos", photos);

        templateEngine.process("homepage.html", ctx, response.getWriter());

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
    private void forwardError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        forward(request, response, "/Error.html");
    }
    private void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException{
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        templateEngine.process(path, ctx, response.getWriter());

    }



}
