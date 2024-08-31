package controller;

import dao.AlbumDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/GroupInfoServlet")
public class GroupInfoServlet extends HttpServlet {
    AlbumDAO albumDAO;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*
        if (request.getSession().getAttribute("user") == null){
            response.sendRedirect("login.html");
            return;
        }
        User user = (User) request.getSession().getAttribute("user");
        int groupId = Integer.parseInt(request.getParameter("groupId"));
        try {
            groupDAO = new GroupDAO(ConnectionHandler.getConnection(getServletContext()));
            try {
              Group  group = groupDAO.getGroupByID(groupId);

                response.setContentType("text/html");
                response.setCharacterEncoding("UTF-8");

                if (group != null) {
                    if (!(group.getMembers().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))||group.getCreator().equals(user.getEmail()))||!groupDAO.isGroupValid(groupId)){
                        return;
                    }
                    response.getWriter().println("<h1>Group: " + group.getTitle() + "</h1>");
                    response.getWriter().println("<h1>creator email: " + group.getCreator()  + "</h1>");
                    response.getWriter().println("<h2>Members: </h2>");

                    response.getWriter().println("<table border=\"1\">");
                    response.getWriter().println("<tr><th>Name</th><th>Surname</th><th>Email</th></tr>");

                    for (User member : group.getMembers()) {
                        response.getWriter().println("<tr>");
                        response.getWriter().println("<td>" + member.getUsername() + "</td>");
                        response.getWriter().println("<td>" + member.getSurname() + "</td>");
                        response.getWriter().println("<td>" + member.getEmail() + "</td>");
                        response.getWriter().println("</tr>");
                    }

                    response.getWriter().println("</table>");
                } else {
                    return;
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
                throw new RuntimeException(e);
            }

         */
        }


    }

