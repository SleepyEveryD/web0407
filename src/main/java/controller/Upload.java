package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import beans.User;
import dao.PhotoDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

/**
 * Servlet implementation class Upload
 */
@WebServlet("/Upload")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2,  // 2 MB
        maxFileSize = 1024 * 1024 * 5,       // 5 MB
        maxRequestSize = 1024 * 1024 * 10     // 10 MB
)
public class Upload extends HttpServlet {
    private static final long serialVersionUID = 1L;


    private Connection connection;
    private TemplateEngine templateEngine;



    /**
     * @see HttpServlet#HttpServlet()
     */
    public Upload() {
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
        PhotoDAO photo_dao = new PhotoDAO(Connection);
        // Constructs path of the directory to save uploaded file
        String uploadFilePath = "D:/image";
        // Creates upload folder if it does not exist
        File uploadFolder = new File(uploadFilePath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        Part part = request.getPart("file");
        User user = (User) request.getSession().getAttribute("user");
        String username = user.getUsername();
        String title = request.getParameter("title").trim();
        String description = request.getParameter("description");

        /* Initial parameters validation */
        if (title == null || title.isEmpty()) {
            forwardError(request, response, "Wrong parameters");
            return;
        }
        if (description == null) {
            description = "No description";
        }

        // Insert image into database
        String fileName = part.getSubmittedFileName();
        String extension = fileName.substring(fileName.lastIndexOf('.'));

        // Allows only JPEG and PNG files to be uploaded
        if (!extension.equalsIgnoreCase(".jpg") && !extension.equalsIgnoreCase(".jpeg") && !extension.equalsIgnoreCase(".png")) {
            forwardError(request, response, "Wrong filetype");
            return;
        }

        String path;

        try {

            File f;
            int baseName = photo_dao.getLastInsertedId()+1;

            String newFileName = baseName  + extension;
            f = new File(uploadFilePath, newFileName);

            path = f.getPath();

            // Rimuove la parte "D:/image" dal percorso
            String relativePath = path.substring(8);



            part.write(path);
            photo_dao.createPhoto(title, description, relativePath, username);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            forwardError(request, response, "An error occurred while uploading the photo");
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("./ShowHome");
    }



    private void forwardError(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException{
        request.setAttribute("error", error);
        forward(request, response, "/Error.html");
    }

    private void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException{
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        templateEngine.process(path, ctx, response.getWriter());

    }

}
