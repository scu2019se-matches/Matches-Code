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


@WebServlet("/GroupTask")
public class GroupTask extends HttpServlet {

    private static JSONArray queryResult = null;
    private static QueryBuilder TaskView = null;
    private static QueryBuilder TaskHistoryTable = null;
    private static QueryBuilder TaskTable = null;
    private static QueryBuilder MemberRecordTable = null;
    private static QueryBuilder GroupMemberTable = null;
    static {
        try {
            queryResult = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TaskView = new QueryBuilder("tasklist");
        TaskTable = new QueryBuilder("task");
        TaskHistoryTable = new QueryBuilder("taskhistory");
        MemberRecordTable = new QueryBuilder("memberrecord");
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
                case "finish_task":
                    finishTask(request, response);
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
            String groupId=request.getParameter("group_id");
            String context=request.getParameter("context");
            String grades=request.getParameter("grades");
            String createTime=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
            String beginTime=request.getParameter("begin_time");
            String endTime=request.getParameter("end_time");
            TaskTable.clear();
            TaskTable.set("groupId",Integer.parseInt(groupId));
            TaskTable.set("context",context);
            TaskTable.set("grades",Integer.parseInt(grades));
            TaskTable.set("createTime",createTime);
            TaskTable.set("beginTime",beginTime);
            TaskTable.set("endTime",endTime);

            sql= TaskTable.getInsertStmt();
            db.execute(sql);
            response.sendRedirect("groupdetails/task/list.jsp?group_id="+groupId);
        }
    }
    private void getRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException, ParseException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        String groupId=request.getParameter("group_id");
        String grades=request.getParameter("grades");
        String context=request.getParameter("context");
        String orderBy=request.getParameter("orderby");
        System.out.println("enter group_task getResult");
        TaskView.clear();
        TaskView.set("groupId",Integer.parseInt(groupId));
        if(grades!=null){
            TaskView.set("grades",Integer.parseInt(grades));
        }
        TaskView.set("context",context,1);
        TaskView.set("orderBy",orderBy);

        String sql= TaskView.getSelectStmt();
        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs=db.executeQuery(sql);
            processResult(request,rs);
            db.close();
            out.print(queryResult);
            session.setAttribute("queryResult",queryResult);
            out.flush();
            out.close();
            System.out.println("exit group_task getResult");
        }

    }
    private void deleteRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");	//设置编码
        String taskId=request.getParameter("task_id");
        try(DatabaseHelper db = new DatabaseHelper()){
            TaskTable.set("id",Integer.parseInt(taskId));
            String sql= TaskTable.getDeleteStmt();
            db.execute(sql);
//            response.sendRedirect("group/list.jsp");
        }
    }
    private void modifyRecord(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");	//设置编码
        String taskId=request.getParameter("task_id");
        String groupId=request.getParameter("group_id");
        String grades=request.getParameter("grades");
        String endTime=request.getParameter("end_time");
        try(DatabaseHelper db = new DatabaseHelper()){
            TaskTable.set("id",Integer.parseInt(taskId));
            TaskTable.set("groupId",Integer.parseInt(groupId));
            TaskTable.set("grades",Integer.parseInt(grades));
            TaskTable.set("endTime",endTime);
            String sql= TaskTable.getUpdateStmt();
            db.execute(sql);
//            response.sendRedirect("group/list.jsp");
        }
    }
    private void getStatistics(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException {

    }
    private void finishTask(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");	//设置编码

        String sql="";

        //增加积分
        String groupId=request.getParameter("group_id");
        String userId=session.getAttribute("id").toString();
        String grades=request.getParameter("grades");
        String taskId=request.getParameter("task_id");
        String task=request.getParameter("task");
        String createTime=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());
        //查询id
        GroupMemberTable.clear();
        GroupMemberTable.set("groupId",Integer.parseInt(groupId));
        GroupMemberTable.set("userId",Integer.parseInt(userId));
        sql= GroupMemberTable.getSelectStmt();

        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs=db.executeQuery(sql);
            rs.next();
            int id=rs.getInt("id");
            int init_grades=rs.getInt("grades");
            //更改积分
            GroupMemberTable.set("id",id);
            GroupMemberTable.set("grades",init_grades+Integer.parseInt(grades));
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
        }
        System.out.println("exit member_task finish");
    }


    private void processResult(HttpServletRequest request,ResultSet rs) throws JSONException, SQLException, ParseException {
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
//                finished.next();
//                System.out.println("finished:"+finished);
                if(!finished.next()){
                    my_status=1;
                }

            }
            item.put("task_status", task_status);
            item.put("my_status", my_status);
//            System.out.println("my_status:"+my_status);
//            item.put("user_id", user_id);
            queryResult.put(item);
        }
    }
}
