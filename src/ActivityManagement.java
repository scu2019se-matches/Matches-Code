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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@WebServlet("/ActivityManagement")
public class ActivityManagement extends HttpServlet {

    private static JSONArray queryResult = null;
    private static QueryBuilder ActivityView = null;
    private static QueryBuilder ActivityTable = null;
    private static QueryBuilder FocusTable = null;
    private static QueryBuilder CommentView = null;
    static {
        try {
            queryResult = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ActivityView = new QueryBuilder("activitylist");
        CommentView = new QueryBuilder("commentlist");
        ActivityTable = new QueryBuilder("activity");
        FocusTable = new QueryBuilder("focus");
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
//                case "get_detail":
//                     getStatistics(request, response);
//                    break;
                case "get_comment":
//                    getStatistics(request, response);
                    break;
                case "focus_record":
                    focusAct(request, response);
                    break;
                default:
                    System.out.println("activity: invalid action: "+action);
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
            response.sendRedirect("activity/list.jsp");
        }
    }
    private void getRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        System.out.println("enter activity getResult");
        ActivityView.clear();
        String activityId=request.getParameter("activity_id");
        if(activityId!=null){
            ActivityView.set("id",Integer.parseInt(activityId));
        }
        String sql= ActivityView.getSelectStmt();
        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs=db.executeQuery(sql);
            processResult(request,rs);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        out.print(queryResult);
        session.setAttribute("queryResult",queryResult);
        out.flush();
        out.close();
        System.out.println("exit activity getResult");
    }
    private void focusAct(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        int userId=Integer.parseInt(session.getAttribute("id").toString());
        int activityId=Integer.parseInt(request.getParameter("activity_id"));
        FocusTable.clear();
        FocusTable.set("focusUser",userId);
        FocusTable.set("focusActivity",activityId);
        try(DatabaseHelper db = new DatabaseHelper()){
            db.execute(FocusTable.getInsertStmt());
        }
    }
    private void deleteRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");	//设置编码
        try(DatabaseHelper db = new DatabaseHelper()){
            String activityId=request.getParameter("activity_id");
            ActivityTable.clear();
            ActivityTable.set("id",Integer.parseInt(activityId));
            String sql=ActivityTable.getDeleteStmt();
            db.execute(sql);
//            response.sendRedirect("group/list.jsp");
        }
    }
    private void modifyRecord(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");	//设置编码
        String activityId=request.getParameter("activity_id");
        String headline=request.getParameter("headline");
        String site=request.getParameter("site");
        String beginTime=request.getParameter("begin_time");
        String endTime=request.getParameter("end_time");
        String tag=request.getParameter("tag");
        String description=request.getParameter("description");
        try(DatabaseHelper db = new DatabaseHelper()){
            ActivityTable.clear();
            ActivityTable.set("id",Integer.parseInt(activityId));
            ActivityTable.set("site",new String(site.getBytes("iso-8859-1"),"utf-8"));
            ActivityTable.set("headline",new String(headline.getBytes("iso-8859-1"),"utf-8"));
            ActivityTable.set("beginTime",beginTime);
            ActivityTable.set("endTime",endTime);
            ActivityTable.set("tag",new String(tag.getBytes("iso-8859-1"),"utf-8"));
            ActivityTable.set("description",new String(description.getBytes("iso-8859-1"),"utf-8"));
            String sql=ActivityTable.getUpdateStmt();
            db.execute(sql);
            response.sendRedirect("activity/list.jsp");
        }
    }
    private void processResult(HttpServletRequest request,ResultSet rs) throws JSONException, SQLException, ParseException {
        HttpSession session = request.getSession();
        int user_id=Integer.parseInt(session.getAttribute("id").toString());
        int auth=Integer.parseInt(session.getAttribute("auth")==null?"0":session.getAttribute("auth").toString());
        queryResult = new JSONArray("[]");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String queryTime=dateFormat.format(new Date());
        DatabaseHelper db = new DatabaseHelper();
        ResultSet  finished=null;

        rs.beforeFirst();
        while(rs.next())
        {
            JSONObject item = new JSONObject();
            item.put("id", rs.getInt("id"));
            item.put("headline", rs.getString("headline"));
            item.put("publisher_id", rs.getInt("publisherId"));
            item.put("create_time", rs.getString("createTime"));
            item.put("site", rs.getString("site"));
            item.put("publisher", rs.getString("publisher"));
            item.put("begin_time", rs.getString("BeginTime"));
            item.put("end_time", rs.getString("EndTime"));
            item.put("tag", rs.getString("tag"));
            item.put("imageUrl", rs.getString("imageUrl"));
            item.put("description", rs.getString("description"));
            if(auth>1||rs.getInt("publisherId")==user_id){
                item.put("auth", 1);
            }else{
                item.put("auth", 0);
            }

            //活动状态:0未开始,1进行中,2已结束
            int act_status=0,focus_status=1;
            if(dateFormat.parse(rs.getString("EndTime")).compareTo(dateFormat.parse(queryTime))<0){
                act_status=2;
            }else if(dateFormat.parse(rs.getString("BeginTime")).compareTo(dateFormat.parse(queryTime))<0){
                act_status=1;
            }

            //关注状态:0未关注,1已关注
            FocusTable.clear();
            FocusTable.set("focusActivity",rs.getInt("id"));
            FocusTable.set("focusUser",user_id);
            finished= db.executeQuery(FocusTable.getSelectStmt());
            if(!finished.next()){
                focus_status=0;
            }
            item.put("act_status", act_status);
            item.put("focus_status", focus_status);
            queryResult.put(item);
        }
    }
}
