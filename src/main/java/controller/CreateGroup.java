package controller;
/*

@WebServlet("/createGroup")
public class CreateGroup extends HttpServlet {
    String[] invitedUsers = null;
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

            if (actionType != null &&actionType.equals("selectMembers")){
                System.out.println("test> CreateGroup> selectMember");
                invitedUsers = request.getParameterValues("selectedMembers");

                System.out.println("test> CreateGroup> selectMember invitedUsers: " + invitedUsers.length);
                Group group =(Group)request.getSession().getAttribute("group");
                int minMembers = 0;
                int maxMembers = 0;
                try {
                     minMembers = group.getMinPeople();
                     maxMembers = group.getMaxPeople();
                    if (minMembers < 0 || maxMembers < 0 || maxMembers < minMembers) {
                        throw new IllegalArgumentException("minMmbers and maxMembers can not be negativ");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (invitedUsers == null || invitedUsers.length < minMembers || invitedUsers.length > maxMembers ){
                    attempts ++;
                    if (attempts >= 3){
                        response.sendRedirect("create_failure.html");
                        selectingMembers = false;
                        attempts = 0;
                        invitedUsers = null;
                        return;
                    }
                    if (minMembers> invitedUsers.length)  selectMembersError = " Please select " +(minMembers-invitedUsers.length) + " more. You have "+( 3-attempts)+ " attempts left.";
                    if (maxMembers < invitedUsers.length) selectMembersError = " Please select " +(invitedUsers.length-maxMembers) + " less. You have "+( 3-attempts)+ " attempts left.";
                    response.sendRedirect("createGroup");
                }else{
                    ArrayList<String> selectedUsersList = new ArrayList<>(Arrays.asList(invitedUsers));

                    try {
                        GroupDAO groupDAO = new GroupDAO(ConnectionHandler.getConnection(getServletContext()));

                        int groupID = groupDAO.registerGroup(group.getTitle(),creatorEmail, group.getMaxPeople(),group.getMinPeople(), group.getDuration(),selectedUsersList );
                        if (groupID!= 0) group.setGroupId(groupID);
                        selectingMembers = false;
                        attempts = 0;
                        this.invitedUsers = null;
                        request.setAttribute("action","setGroupInfo");
                        response.sendRedirect("myActivity");
                    } catch (IllegalAccessException | SQLException e) {
                        throw new RuntimeException(e);
                    }

                }


            }else if (actionType != null &&actionType.equals("setGroupInfo")){

                String groupName = request.getParameter("groupName");
                Group group =(Group)request.getSession().getAttribute("group");
                int minMembers = 0;
                int maxMembers = 0;
                int duration = 0;
                try {
                    minMembers = group.getMinPeople();
                    maxMembers = group.getMaxPeople();
                    if (minMembers < 0 || maxMembers < 0 || maxMembers < minMembers) {
                        throw new IllegalArgumentException("minMmbers and maxMembers can not be negativ");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                 minMembers = Integer.parseInt(request.getParameter("minMembers"));
                 maxMembers = Integer.parseInt(request.getParameter("maxMembers"));
                 duration = Integer.parseInt(request.getParameter("duration"));

                System.out.println("test> CreateGroup> setGroupInfo minMembers: " + minMembers + " maxMembers: " + maxMembers);
                 group = new Group(groupName, creatorUser, minMembers, maxMembers,duration);
                session.setAttribute("group", group);
                if (minMembers> maxMembers||duration<0){
                    request.getSession().setAttribute("createGroupError", "max num of members can not be smaller than min num of members. or durantion must be more than 0 days");
                    templateEngine.process("createGroup.html", webContext, response.getWriter());

                }else {
                    session.setAttribute("actionType","selectMember");
                    System.out.println("test> attribute actionType set to selectMember");
                    invitedUsers = null;
                    selectingMembers = true;
                    attempts = 0;
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
                selectMembersError = "";
            }

            if (!selectingMembers) {
                response.sendRedirect("createGroup.html");
            }
            else{
                if (attempts == 0) selectMembersError = null;
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
                    matrix[i][2] = users.get(i).getUsername();
                    matrix[i][3] = users.get(i).getEmail();
                }
                Arrays.sort(matrix, Comparator.comparing(row -> row[1]));

                String htmlContent = Util.writeTableToHtml("createGroup", matrix,selectMembersError, user.getEmail(),invitedUsers);
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

 */
