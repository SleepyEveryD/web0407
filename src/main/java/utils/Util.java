package utils;

import java.io.*;

public class Util {
    public static String header ="<nav class=\"h-navbar\">\n" +
            "    <div class=\"h-navbar-container h-container\">\n" +
            "\n" +
            "        <ul class=\"h-menu-items\">\n" +
            "            <li><a href=\"index.html\">Home</a></li>\n" +
            "            <li><a href=\"myActivity\">My activity</a></li>\n" +
            "            <li><a href=\"createGroup\">New Activity</a></li>\n" +
            "            <li><a href=\"login\">Log in </a></li>\n" +
            "            <li><a href=\"#about\">About</a></li>\n" +
            "\n" +
            "        </ul>\n" +
            "        <h1 class=\"h-logo\">PA</h1>\n" +
            "    </div>\n" +
            "</nav>";
    public static String home_page= "<!DOCTYPE html>\n" +
            "<html lang=\"zh-CN\">\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "  <title>Homepage</title>\n" +
            "  <link rel=\"stylesheet\" href=\"header.css\"> <!-- 引入外部 CSS 文件 -->\n" +
            "  <style>\n" +
            "    body {\n" +
            "      font-family: Arial, sans-serif;\n" +
            "      display: flex;\n" +
            "      flex-direction: column;\n" +
            "      align-items: center;\n" +
            "      margin: 0;\n" +
            "      background-color: #f0f0f0;\n" +
            "    }\n" +
            "    .title {\n" +
            "      margin-top: 20px;\n" +
            "      font-size: 2em;\n" +
            "      color: #333;\n" +
            "    }\n" +
            "    .container {\n" +
            "      display: flex;\n" +
            "      width: 80%;\n" +
            "      max-width: 1000px;\n" +
            "      background-color: white;\n" +
            "      box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
            "      border-radius: 8px;\n" +
            "      overflow: hidden;\n" +
            "      margin-top: 20px;\n" +
            "    }\n" +
            "    .left-panel, .right-panel {\n" +
            "      padding: 20px;\n" +
            "      width: 50%;\n" +
            "    }\n" +
            "    .left-panel {\n" +
            "      background-color: #007bff;\n" +
            "      color: white;\n" +
            "    }\n" +
            "    .right-panel {\n" +
            "      background-color: #f8f9fa;\n" +
            "      color: #333;\n" +
            "      overflow-y: auto;\n" +
            "    }\n" +
            "    .btn-create-group {\n" +
            "      display: inline-block;\n" +
            "      padding: 10px 20px;\n" +
            "      background-color: #28a745;\n" +
            "      color: white;\n" +
            "      text-decoration: none;\n" +
            "      border-radius: 4px;\n" +
            "      transition: background-color 0.3s;\n" +
            "    }\n" +
            "    .btn-create-group:hover {\n" +
            "      background-color: #218838;\n" +
            "    }\n" +
            "    .group-list {\n" +
            "      width: 100%;\n" +
            "      border-collapse: collapse;\n" +
            "      margin-top: 20px;\n" +
            "    }\n" +
            "    .group-list th, .group-list td {\n" +
            "      padding: 10px;\n" +
            "      border: 1px solid #ccc;\n" +
            "      text-align: left;\n" +
            "    }\n" +
            "    .group-list th {\n" +
            "      background-color: #007bff;\n" +
            "      color: white;\n" +
            "      text-align: center; \n" +
            "    }\n" +
            "    .group-list td {\n" +
            "      text-align: center; \n" +
            "    }\n" +
            "    .group-list td a {\n" +
            "      text-decoration: none;\n" +
            "      color: #007bff;\n" +
            "      transition: color 0.3s;\n" +
            "    }\n" +
            "    .group-list td a:hover {\n" +
            "      color: #0056b3;\n" +
            "    }\n" +
            "  </style>\n" +
            "</head>\n" +
            "<body>\n" +
            header +
            "<h1 class=\"title\">HOMEPAGE</h1>\n" +
            "<div class=\"container\">\n" +
            "  <div class=\"left-panel\">\n" +
            "    <h2>Create_New_Group</h2>\n" +
            "    <a href=\"#\" class=\"btn-create-group\">创建小组</a>\n" +
            "  </div>\n" +
            "  <div class=\"right-panel\">\n" +
            "    <h2 style=\"text-align: center;\">My_Group</h2>\n" +
            "    <table class=\"group-list\">\n" +
            "      <thead>\n" +
            "      <tr>\n" +
            "        <th>Group ID</th>\n" +
            "        <th>Title</th>\n" +
            "      </tr>\n" +
            "      </thead>\n" +
            "      <tbody>\n" +
            "     //wait for table\n" +
            "      </tr>\n" +
            "      </tbody>\n" +
            "    </table>\n" +
            "  </div>\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>\n";

