/**
 * Created by Janspiry on 2019/11/7.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import util.DatabaseHelper;
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


@WebServlet("/GroupMember")
public class GroupMember extends HttpServlet {

    private static JSONArray queryResult = null;
    private static QueryBuilder GroupMemberView = null;
    private static QueryBuilder GroupTable = null;
    private static QueryBuilder GroupMemberTable = null;
    static {
        try {
            queryResult = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        GroupMemberView = new QueryBuilder("groupmemberlist");
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
                case "getValid":
                    getValid(request, response);
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

        String groupId=request.getParameter("group_id");
        String userId=session.getAttribute("id").toString();
        String createTime=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());

        GroupMemberTable.clear();
        GroupMemberTable.set("groupId",Integer.parseInt(groupId));
        GroupMemberTable.set("userId",Integer.parseInt(userId));
        GroupMemberTable.set("createTime",createTime);
        GroupMemberTable.set("grades",0);

        String sql= GroupMemberTable.getInsertStmt();
        try(DatabaseHelper db = new DatabaseHelper()){
            db.execute(sql);
            response.sendRedirect("groupdetails/member/list.jsp");
        }
    }
    private void getRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        String userId=request.getParameter("user_id");
        String user=request.getParameter("user");
        String groupId=request.getParameter("group_id");
        String orderBy=request.getParameter("orderby");

        System.out.println("enter group_member getResult");
        GroupMemberView.clear();
        if(userId!=null){
            GroupMemberView.set("userId",Integer.parseInt(userId));
        }
        if(groupId!=null){
            GroupMemberView.set("groupId",Integer.parseInt(groupId));
        }
        GroupMemberView.set("user",user,1);
        GroupMemberView.set("orderBy",orderBy);

        String sql= GroupMemberView.getSelectStmt();
        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs=db.executeQuery(sql);
            processResult(request,rs);
            out.print(queryResult);
            out.flush();
            out.close();
            session.setAttribute("queryResult",queryResult);
            System.out.println("exit group_member getResult");
        }
    }
    private void deleteRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");	//设置编码
        try(DatabaseHelper db = new DatabaseHelper()){
            String groupId=request.getParameter("group_id");
            String memberId=request.getParameter("member_id");
            String sql="delete from `groupmember` where `groupId`="+groupId+" and userId="+memberId;
            db.execute(sql);
        }
    }
    private void modifyRecord(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{

    }
    private void getStatistics(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException {

    }
    private void getValid(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        int groupId = Integer.parseInt(request.getParameter("groupId"));
        int userId=Integer.parseInt(session.getAttribute("id").toString());
        GroupMemberTable.clear();
        GroupMemberTable.set("groupId",groupId);
        GroupMemberTable.set("userId",userId);
        String sql = GroupMemberTable.getSelectStmt();
        JSONObject res = new JSONObject();
        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs = db.executeQuery(sql);
            if(rs.next()){
                res.put("errno", 0);
            }else{
                res.put("errno", 1);
            }
            sql="SELECT auth from `user` WHERE id="+userId;
            rs = db.executeQuery(sql);
            if(rs.next()){
                res.put("auth", rs.getInt("auth"));
            }else{
                res.put("auth", 0);
            }
        }
        out.print(res);
        out.flush();
        out.close();
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
            item.put("group_id", rs.getInt("groupId"));
            item.put("creator_id", rs.getInt("creatorId"));
            item.put("user_id", rs.getInt("userId"));
            item.put("user", rs.getString("user"));
            item.put("create_time", rs.getString("createTime"));
            item.put("grades", rs.getInt("grades"));
            item.put("commodity", rs.getInt("commodity"));
            if((auth>1||rs.getInt("creatorId")==user_id)&&rs.getInt("creatorId")!=rs.getInt("userId")){
                item.put("delauth", 1);
            }else{
                item.put("delauth", 0);
            }
            if(auth>1||rs.getInt("creatorId")==user_id||rs.getInt("userId")==user_id){
                item.put("enterauth", 1);
            }else{
                item.put("enterauth", 0);
            }
//            item.put("user_id", user_id);
            queryResult.put(item);
        }
    }
}
