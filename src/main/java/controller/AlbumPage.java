package controller;

import java.io.*;
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
import dao.AlbumPhotoDAO;
import dao.PhotoDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.DBConnection;



/**
 * Servlet implementation class CreateAlbum
 */
@WebServlet("/albumpage")
public class AlbumPage extends HttpServlet {
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
    public AlbumPage() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("!!!!!!!!!!");
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect("./login");
            System.out.println("You need to login first");
            return;
        }
        System.out.println("it's AlbumPage doGet");
        String idstr = request.getParameter("albumId");
        int albumId = (idstr != null) ? Integer.parseInt(idstr) : 0;

        String pageParam = request.getParameter("page");//html 超链接还要写page
        int page = (pageParam!= null) ? Integer.parseInt(pageParam) : 1;
        System.out.println("AlbumPage: albumId=" + albumId + " page=" + page);
        PhotoDAO photoDAO = new PhotoDAO(connection);
        // 获取 URL 查询参数 id
        int totalPages;
        try {
            totalPages = photoDAO.getTotalPageCountByAlbum(albumId);
            System.out.println(" totalPages=" + totalPages);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (totalPages == 0  || page<=0 ||page>totalPages ){
            System.out.println("" + pageParam + " " + totalPages + " " + page);
            response.sendRedirect("./homepage");
        }else {

            int start = 0;

            if (page > 0) start = 5 * (page - 1); // 计算开始位置
            System.out.println("start=" + start);
            List<Photo> photos = null; // 根据 id 和当前页获取照片
            try {
                photos = photoDAO.getPhotosByAlbum(albumId, start);
                System.out.println("Photos size=" + photos.size());
                for (Photo photo : photos) {
                    System.out.println( "" + photo.getId_user() + " " + photo.getTitle() + " " + photo.getId_image()+ " " + photo.getPath());


                }
                // 获取总页数
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // 计算是否有下一页
            boolean hasNextPage = page < totalPages;

            boolean hasPreviousPage = page > 1;
            System.out.println("hasNextPage=" + hasNextPage + " hasPreviousPage=" + hasPreviousPage );
            final WebContext webContext = new WebContext(request, response, getServletContext(), request.getLocale());

            // 将数据添加到请求属性中
            webContext.setVariable("photos", photos);
            webContext.setVariable("currentPage", page);
            webContext.setVariable("hasNextPage", hasNextPage);
            webContext.setVariable("hasPreviousPage", hasPreviousPage);
            webContext.setVariable("albumId", albumId);



            String html = templateEngine.process("AlbumPage.html", webContext);

            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            out.println(html);
            out.flush();
            out.close();

        }
    }
    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /* Extracts parameters from POST */

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
