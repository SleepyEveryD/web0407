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
import dao.CommentDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.DBConnection;


/**
 * Servlet implementation class DeleteImageFromAlbum
 */
@WebServlet("/DeleteImageFromAlbum")
public class DeleteImageFromAlbum extends HttpServlet {
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


    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteImageFromAlbum() {
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
        System.out.println(" DeleteImageFromAlbum do post");
        CommentDAO c_dao = new CommentDAO(connection);
        AlbumDAO a_dao = new AlbumDAO(connection);
        User user = (User) request.getSession().getAttribute("user"); // 修正获取 session 中 user 的方式
        if (user == null) {
            response.sendRedirect("./Login");
            return;
        }
        /* Extracts parameters from POST and session */
        String username = user.getUsername();
        String req_photo_id = request.getParameter("photoId"); // 确保表单参数名称与代码一致
        String req_album_id = request.getParameter("albumId"); // 确保表单参数名称与代码一致
        int photo_id = -1, album_id = -1;
        System.out.println( "photo_id: " + req_photo_id + " album_id: " + req_album_id);
        /* Initial parameters validation */
        try {
            photo_id = Integer.parseInt(req_photo_id);
            album_id = Integer.parseInt(req_album_id);
        } catch (NumberFormatException nf) {
            forwardError(request, response, "Photo's ID and Album's ID must be integer values.");
            return;
        }
        System.out.println("Start Commit");

        // 开始事务
        try {
            connection.setAutoCommit(false); // 禁用自动提交，开启手动事务控制
            System.out.println("111111");
            // 删除评论并从相册中移除照片
            c_dao.deleteCommentsByPhotoAndAlbum(photo_id, album_id);
            System.out.println("222222");
            a_dao.removePhotoFromAlbum(album_id, photo_id);
            System.out.println("333333");

            connection.commit(); // 提交事务
            System.out.println("444444");

        } catch (SQLException e) {
            // 捕获异常并回滚事务
            if (connection != null) {
                try {
                    connection.rollback(); // 回滚事务
                    System.err.println("Transaction rolled back due to an error: " + e.getMessage());
                } catch (SQLException ex) {
                    System.err.println("Failed to rollback the transaction: " + ex.getMessage());
                }
            }
            e.printStackTrace();
            forwardError(request, response, "Unable to add comment or remove photo from album.");
            return;
        } finally {
            try {
                connection.setAutoCommit(true); // 恢复默认的自动提交模式
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        // 成功处理后重定向
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect(String.format("./albumpage?albumId=%d", album_id));
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
