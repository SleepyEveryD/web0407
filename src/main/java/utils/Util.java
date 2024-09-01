package utils;

import beans.Album;

import java.util.List;

public class Util {
    public static String generateHomePageHtml(List<Album> myAlbums, List<Album> otherAlbums) {
        StringBuilder myAlbumsHtml = new StringBuilder();
        StringBuilder otherAlbumsHtml = new StringBuilder();

        for (Album album : myAlbums) {
            myAlbumsHtml.append("<li><a href=\"AlbumInfoServlet?albumId=").append(album.getId_album())
                    .append("\">").append(album.getUsername()).append(" - ").append(album.getTitle()).append("</a></li>");
        }
        for (Album album : otherAlbums) {
            otherAlbumsHtml.append("<li><a href=\"AlbumInfoServlet?albumId=").append(album.getId_album())
                    .append("\">").append(album.getUsername()).append(" - ").append(album.getTitle()).append("</a></li>");
        }


        return home_page.replace("wait for my albums", myAlbumsHtml.toString())
                .replace("wait for other albums", otherAlbumsHtml.toString());
    }
    static String home_page = "<!DOCTYPE html>\n" +
            "<html lang=\"zh-CN\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Homepage</title>\n" +
            "    <link rel=\"stylesheet\" href=\"homePage.css\"> <!-- Link to the external CSS file -->\n" +
            "    <style>\n" +
            "        /* 模态窗背景 */\n" +
            "        .modal-overlay {\n" +
            "            display: none;\n" +
            "            position: fixed;\n" +
            "            top: 0;\n" +
            "            left: 0;\n" +
            "            width: 100%;\n" +
            "            height: 100%;\n" +
            "            background-color: rgba(0, 0, 0, 0.5);\n" +
            "            z-index: 1000; /* 使其置顶 */\n" +
            "        }\n" +
            "\n" +
            "        /* 模态窗内容 */\n" +
            "        .modal-content {\n" +
            "            position: absolute;\n" +
            "            top: 50%;\n" +
            "            left: 50%;\n" +
            "            transform: translate(-50%, -50%);\n" +
            "            background-color: white;\n" +
            "            padding: 20px;\n" +
            "            border-radius: 8px;\n" +
            "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);\n" +
            "            width: 600px; /* 可以根据需要调整大小 */\n" +
            "            z-index: 1001;\n" +
            "        }\n" +
            "\n" +
            "        /* 关闭按钮 */\n" +
            "        .close-btn {\n" +
            "            background-color: red;\n" +
            "            color: white;\n" +
            "            border: none;\n" +
            "            padding: 5px 10px;\n" +
            "            cursor: pointer;\n" +
            "            float: right;\n" +
            "            text-decoration: none;\n" +
            "        }\n" +
            "\n" +
            "        /* 使用 :target 伪类显示模态窗 */\n" +
            "        #album-modal:target {\n" +
            "            display: block;\n" +
            "        }\n" +
            "\n" +
            "        /* 照片选择区域 */\n" +
            "        .photo-selection {\n" +
            "            display: flex;\n" +
            "            flex-wrap: wrap;\n" +
            "            gap: 10px;\n" +
            "        }\n" +
            "\n" +
            "        .photo-item {\n" +
            "            flex: 1 1 calc(33.333% - 10px); /* 一行三个 */\n" +
            "            box-sizing: border-box;\n" +
            "            margin-bottom: 10px;\n" +
            "            position: relative;\n" +
            "        }\n" +
            "\n" +
            "        .photo-item img {\n" +
            "            width: 100%;\n" +
            "            height: auto;\n" +
            "            object-fit: cover;\n" +
            "            border: 1px solid #ddd;\n" +
            "            border-radius: 4px;\n" +
            "        }\n" +
            "\n" +
            "        .photo-item input {\n" +
            "            position: absolute;\n" +
            "            bottom: 10px;\n" +
            "            left: 10px;\n" +
            "            background: rgba(255, 255, 255, 0.7);\n" +
            "            border: none;\n" +
            "            padding: 5px;\n" +
            "            cursor: pointer;\n" +
            "        }\n" +
            "\n" +
            "        /* 额外样式 */\n" +
            "        .form-group {\n" +
            "            margin-bottom: 15px;\n" +
            "        }\n" +
            "\n" +
            "        .form-group label {\n" +
            "            display: block;\n" +
            "            margin-bottom: 5px;\n" +
            "        }\n" +
            "\n" +
            "        .form-group input,\n" +
            "        .form-group textarea {\n" +
            "            width: 100%;\n" +
            "            padding: 8px;\n" +
            "            box-sizing: border-box;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<p th:if=\"${errorMessage}\" th:text=\"${errorMessage}\" style=\"color: red;\"></p>\n" +
            "<div class=\"container\">\n" +
            "    <div class=\"left-panel\">\n" +
            "        <div class=\"header\">\n" +
            "            <h2>我的相册</h2>\n" +
            "            <a href=\"#album-modal\" class=\"btn-create-group\">➕</a>\n" +
            "\n" +
            "            <a href=\"#modal\" class=\"btn-upload-image\">\n" +
            "                <img src=\"uploadImage.png\" alt=\"Upload Image\">\n" +
            "            </a>\n" +
            "        </div>\n" +
            "        <ul class=\"album-list\">\n" +
            "            wait for my albums\n" +
            "        </ul>\n" +
            "    </div>\n" +
            "    <div class=\"right-panel\">\n" +
            "        <div class=\"header\">\n" +
            "            <h2>其他相册</h2>\n" +
            "        </div>\n" +
            "        <ul class=\"album-list\">\n" +
            "            wait for other albums\n" +
            "        </ul>\n" +
            "    </div>\n" +
            "</div>\n" +
            "\n" +
            "<!-- 增加专辑模态窗结构 -->\n" +
            "<div id=\"album-modal\" class=\"modal-overlay\">\n" +
            "    <div class=\"modal-content\">\n" +
            "        <!-- 关闭按钮 -->\n" +
            "        <a href=\"#\" class=\"close-btn\">关闭</a>\n" +
            "        <!-- 增加专辑表单 -->\n" +
            "        <form method=\"POST\" action=\"/CreateAlbum\" enctype=\"multipart/form-data\">\n" +
            "            <!-- 标题输入 -->\n" +
            "            <div class=\"form-group\">\n" +
            "                <label for=\"album-title\">专辑标题</label>\n" +
            "                <input type=\"text\" id=\"album-title\" name=\"title\" placeholder=\"请输入专辑标题\" required>\n" +
            "            </div>\n" +
            "\n" +
            "            <!-- 描述输入 -->\n" +
            "            <div class=\"form-group\">\n" +
            "                <label for=\"album-description\">专辑描述</label>\n" +
            "                <textarea id=\"album-description\" name=\"description\" placeholder=\"请输入专辑描述\" required></textarea>\n" +
            "            </div>\n" +
            "\n" +
            "            <!-- 选择图片 -->\n" +
            "            <div class=\"form-group\">\n" +
            "                <label for=\"album-images\">选择图片</label>\n" +
            "                <div class=\"photo-selection\">\n" +
            "                    <!-- 通过 Thymeleaf 动态生成照片项 -->\n" +
            "                    <div th:each=\"photo : ${photos}\" class=\"photo-item\">\n" +
            "                        <img th:src=\"@{${photo.url}}\" alt=\"Photo\" />\n" +
            "                        <input type=\"checkbox\" name=\"selectedPhotos\" th:value=\"${photo.id}\"> 选择\n" +
            "                    </div>\n" +
            "                </div>\n" +
            "            </div>\n" +
            "\n" +
            "            <!-- 提交按钮 -->\n" +
            "            <button type=\"submit\">创建专辑</button>\n" +
            "        </form>\n" +
            "    </div>\n" +
            "</div>\n" +
            "\n" +
            "<!-- 上传照片模态窗结构 -->\n" +
            "<div id=\"modal\" class=\"modal-overlay\">\n" +
            "    <div class=\"modal-content\">\n" +
            "        <!-- 关闭按钮 -->\n" +
            "        <a href=\"#\" class=\"close-btn\">关闭</a>\n" +
            "        <!-- 上传照片表单 -->\n" +
            "        <form method=\"POST\" action=\"/Upload\" enctype=\"multipart/form-data\">\n" +
            "            <!-- 标题输入 -->\n" +
            "            <input type=\"text\" name=\"title\" placeholder=\"请输入标题\" required style=\"display: block; margin-bottom: 10px;\">\n" +
            "\n" +
            "            <!-- 描述输入 -->\n" +
            "            <textarea name=\"description\" placeholder=\"请输入描述\" style=\"display: block; margin-bottom: 10px;\"></textarea>\n" +
            "\n" +
            "            <!-- 文件输入 -->\n" +
            "            <input type=\"file\" name=\"file\" required style=\"display: block; margin-bottom: 10px;\">\n" +
            "\n" +
            "            <!-- 提交按钮 -->\n" +
            "            <button type=\"submit\">提交</button>\n" +
            "        </form>\n" +
            "    </div>\n" +
            "</div>\n" +
            "\n" +
            "</body>\n" +
            "</html>\n" ;


/*

    public static String readHTMLFile(String htmlFilePath) throws IOException {
        File htmlFile <!DOCTYPE html>


            = new File(htmlFilePath);
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

    public static String writeTableToHtml(String htmlName, String matrix[][],String error,String hidden,String[] selectedMembers) {

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
            String errorHtml = "<div style=\"position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background-color: white; padding: 20px; border: 1px solid black; box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1); animation: fadeOut 3s forwards;\">" + error + "</div><style>@keyframes fadeOut { 0% { opacity: 1; } 100% { opacity: 0; display: none; } }</style>";


            htmlContent = htmlContent.replace("//wait for error", errorHtml);
        }else{
            htmlContent = htmlContent.replace("//wait for error", "");
        }
        String placeholder = "//wait for table";
        String tableHtml = generateTableHtml(matrix, hidden, selectedMembers); // 生成要插入的表格HTML

        htmlContent = htmlContent.replace(placeholder, tableHtml);

        System.out.println("test> Util htmlContent: " + htmlContent);

        return htmlContent;

    }

    // 生成表格HTML的方法
    private static String generateTableHtml(String[][] matrix,String hidden,String[] selectedMembers) {
        StringBuilder tableHtml = new StringBuilder();

        for (String[] row : matrix) {
            tableHtml.append("<tr>");

            for (int i = 0; i < row.length; i++) {
                if (row[i].equals("checkbox")) {
                    if (row[3].equals(hidden)){
                        i = 4;
                        continue;
                    }
                    if (selectedMembers!= null && Arrays.stream(selectedMembers).anyMatch(s -> s.equals(row[3]))) tableHtml.append("<td><input type=\"checkbox\" name=\"selectedMembers\" value=\"" + row[3] + "\" checked></td>");
                    else tableHtml.append("<td><input type=\"checkbox\" name=\"selectedMembers\" value=\"" + row[3] + "\"></td>");


                }else if (row[1].startsWith("GroupName: ") && row[0].startsWith("Id: ") && i == 1){
                    String groupName = row[1].substring("GroupName: ".length()).trim();
                    int groupId = Integer.parseInt(row[0].substring("Id: ".length()).trim());
                    System.out.println("test> Util>" + groupId + " " + groupName);
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

 */


}