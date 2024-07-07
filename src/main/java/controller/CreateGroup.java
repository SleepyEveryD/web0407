package controller;
import beans.Group;
import beans.User;
import dao.GroupDAO;
import dao.UserDAO;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import utils.ConnectionHandler;
import utils.Util;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/createGroup")
public class CreateGroup extends HttpServlet {
    ArrayList<String> invitedUsers = null;
    int attempts = 0;
    TemplateEngine templateEngine;
    boolean selectingMembers = false;
    String selectMembersError = "";

    @Override
    public void init() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(getServletContext());
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        WebContext webContext = new WebContext(request, response, getServletContext(), request.getLocale());

        User creatorUser = (User) request.getSession().getAttribute("user");
        String creatorEmail = creatorUser.getEmail();
        System.out.println(creatorEmail);

        String actionType = request.getParameter("action");
        if (actionType.equals("selectMembers")){
            System.out.println("test> CreateGroup> selectMember");
            String[] selectedUsers = request.getParameterValues("selectedMembers");

            System.out.println("test> CreateGroup> selectMember selectedUsers: " + selectedUsers.length);
            Group group =(Group)request.getSession().getAttribute("group");
            int minMembers = group.getMinPeople();
            int maxMembers = group.getMaxPeople();
            if (selectedUsers == null || selectedUsers.length < minMembers || selectedUsers.length > maxMembers){
                attempts ++;
                if (attempts == 3){
                    response.sendRedirect("create_failure.html");
                    return;
                }
                    selectMembersError = "Please select at least " + minMembers + " and at most " + maxMembers + " members. You have "+( 3-attempts)+ " attempts left.";
                response.sendRedirect("createGroup");
            }else{
                ArrayList<String> selectedUsersList = new ArrayList<>(Arrays.asList(selectedUsers));

                try {
                    GroupDAO groupDAO = new GroupDAO(ConnectionHandler.getConnection(getServletContext()));
                    int groupID = groupDAO.registerGroup(group.getTitle(),creatorEmail, group.getMaxPeople(),group.getMinPeople(), group.getDuration(),selectedUsersList );
                    if (groupID!= 0) group.setGroupId(groupID);
                    selectingMembers = false;
                    attempts = 0;
                    invitedUsers = null;
                    response.sendRedirect("myActivity");
                } catch (IllegalAccessException | SQLException e) {
                    throw new RuntimeException(e);
                }

            }


        }else if (actionType.equals("setGroupInfo")){
            String groupName = request.getParameter("groupName");
            int minMembers = Integer.parseInt(request.getParameter("minMembers"));
            int maxMembers = Integer.parseInt(request.getParameter("maxMembers"));
            int duration = Integer.parseInt(request.getParameter("duration"));
            System.out.println("test> CreateGroup> setGroupInfo minMembers: " + minMembers + " maxMembers: " + maxMembers);
            Group group = new Group(groupName, creatorUser, minMembers, maxMembers,duration);
            session.setAttribute("group", group);
            if (minMembers> maxMembers){
                request.getSession().setAttribute("createGroupError", "max num of members can not be smaller than min num of members.");
                templateEngine.process("createGroup.html", webContext, response.getWriter());

            }else {
                session.setAttribute("actionType","selectMember");
                System.out.println("test> attribute actionType set to selectMember");
                invitedUsers = new ArrayList<>();
                selectingMembers = true;
                response.sendRedirect("createGroup");

            }

        }


    }
    //get all users and display them in invitations.html
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null||!MainController.getOnlineUsers().stream().anyMatch(c-> c.getEmail().equals(user.getEmail()))){
            request.getSession().setAttribute("loginError", "You have not logined in.");
            response.sendRedirect("login");
        }else{
            String actionType = request.getParameter("action");
            if (actionType!= null && actionType.equals("close")){
                attempts = 0;
                selectingMembers =false;
                invitedUsers = null;
            }
            if (!selectingMembers)  response.sendRedirect("createGroup.html");
            else{
                String html = Util.selectMembersStatic;
                System.out.println(html);
                ArrayList<User> users = null;
                try {
                    UserDAO userDAO = new UserDAO(ConnectionHandler.getConnection(getServletContext()));
                    users = userDAO.getAllUser();
                    System.out.println("test> user size: " + users.size());
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }

                String [][] matrix   = new String[users.size()][4];
                for(int i=0;i< users.size();i++){
                    matrix[i][0] = "checkbox";
                    matrix[i][2] = users.get(i).getName();
                    matrix[i][1] = users.get(i).getSurname();
                    matrix[i][3] = users.get(i).getEmail();
                }

                String htmlContent = Util.writeTableToHtml("createGroup", matrix,selectMembersError, user.getEmail());
                request.getSession().setAttribute("actionType","selectMember");

                response.setContentType("text/html;charset=UTF-8");

                // 获取输出流
                PrintWriter out = response.getWriter();

                // 将生成的HTML字符串写入响应
                out.println(htmlContent);

                // 关闭输出流
                out.close();

            }


        }



    }



    
}
