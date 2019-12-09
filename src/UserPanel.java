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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@WebServlet("/UserPanel")
public class UserPanel extends HttpServlet {

    private static JSONArray queryResult = null;
    private static QueryBuilder Task = null;
    private static QueryBuilder TaskHistoryTable = null;
    private static QueryBuilder MemberRecordTable = null;
    private static QueryBuilder GroupMemberTable = null;
    private static QueryBuilder FocusView = null;
    static {
        try {
            queryResult = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Task = new QueryBuilder("task");
        TaskHistoryTable = new QueryBuilder("taskhistory");
        MemberRecordTable = new QueryBuilder("memberrecord");
        GroupMemberTable = new QueryBuilder("groupmember");
        FocusView = new QueryBuilder("focuslist");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String action = request.getParameter("action");
        try
        {
            switch(action)
            {
                case "get_task":
                    getTask(request, response);
                    break;
                case "finish_task":
                    finishTask(request, response);
                    break;
                case "get_activity":
                    getActivity(request, response);
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
    private void getTask(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException, ParseException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        String userId=request.getParameter("user_id");
        System.out.println("enter userpanel_task getResult");
        String sql="select * from tasklist where groupId in" +
                "(select groupId from groupmemberlist where userId=" +userId+ ") "+
                "order by createTime desc";
        DatabaseHelper db=new DatabaseHelper();
        ResultSet rs=db.executeQuery(sql);
        processTask(request,rs);
        session.setAttribute("exist_result", false);
        db.close();
        out.print(queryResult);
        session.setAttribute("queryResult",queryResult);
        out.flush();
        out.close();
        System.out.println("exit userpanel_task getResult");
    }

    private void finishTask(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");	//设置编码
        DatabaseHelper db = new DatabaseHelper();
        String sql="";

        //增加积分
        String groupId=request.getParameter("group_id");
        String userId=request.getParameter("user_id");
        String taskId=request.getParameter("task_id");
        String createTime=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        //  查询任务详情
        sql="select * from tasklist where id=" +taskId;
        ResultSet rs=db.executeQuery(sql);
        rs.next();
        int grades=rs.getInt("grades");
        String task=rs.getString("context");
        //  查询id
        GroupMemberTable.clear();
        GroupMemberTable.set("groupId",Integer.parseInt(groupId));
        GroupMemberTable.set("userId",Integer.parseInt(userId));
        sql= GroupMemberTable.getSelectStmt();
        rs=db.executeQuery(sql);
        rs.next();
        int id=rs.getInt("id");
        int init_grades=rs.getInt("grades");
        //  更改积分
        GroupMemberTable.set("id",id);
        GroupMemberTable.set("grades",init_grades+grades);
        sql= GroupMemberTable.getUpdateStmt();
        db.execute(sql);


        //添加操作记录
        MemberRecordTable.clear();
        MemberRecordTable.set("groupId",Integer.parseInt(groupId));
        MemberRecordTable.set("operatorId",Integer.parseInt(userId));
        MemberRecordTable.set("memberId",Integer.parseInt(userId));
        MemberRecordTable.set("object","task");
        MemberRecordTable.set("type","tag");
        MemberRecordTable.set("context",task+"+"+grades);
        MemberRecordTable.set("createTime",createTime);
        sql= MemberRecordTable.getInsertStmt();
        db.execute(sql);


        //添加任务历史记录
        TaskHistoryTable.clear();
        TaskHistoryTable.set("groupId",Integer.parseInt(groupId));
        TaskHistoryTable.set("taskId",Integer.parseInt(taskId));
        TaskHistoryTable.set("userId",Integer.parseInt(userId));
        TaskHistoryTable.set("createTime",createTime);
        sql= TaskHistoryTable.getInsertStmt();
        db.execute(sql);
        db.close();
        System.out.println("exit member_task finish");
    }
    private void getActivity(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        System.out.println("enter follow_activity getResult");
        FocusView.clear();
        String activityId=request.getParameter("activity_id");
        String userId=request.getParameter("user_id");
        if(activityId!=null){
            FocusView.set("id",Integer.parseInt(activityId));
        }
        if(userId!=null){
            FocusView.set("userId",Integer.parseInt(userId));
        }
        String sql= FocusView.getSelectStmt();
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
        System.out.println("exit follow_activity getResult");
    }

    private void processTask(HttpServletRequest request,ResultSet rs) throws JSONException, SQLException, ParseException {
        HttpSession session = request.getSession();
        int user_id=Integer.parseInt(session.getAttribute("id").toString());
        int auth=Integer.parseInt(session.getAttribute("auth")==null?"0":session.getAttribute("auth").toString());
        queryResult = new JSONArray("[]");
        rs.beforeFirst();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String queryTime=dateFormat.format(new Date());
        DatabaseHelper db = new DatabaseHelper();
        ResultSet  finished=null;
        while(rs.next())
        {
            JSONObject item = new JSONObject();
            item.put("id", rs.getInt("id"));
            item.put("group_id", rs.getInt("groupId"));
            item.put("creator_id", rs.getInt("creatorId"));
            item.put("creator", rs.getString("creator"));
            item.put("context", rs.getString("context"));
            item.put("grades", rs.getInt("grades"));
            item.put("create_time", rs.getString("createTime"));
            item.put("begin_time", rs.getString("beginTime"));
            item.put("end_time", rs.getString("endTime"));
            if(auth>1||rs.getInt("creatorId")==user_id){
                item.put("auth", 1);
            }else{
                item.put("auth", 0);
            }
            //任务状态:0未开始,1进行中,2已结束
            int task_status=0,my_status=0;
            if(dateFormat.parse(rs.getString("endTime")).compareTo(dateFormat.parse(queryTime))<0){
                task_status=2;
            }else if(dateFormat.parse(rs.getString("beginTime")).compareTo(dateFormat.parse(queryTime))<0){
                task_status=1;
            }
            //我的完成状态:0已完成,不可点击,1可点击,2不可操作
            if(task_status!=1){
                my_status=2;
            }else{
                TaskHistoryTable.clear();
                TaskHistoryTable.set("groupId",rs.getInt("groupId"));
                TaskHistoryTable.set("taskId",rs.getInt("id"));
                TaskHistoryTable.set("userId",user_id);
                finished= db.executeQuery(TaskHistoryTable.getSelectStmt());
                if(!finished.next()){
                    my_status=1;
                }

            }
            item.put("task_status", task_status);
            item.put("my_status", my_status);
            queryResult.put(item);
        }
    }

    private void processResult(HttpServletRequest request,ResultSet rs) throws JSONException, SQLException, ParseException {
        HttpSession session = request.getSession();
        int user_id=Integer.parseInt(session.getAttribute("id").toString());
        int auth=Integer.parseInt(session.getAttribute("auth")==null?"0":session.getAttribute("auth").toString());
        queryResult = new JSONArray("[]");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String queryTime=dateFormat.format(new Date());

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
            item.put("begin_time", rs.getString("beginTime"));
            item.put("end_time", rs.getString("endTime"));
            item.put("tag", rs.getString("tag"));
            item.put("imageUrl", rs.getString("imageUrl"));
            item.put("description", rs.getString("description"));
            if(auth>1||rs.getInt("publisherId")==user_id){
                item.put("auth", 1);
            }else{
                item.put("auth", 0);
            }

            //活动状态:0未开始,1进行中,2已结束
            int act_status=0;
            if(dateFormat.parse(rs.getString("endTime")).compareTo(dateFormat.parse(queryTime))<0){
                act_status=2;
            }else if(dateFormat.parse(rs.getString("beginTime")).compareTo(dateFormat.parse(queryTime))<0){
                act_status=1;
            }

            item.put("act_status", act_status);
            queryResult.put(item);
        }
    }
}
