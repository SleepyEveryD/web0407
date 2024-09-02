package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.User;
import dao.AlbumDAO;
import dao.AlbumPhotoDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.DBConnection;



/**
 * Servlet implementation class CreateAlbum
 */
@WebServlet("/CreateAlbum")
public class CreateAlbum extends HttpServlet {
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
    public CreateAlbum() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int albumID;

        String title = request.getParameter("title");
        String username = ((User) request.getSession().getAttribute("user")).getUsername();
        String description = request.getParameter("description");
        String[] selectedPhotos = request.getParameterValues("selectedPhotos");

        // Trim title and description and validate
        if (title == null || title.isEmpty() || title.trim().isEmpty() || title.length() > 256) {
            forwardToErrorPage(request, response, "Title must not be empty and should be less than 256 characters.");
            return;
        }

        if (description != null) {
            description = description.trim();
            if (description.length() > 1024) {
                forwardToErrorPage(request, response, "Description must be less than 1024 characters.");
                return;
            }
        }

        AlbumDAO aDAO = new AlbumDAO(connection);
        AlbumPhotoDAO albumPhotoDAO = new AlbumPhotoDAO(connection);

        try {
            // Disable auto-commit to begin the transaction
            connection.setAutoCommit(false);

            // Create the album and get the album ID
            albumID = aDAO.createAlbum(title, username, description);
            System.out.println("Album created with ID " + albumID);

            // If there are selected photos, add them to the album
            if (selectedPhotos != null && selectedPhotos.length != 0) {
                
                for (String photoIdStr : selectedPhotos) {
                    int photoId = Integer.parseInt(photoIdStr);
                    System.out.println("photoId " + photoId + " AlbumID " + photoId );
                    albumPhotoDAO.addPhotoToAlbum(photoId, albumID);
                }
            }

            // Commit the transaction if all operations are successful
            connection.commit();
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("./homepage");

        } catch (SQLException e) {
            // Rollback the transaction in case of any error
            if (connection != null) {
                try {
                    connection.rollback();
                    System.err.println("Transaction rolled back due to an error.");
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();  // Log rollback failure
                }
            }
            e.printStackTrace();
            forwardToErrorPage(request, response, "Unable to add album due to a database error.");

        } finally {
            // Restore the auto-commit mode to the default
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException setAutoCommitEx) {
                setAutoCommitEx.printStackTrace();  // Log auto-commit restoration failure
            }
        }
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
