package controller;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
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
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.DBConnection;

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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("000000000000");
        PhotoDAO photo_dao = new PhotoDAO(connection);

        // 获取 WEB-INF/classes 目录的绝对路径
        String classesPath = getServletContext().getRealPath("/WEB-INF/classes");
        // 构建 resources 子目录路径
        String uploadFilePath = Paths.get(classesPath, "resources").toString();

        // 创建上传文件夹，如果不存在
        File uploadFolder = new File(uploadFilePath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        System.out.println("1111111111111");

        Part part = request.getPart("file");
        User user = (User) request.getSession().getAttribute("user");
        String username = user.getUsername();
        String title = request.getParameter("title").trim();
        String description = request.getParameter("description");
        System.out.println("222222222222");
        // 初步参数验证
        if (title == null || title.isEmpty()||title.length()>256|| true) {
            forwardError(request, response, "Please enter a title");
            return;
        }
        if (description == null) {
            description = "No description";
        }
        /*
        // 获取文件名和扩展名
        String fileName = part.getSubmittedFileName();
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        System.out.println("BKKkkkkkkkkkkkkkkkkkkkkkkk");
        // 仅允许上传 JPEG 和 PNG 文件
        if (!extension.equalsIgnoreCase(".jpg") && !extension.equalsIgnoreCase(".jpeg") && !extension.equalsIgnoreCase(".png")) {
            forwardError(request, response, "Wrong filetype");
            return;
        }

        String path = null;
        try {
            // 开启事务
            connection.setAutoCommit(false);

            // 使用 UUID 生成随机文件名并转为乱码
            String randomFileName = UUID.randomUUID().toString();
            byte[] bytes = randomFileName.getBytes(StandardCharsets.UTF_8);
            String encodedFileName = new String(bytes, StandardCharsets.ISO_8859_1);  // 使用 ISO_8859_1 编码模拟乱码

            String newFileName = encodedFileName + extension;
            File file = new File(uploadFolder, newFileName);

            path = file.getPath();

            // 获取相对于 classes 的相对路径
            String relativePath = Paths.get(classesPath).relativize(Paths.get(path)).toString();

            // 保存文件到磁盘
            part.write(path);

            // 保存文件信息到数据库
            photo_dao.createPhoto(title, description, relativePath, username);

            // 提交事务
            connection.commit();

        } catch (IOException | SQLException e) {
            e.printStackTrace();
            // 如果 SQL 出错，删除已写入的文件
            if (path != null) {
                File file = new File(path);
                if (file.exists()) {
                    file.delete();
                }
            }
            try {
                // 回滚事务
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            forwardError(request, response, "An error occurred while uploading the photo");
            return;
        } finally {
            try {
                // 恢复默认的自动提交行为
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

         */

        // 文件上传成功后的响应
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("./ShowHome");
    }

    private void forwardError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws ServletException, IOException {
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/homepage").forward(request, response);
        System.out.println("jjjjjj");
    }




    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }


    private void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException{
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
        templateEngine.process(path, ctx, response.getWriter());

    }

}
