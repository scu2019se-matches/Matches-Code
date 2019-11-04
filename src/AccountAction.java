
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.DatabaseHelper;
import util.MD5Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by silenus on 2019/4/29.
 */

import util.QueryBuilder;

@WebServlet("/AccountAction")
public class AccountAction extends HttpServlet
{
    private QueryBuilder queryBuilder = new QueryBuilder("user");

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        System.out.println("AccountAction action="+action);
        try
        {
            switch(action)
            {
                case "login":
                    login(request, response);
                    break;
//                case "logout":
//                    logout(request, response);
//                    break;
                case "register":
                    register(request, response);
                    break;
//                case "getmenu":
//                    getMenu(request, response);
//                    break;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        System.out.println("enter AccountAction.login");

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        session.removeAttribute("id");
        session.removeAttribute("username");
        session.removeAttribute("email");
        session.removeAttribute("login_time");
        session.removeAttribute("check");

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if(email==null || password==null || email.length()==0 || password.length()==0)
        {
            session.setAttribute("login_errno", 1);
            session.setAttribute("login_msg", "邮箱或密码不能为空");
            response.sendRedirect("login.jsp");
            return;
        }
        if(!Pattern.matches("[_0-9A-Za-z]+@[_0-9A-Za-z]+(\\.com)?", email))
        {
            session.setAttribute("login_errno", 2);
            session.setAttribute("login_msg", "邮箱格式不正确");
            response.sendRedirect("login.jsp");
            return;
        }

        ResultSet rs=null;
        password = MD5Util.MD5(password);
        queryBuilder.clear();
        queryBuilder.set("email", email);
        queryBuilder.set("password", password);

        try(DatabaseHelper db = new DatabaseHelper()){
            String sql=queryBuilder.getSelectStmt();
            rs = db.executeQuery(sql);
            if(!rs.next()) {
                session.setAttribute("login_errno", 3);
                session.setAttribute("login_msg", "邮箱或密码错误");
                response.sendRedirect("login.jsp");
                return;
            }
            session.setAttribute("id", Integer.toString(rs.getInt("id")));
            session.setAttribute("username", rs.getString("username"));
            session.setAttribute("email", rs.getInt("email"));
            session.setAttribute("login_time", System.currentTimeMillis());
            session.setAttribute("check", 1L); // reserved
            session.setAttribute("login_errno", 0);
        }

        response.sendRedirect("index.jsp");
    }

//    private void logout(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
//        System.out.println("enter AccountAction.logout");
//        HttpSession session = request.getSession();
//
//        session.removeAttribute("guid");
//        session.removeAttribute("username");
//        session.removeAttribute("login_time");
//        session.removeAttribute("authorization");
//        session.removeAttribute("check");
//        response.sendRedirect("login.jsp");
//    }

    private void register(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, ServletException, SQLException {
        System.out.println("enter AccountAction.register");

        request.setCharacterEncoding("UTF-8");

        QueryBuilder q = new QueryBuilder("user");
        JSONObject res = new JSONObject();
        boolean flag = true;

        q.set("email", request.getParameter("email"));

        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs = db.executeQuery(q.getSelectStmt());
            if(rs.next())
            {
                res.put("register_errno", 2);
                res.put("register_msg", String.format("邮箱\"%s\"已经注册", q.get("email")));
                flag = false;
            }
            if(flag)
            {
                q.set("username", request.getParameter("username"));
                q.set("fullname", request.getParameter("fullname"));
                q.set("gender", Integer.parseInt(request.getParameter("gender")));
                String password = request.getParameter("password");
                q.set("password", MD5Util.MD5(password));
                db.execute(q.getInsertStmt());
                res.put("register_errno", 0);
            }
        }

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(res);
        out.flush();
        out.close();
    }

//    private void getMenu(HttpServletRequest request, HttpServletResponse response) throws SQLException, JSONException, IOException {
//        System.out.println("enter AccountAction.getMenu");
//
//        HttpSession session = request.getSession();
//        DatabaseHelper db=new DatabaseHelper();
//
//        if(session.getAttribute("guid")==null)
//        {
//            System.out.println("Authorization.getMenu: error: not login");
//            return;
//        }
//        int auth=(int)session.getAttribute("authorization");
//        System.out.printf("auth = %d\n", auth);
//
//        String sql=String.format("select * from `menu_tree` where authorization&%d>0", auth);
//        System.out.println(sql);
//        ResultSet rs2 =db.executeQuery(sql);
//        JSONArray menu=new JSONArray();
//        while(rs2.next())
//        {
//            if(rs2.getString("parent")==null)
//            {
//                JSONObject header=new JSONObject();
//                header.put("title", rs2.getString("title"));
//                header.put("sub", new JSONArray());
//                menu.put(rs2.getInt("id"), header);
//            }
//        }
//        rs2.beforeFirst();
//        while(rs2.next())
//        {
//            if(rs2.getString("parent")!=null)
//            {
//                JSONObject item=new JSONObject();
//                int parent = rs2.getInt("parent");
//                item.put("title", rs2.getString("title"));
//                item.put("href", rs2.getString("href").length()>0 ? rs2.getString("href") : "#");
//                ((JSONArray)(((JSONObject)(menu.get(parent))).get("sub"))).put(item);
//            }
//        }
//
//        response.setContentType("application/json; charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        out.print(menu);
//        out.flush();
//        out.close();
//
//        System.out.println("exit AccountAction.getMenu");
//    }

}
