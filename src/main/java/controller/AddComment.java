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
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;



/**
 * Servlet implementation class CreateAlbum
 */
@WebServlet("/AddComment")
public class AddComment extends HttpServlet {
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
     * @see HttpServlet#HttpServlet()
     */
    public AddComment() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String photoIdStr = request.getParameter("photoId");

        if (photoIdStr == null) {
            forwardToErrorPage(request, response, "Missing photo ID");
            return;
        }

        int photoId;
        try {
            photoId = Integer.parseInt(photoIdStr);
        } catch (NumberFormatException e) {
            forwardToErrorPage(request, response, "Invalid photo ID");
            return;
        }

        // Initialize DAOs
        PhotoDAO photoDAO = new PhotoDAO(connection);
        CommentDAO commentDAO = new CommentDAO(connection);

        Photo photo;
        List<Comment> comments;

        try {
            // Get the photo details
            photo = photoDAO.getPhotoById(photoId);
            if (photo == null) {
                forwardToErrorPage(request, response, "Photo not found");
                return;
            }

            // Get all comments for this photo
            comments = commentDAO.getCommentsByPhotoId(photoId);
        } catch (SQLException e) {
            forwardToErrorPage(request, response, "Database error: " + e.getMessage());
            return;
        }

        // Set attributes for the template
        request.setAttribute("photo", photo);
        request.setAttribute("comments", comments);

        // Forward to the photo detail page
        String path = "/WEB-INF/templates/photoDetail.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        ctx.setVariable("photo", photo);
        ctx.setVariable("comments", comments);
        templateEngine.process(path, ctx, response.getWriter());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("In server side code of AddComment servlet");
        CommentDAO c_dao = new CommentDAO(connection);
        /* Extracts parameters from POST and session*/
        User user = (User)request.getSession().getAttribute("user");
        if(user == null) {
            forwardToErrorPage(request, response, "Please login to add comments");
            return;
        }
        String username = user.getUsername();
        String comment = null;
        String req_photo_id = null;
        String req_album_id = null;
        comment = request.getParameter("comment");
        if (comment == null|| comment.isEmpty()||comment.trim().isEmpty()) forwardToErrorPage( request, response, "Comment can not be empty");
        req_photo_id = request.getParameter("photoId");
        req_album_id = request.getParameter("albumId");
        System.out.println( " req_photo_id: " + req_photo_id + " req_album_id: " + req_album_id + " comment: " + comment);
        int photo_id = -1, album_id = -1;
        /* Initial parameters validation */
        try {
            photo_id = Integer.parseInt(req_photo_id);
            album_id = Integer.parseInt(req_album_id);
        } catch (NumberFormatException nf){
            forwardToErrorPage(request, response, "Photo's ID must be an integer value");
            return;
        }

        System.out.println(""+photo_id+" "+album_id+" "+username+" "+comment);

        try {
            c_dao.addComment(album_id,photo_id,username,comment.trim());
            System.out.println("added comment");
        } catch (SQLException e) {
            e.printStackTrace();
            forwardToErrorPage(request, response, "Unable to add comment");
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(
                String.format(
                        "./imagePage?albumId=%d&page=%d&photoId=%d",

                        album_id,
                        0,
                        photo_id
                )
        );
        return;
    }

    private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException{
        request.setAttribute("error", error);
        forward(request, response, "/Error.html");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException{
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        templateEngine.process(path, ctx, response.getWriter());
    }

}
