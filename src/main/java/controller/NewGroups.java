package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/groupInfo")
public class NewGroups extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        String title = request.getParameter("title");
        int maxPeople = Integer.parseInt(request.getParameter("maxPeople"));
        int minPeople = Integer.parseInt(request.getParameter("minPeople"));
        int duration = Integer.parseInt(request.getParameter("duration"));


        // 构建动态生成的 HTML 页面
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<!DOCTYPE html>");
        htmlBuilder.append("<html lang=\"zh-CN\">");
        htmlBuilder.append("<head>");
        htmlBuilder.append("<meta charset=\"UTF-8\">");
        htmlBuilder.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
        htmlBuilder.append("<title>Group Details</title>");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body { font-family: Arial, sans-serif; display: flex; justify-content: center; align-items: center; height: 100vh; margin: 0; background-color: #f0f0f0; }");
        htmlBuilder.append(".container { width: 80%; max-width: 600px; background-color: white; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); border-radius: 8px; padding: 20px; }");
        htmlBuilder.append("h1 { margin-bottom: 20px; }");
        htmlBuilder.append(".group-info { margin-bottom: 10px; }");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head>");
        htmlBuilder.append("<body>");
        htmlBuilder.append("<div class=\"container\">");
        htmlBuilder.append("<h1>Group Details</h1>");
        htmlBuilder.append("<div class=\"group-info\">");
        htmlBuilder.append("<strong>Title:</strong> ").append(title).append("<br>");
        htmlBuilder.append("<strong>Max People:</strong> ").append(maxPeople).append("<br>");
        htmlBuilder.append("<strong>Min People:</strong> ").append(minPeople).append("<br>");
        htmlBuilder.append("<strong>Duration:</strong> ").append(duration).append(" days<br>");
        // 在这里添加更多小组信息显示
        htmlBuilder.append("</div>");
        htmlBuilder.append("<a href=\"home.html\">返回首页</a>");
        htmlBuilder.append("</div>");
        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");

        // 将生成的 HTML 页面写入响应
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.println(htmlBuilder.toString());
    }
}