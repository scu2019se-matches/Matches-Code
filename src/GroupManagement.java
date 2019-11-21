/**
 * Created by Janspiry on 2019/11/7.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.DatabaseHelper;
import util.MD5Util;
import util.QueryBuilder;

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
import java.text.SimpleDateFormat;
import java.util.Date;


@WebServlet("/GroupManagement")
public class GroupManagement extends HttpServlet {

    private static JSONArray queryResult = null;
    private static QueryBuilder GroupView = null;
    private static QueryBuilder GroupTable = null;
    private static QueryBuilder GroupMemberTable = null;
    static {
        try {
            queryResult = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GroupView = new QueryBuilder("grouplist");
        GroupTable = new QueryBuilder("group");
        GroupMemberTable = new QueryBuilder("groupmember");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String action = request.getParameter("action");
        try
        {
            switch(action)
            {
                case "get_record":
                    getRecord(request, response);
                    break;
                case "add_record":
                    addRecord(request, response);
                    break;
                case "delete_record":
                    deleteRecord(request, response);
                    break;
                case "modify_record":
                    modifyRecord(request, response);
                    break;
                case "getStatistics":
                    getStatistics(request, response);
                    break;
                case "get_mygroup":
                    getMyGroup(request, response);
                    break;
                default:
                    System.out.println("group: invalid action: "+action);
                    break;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void addRecord(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");	//设置编码
        try(DatabaseHelper db = new DatabaseHelper()){
            String sql="";

            //增加组
            String title=request.getParameter("title");
            String password=request.getParameter("password");
            String creatorId=session.getAttribute("id").toString();
            String createTime=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());

            ResultSet rs = db.executeQuery("select max(id) from `group`");
            int groupId=1;
            if(rs.next()){
                groupId = rs.getInt(1)+1;
            }

            GroupTable.clear();
            GroupTable.set("id",groupId);
            GroupTable.set("title",new String(title.getBytes("iso-8859-1"),"utf-8"));
            GroupTable.set("password",MD5Util.MD5(password));
            GroupTable.set("creatorId",Integer.parseInt(creatorId));
            GroupTable.set("createTime",createTime);

            sql= GroupTable.getInsertStmt();
            db.execute(sql);

            //组内成员添加组长记录
            GroupMemberTable.clear();
            GroupMemberTable.set("groupId",groupId);
            GroupMemberTable.set("userId",Integer.parseInt(creatorId));
            GroupMemberTable.set("createTime",createTime);
            GroupMemberTable.set("grades",0);

            sql= GroupMemberTable.getInsertStmt();
            db.execute(sql);
            response.sendRedirect("group/list.jsp");
        }
    }
    private void getRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();


        String id=request.getParameter("group_id");
        String creatorId=request.getParameter("creator_id");
        String password=request.getParameter("password");
        String title=request.getParameter("title");
        String creator=request.getParameter("creator");
        String orderBy=request.getParameter("orderby");

        System.out.println("enter group getResult");
        GroupView.clear();
        if(id!=null){
            GroupView.set("id",Integer.parseInt(id));
        }
        if(password!=null){
            GroupView.set("password",MD5Util.MD5(password));
        }
        if(creatorId!=null){
            GroupView.set("creatorId",Integer.parseInt(creatorId));
        }
        GroupView.set("title",title,1);
        GroupView.set("creator",creator,1);
        GroupView.set("orderBy",orderBy);

        String sql= GroupView.getSelectStmt();

        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs=db.executeQuery(sql);
            processResult(request,rs);
        }

        for(int i=0;i<queryResult.length();i++)
        {
            System.out.printf("queryResult[%d] id=%d title=%s creatorId=%d \n",i,
                    queryResult.getJSONObject(i).getInt("id"),
                    queryResult.getJSONObject(i).getString("title"),
                    queryResult.getJSONObject(i).getInt("creator_id"));
        }
        out.print(queryResult);
        session.setAttribute("queryResult",queryResult);
        out.flush();
        out.close();
        System.out.println("exit group getResult");
    }
    private void getMyGroup(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        int userId=Integer.parseInt(session.getAttribute("id").toString());
        String sql="SELECT * from `grouplist` where id in ("
                +" SELECT groupId from `groupmember` WHERE userId="+userId+")";
        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs=db.executeQuery(sql);
            processResult(request,rs);
        }

        for(int i=0;i<queryResult.length();i++)
        {
            System.out.printf("queryResult[%d] id=%d title=%s creatorId=%d \n",i,
                    queryResult.getJSONObject(i).getInt("id"),
                    queryResult.getJSONObject(i).getString("title"),
                    queryResult.getJSONObject(i).getInt("creator_id"));
        }
        out.print(queryResult);
        session.setAttribute("queryResult",queryResult);
        out.flush();
        out.close();
        System.out.println("exit group getResult");
    }
    private void deleteRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");	//设置编码
        try(DatabaseHelper db = new DatabaseHelper()){
            String groupId=request.getParameter("group_id");
            GroupTable.clear();
            GroupTable.set("id",Integer.parseInt(groupId));
            String sql=GroupTable.getDeleteStmt();
            db.execute(sql);
//            response.sendRedirect("group/list.jsp");
        }
    }
    private void modifyRecord(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");	//设置编码
        String groupId=request.getParameter("group_id");
        String title=request.getParameter("title");
        try(DatabaseHelper db = new DatabaseHelper()){
            GroupTable.clear();
            GroupTable.set("id",Integer.parseInt(groupId));
            GroupTable.set("title",title);
            String sql=GroupTable.getUpdateStmt();
            db.execute(sql);
//            response.sendRedirect("group/list.jsp");
        }
    }
    private void getStatistics(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException {

    }


    private void processResult(HttpServletRequest request,ResultSet rs) throws JSONException, SQLException {
        HttpSession session = request.getSession();
        int user_id=Integer.parseInt(session.getAttribute("id").toString());
        int auth=Integer.parseInt(session.getAttribute("auth")==null?"0":session.getAttribute("auth").toString());
        queryResult = new JSONArray("[]");
        rs.beforeFirst();
        while(rs.next())
        {
            JSONObject item = new JSONObject();
            item.put("id", rs.getInt("id"));
            item.put("title", rs.getString("title"));
            item.put("creator_id", rs.getInt("creatorId"));
            item.put("creator", rs.getString("creator"));
            item.put("create_time", rs.getString("createTime"));
            item.put("password", rs.getString("password"));
            item.put("user_number", rs.getInt("userNumber"));
            if(auth>1||rs.getInt("creatorId")==user_id){
                item.put("auth", 1);
            }else{
                item.put("auth", 0);
            }
//            item.put("user_id", user_id);
            queryResult.put(item);
        }
    }
}
