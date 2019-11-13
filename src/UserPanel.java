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
    private static QueryBuilder queryBuilder = null;
    private static QueryBuilder queryAnotherBuilder = null;
    private static QueryBuilder memberRecordBuilder = null;
    private static QueryBuilder groupMemberBuilder = null;
    static {
        try {
            queryResult = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queryBuilder = new QueryBuilder("task");
        queryAnotherBuilder = new QueryBuilder("taskhistory");
        memberRecordBuilder = new QueryBuilder("memberrecord");
        groupMemberBuilder = new QueryBuilder("groupmember");
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
        if(session.getAttribute("exist_result")==null || !(boolean)session.getAttribute("exist_result"))
        {
            System.out.println("getResult exist_result=false or null");
            String sql="select * from task where groupId in" +
                    "(select groupId from groupmember where userId=" +userId+ ")";
            DatabaseHelper db=new DatabaseHelper();
            ResultSet rs=db.executeQuery(sql);
            processTask(request,rs);
            session.setAttribute("exist_result", false);
            db.close();
        }
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
        sql="select * from task where id=" +taskId;
        ResultSet rs=db.executeQuery(sql);
        rs.next();
        int grades=rs.getInt("grades");
        String task=rs.getString("context");
        //  查询id
        groupMemberBuilder.clear();
        groupMemberBuilder.set("groupId",Integer.parseInt(groupId));
        groupMemberBuilder.set("userId",Integer.parseInt(userId));
        sql=groupMemberBuilder.getSelectStmt();
        rs=db.executeQuery(sql);
        rs.next();
        int id=rs.getInt("id");
        int init_grades=rs.getInt("grades");
        String member=rs.getString("user");
        //  更改积分
        groupMemberBuilder.set("id",id);
        groupMemberBuilder.set("grades",init_grades+grades);
        sql=groupMemberBuilder.getUpdateStmt();
        db.execute(sql);


        //添加操作记录
        memberRecordBuilder.clear();
        memberRecordBuilder.set("groupId",Integer.parseInt(groupId));
        memberRecordBuilder.set("operatorId",Integer.parseInt(userId));
        memberRecordBuilder.set("operator",session.getAttribute("username"));
        memberRecordBuilder.set("memberId",Integer.parseInt(userId));
        memberRecordBuilder.set("member",member);
        memberRecordBuilder.set("object","task");
        memberRecordBuilder.set("type","tag");
        memberRecordBuilder.set("context",task+"+"+grades);
        memberRecordBuilder.set("createTime",createTime);
        sql=memberRecordBuilder.getInsertStmt();
        db.execute(sql);


        //添加任务历史记录
        queryAnotherBuilder.clear();
        queryAnotherBuilder.set("groupId",Integer.parseInt(groupId));
        queryAnotherBuilder.set("taskId",Integer.parseInt(taskId));
        queryAnotherBuilder.set("userId",Integer.parseInt(userId));
        queryAnotherBuilder.set("user",session.getAttribute("username"));
        queryAnotherBuilder.set("createTime",createTime);
        sql=queryAnotherBuilder.getInsertStmt();
        db.execute(sql);
        db.close();
        System.out.println("exit member_task finish");
    }
    private void getActivity(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{

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
                queryAnotherBuilder.clear();
                queryAnotherBuilder.set("groupId",rs.getInt("groupId"));
                queryAnotherBuilder.set("taskId",rs.getInt("id"));
                queryAnotherBuilder.set("userId",user_id);
                finished= db.executeQuery(queryAnotherBuilder.getSelectStmt());
                if(!finished.next()){
                    my_status=1;
                }

            }
            item.put("task_status", task_status);
            item.put("my_status", my_status);
            queryResult.put(item);
        }
    }
}
