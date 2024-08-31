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


    private static String header ="<nav class=\"h-navbar\">\n" +
            "    <div class=\"h-navbar-container h-container\">\n" +
            "\n" +
            "        <ul class=\"h-menu-items\">\n" +
            "            <li><a href=\"index.html\">Home</a></li>\n" +
            "            <li><a href=\"myActivity\">My activity</a></li>\n" +
            "            <li><a href=\"createGroup\">New Activity</a></li>\n" +
            "            <li><a href=\"logout\">Log Out </a></li>\n" +
            "\n" +
            "        </ul>\n" +
            "        <h1 class=\"h-logo\">PA</h1>\n" +
            "    </div>\n" +
            "</nav>";
    private static String home_page= "<!DOCTYPE html>\n" +
            "<html lang=\"zh-CN\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Homepage</title>\n" +
            "    <link rel=\"stylesheet\" href=\"homePage.css\"> <!-- Link to the external CSS file -->\n" +
            "</head>\n" +
            "<body>\n" +
            "<div class=\"container\">\n" +
            "    <div class=\"left-panel\">\n" +
            "        <div class=\"header\">\n" +
            "            <h2>我的相册</h2>\n" +
            "            <a href=\"#\" class=\"btn-create-group\">➕</a>\n" +
            "            <!-- Form for file upload -->\n" +
            "            <form method=\"POST\" action=\"/upload\" enctype=\"multipart/form-data\" style=\"display: inline-block;\">\n" +
            "                <!-- File input button -->\n" +
            "                <label for=\"fileInput\" class=\"btn-upload-image\" style=\"cursor: pointer;\">\n" +
            "                    <img src=\"uploadImage.png\" alt=\"Upload Image\">\n" +
            "                </label>\n" +
            "                <!-- Hidden file input -->\n" +
            "                <input type=\"file\" id=\"fileInput\" name=\"file\" style=\"display: none;\" required onchange=\"this.form.submit()\">\n" +
            "            </form>\n" +
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
            "</body>\n" +
            "</html>\n";


/*

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