    public static String selectMembersStatic ="<!DOCTYPE html>\n" +
            "<html lang=\"zh-CN\">\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\">\n" +
            "  <title>选择成员</title>\n" +
            "  <link rel=\"stylesheet\" href=\"createGroup.css\">\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<div class=\"container\">\n" +
            "  <h1>选择成员</h1>\n" +
            "//wait for error" +
            "  <form id=\"memberForm\" method=\"post\" action=\"createGroup\">\n" +
            "<input type=\"hidden\" name=\"action\" value=\"selectMembers\">"+
            "    <table>\n" +
            "      <thead>\n" +
            "      <tr>\n" +
            "        <th>选择</th>\n" +
            "        <th>姓</th>\n" +
            "        <th>名</th>\n" +
            "        <th>邮箱</th>\n" +
            "      </tr>\n" +
            "      </thead>\n" +
            "      <tbody id=\"userTableBody\">\n" +
            "      //wait for table\n" +
            "      </tbody>\n" +
            "    </table>\n" +
            "    <div class=\"button-group\">\n" +

            "      <button type=\"submit\" class=\"btn confirm-btn\">Confirm</button>\n" +
            "      <a href=\"createGroup?action=close\"  class=\"btn close-btn\">Close</a>\n" +
            "    </div>\n" +
            "  </form>\n" +
            "</div>\n" +
            "\n" +
            "</body>\n" +
            "</html>\n";


    public static String readHTMLFile(String htmlFilePath) throws IOException {
        File htmlFile = new File(htmlFilePath);
        BufferedReader reader = new BufferedReader(new FileReader(htmlFile));

        StringBuilder stringBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }
        String htmlContent = stringBuilder.toString();
        reader.close();
        return htmlContent;

    }
    public static String writeTableToHtml(String htmlName, String matrix[][],String error,String hidden) {
        String htmlContent = "";
        switch (htmlName){
            case "createGroup":
                htmlContent = selectMembersStatic;
                break;
            case "home_page":
                htmlContent=home_page;
                break;
            default:
                break;
        }
        if (error!= null && !error.isEmpty()){
            String errorHtml = "<div class=\"error-message\">" + error + "</div>";
            htmlContent = htmlContent.replace("//wait for error", errorHtml);
        }else{
            htmlContent = htmlContent.replace("//wait for error", "");
        }
        String placeholder = "//wait for table";
        String tableHtml = generateTableHtml(matrix, hidden); // 生成要插入的表格HTML

        htmlContent = htmlContent.replace(placeholder, tableHtml);

        System.out.println("test> Util htmlContent: " + htmlContent);

        return htmlContent;

    }

    // 生成表格HTML的方法
    private static String generateTableHtml(String[][] matrix,String hidden) {
        StringBuilder tableHtml = new StringBuilder();

        for (String[] row : matrix) {
            tableHtml.append("<tr>");

            for (int i = 0; i < row.length; i++) {
                if (row[i].equals("checkbox")) {
                    if (row[3].equals(hidden)){
                        i = 4;
                        continue;
                    }
                    tableHtml.append("<td><input type=\"checkbox\" name=\"selectedMembers\" value=\"" + row[3] + "\"></td>");


                }else if (row[1].startsWith("GroupName: ") && row[0].startsWith("Id: ") && i == 1){
                    String groupName = row[1].substring("GroupName: ".length()).trim();
                    int groupId = Integer.parseInt(row[0].substring("Id: ".length()).trim());
                    tableHtml.append("<td><a href=\"GroupInfoServlet?groupId=" + groupId + "\">" + groupName + "</a></td>");

                }
                else {
                    String s = row[i].replace("Id: ","");
                    tableHtml.append("<td>").append(s).append("</td>");
                }
            }
            tableHtml.append("</tr>\n");


        }
        return tableHtml.toString();
    }



}