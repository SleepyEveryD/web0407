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
        /* Extracts parameters from POST */
        String username = null;
        String title = null;
        username =  ((User) request.getSession().getAttribute("user")).getUsername();
        title = request.getParameter("title").trim();

        AlbumDAO aDAO = new AlbumDAO(connection);
        if (title == null || title.isEmpty()|| title.trim().isEmpty()||title.length()>256)  {
            forwardToErrorPage(request, response, "Title must not be empty");
            return;
        }
        try {
            aDAO.createAlbum(title, username);
        } catch (SQLException e) {
            e.printStackTrace();
            forwardToErrorPage(request, response, "Unable to add album");
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("./ShowHome");
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
