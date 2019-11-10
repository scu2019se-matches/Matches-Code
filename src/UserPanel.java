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
    static {
        try {
            queryResult = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queryBuilder = new QueryBuilder("task");
        queryAnotherBuilder = new QueryBuilder("taskhistory");
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
        DatabaseHelper db = new DatabaseHelper();
        String sql="";

        //增加组
        String title=request.getParameter("title");
        String password=request.getParameter("password");
        String creatorId=(String)session.getAttribute("id");
        String creator=(String)session.getAttribute("username");
        String createTime=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date());

        ResultSet rs = db.executeQuery("select max(id) from `group`");
        rs.next();
        int groupId=1;

        sql=queryBuilder.getInsertStmt();
        db.execute(sql);



        sql=queryAnotherBuilder.getInsertStmt();
        db = new DatabaseHelper();
        db.execute(sql);
        response.sendRedirect("group/list.jsp");
    }
    private void getRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException, ParseException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        String groupId=request.getParameter("group_id");
        String grades=request.getParameter("grades");
        String context=request.getParameter("context");
        String orderBy=request.getParameter("orderby");
        if(session.getAttribute("exist_result")==null || !(boolean)session.getAttribute("exist_result"))
        {
            System.out.println("getResult exist_result=false or null");
            queryBuilder.clear();

            queryBuilder.set("groupId",Integer.parseInt(groupId));
            if(grades!=null){
                queryBuilder.set("grades",Integer.parseInt(grades));
            }
            queryBuilder.set("context",context,1);
            queryBuilder.set("orderBy",orderBy);

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
        System.out.println("exit group_task getResult");
    }
    private void deleteRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {

    }
    private void modifyRecord(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{

    }
    private void getStatistics(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException {

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
                queryAnotherBuilder.clear();
                queryAnotherBuilder.set("groupId",rs.getInt("groupId"));
                queryAnotherBuilder.set("taskId",rs.getInt("id"));
                queryAnotherBuilder.set("userId",user_id);
                finished= db.executeQuery(queryAnotherBuilder.getSelectStmt());
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
