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


@WebServlet("/GroupMember")
public class GroupMember extends HttpServlet {

    private static JSONArray queryResult = null;
    private static QueryBuilder queryBuilder = null;
    static {
        try {
            queryResult = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queryBuilder = new QueryBuilder("groupmember");
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
        String creatorId=request.getParameter("creator_id");
        String userId=(String)session.getAttribute("user_id");
        String user=(String)session.getAttribute("username");
        String createTime=(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date());

        queryBuilder.clear();
        queryBuilder.set("groupId",Integer.parseInt(groupId));
        queryBuilder.set("creatorId",Integer.parseInt(creatorId));
        queryBuilder.set("userId",Integer.parseInt(userId));
        queryBuilder.set("user",user);
        queryBuilder.set("createTime",createTime);
        queryBuilder.set("grades",0);

        String sql=queryBuilder.getInsertStmt();
        DatabaseHelper db = new DatabaseHelper();
        db.execute(sql);
        response.sendRedirect("groupdetails/member/list.jsp");
    }
    private void getRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        String userId=request.getParameter("user_id");
        String groupId=request.getParameter("group_id");
        String orderBy=request.getParameter("orderby");
        if(session.getAttribute("exist_result")==null || !(boolean)session.getAttribute("exist_result"))
        {
            System.out.println("getResult exist_result=false or null");
            queryBuilder.clear();
            if(userId!=null){
                queryBuilder.set("userId",Integer.parseInt(userId));
            }
            if(groupId!=null){
                queryBuilder.set("groupId",Integer.parseInt(groupId));
            }
            queryBuilder.set("orderBy",request.getParameter("orderby"));

            String sql=queryBuilder.getSelectStmt();
            DatabaseHelper db=new DatabaseHelper();
            ResultSet rs=db.executeQuery(sql);
            processResult(request,rs);
            session.setAttribute("exist_result", false);
        }
        out.print(queryResult);
        session.setAttribute("queryResult",queryResult);
        out.flush();
        out.close();
        System.out.println("exit group_member getResult");
    }
    private void deleteRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {

    }
    private void modifyRecord(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{

    }
    private void getStatistics(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException {

    }


    private void processResult(HttpServletRequest request,ResultSet rs) throws JSONException, SQLException {
        HttpSession session = request.getSession();
        int user_id=Integer.parseInt(session.getAttribute("id").toString());
//        int auth=Integer.parseInt(session.getAttribute("auth").toString());
        int auth=1;
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
