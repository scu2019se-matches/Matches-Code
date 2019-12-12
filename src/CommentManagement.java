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


@WebServlet("/CommentManagement")
public class CommentManagement extends HttpServlet {

    private static JSONArray queryResult = null;
    private static QueryBuilder CommentView = null;
    private static QueryBuilder CommentTable = null;
    static {
        try {
            queryResult = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CommentView = new QueryBuilder("commentlist");
        CommentTable= new QueryBuilder("comment");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String action = request.getParameter("action");
        try
        {
            switch(action)
            {

                case "get_comment":
                    getComment(request, response);
                    break;
                case "del_comment":
                    delComment(request, response);
                    break;
                case "reply_comment":
                    replyComment(request, response);
                    break;
                case "add_comment":
                    addComment(request, response);
                    break;
                case "get_myComment":
                    getMyComment(request, response);
                    break;
                case "get_receiveComment":
                    getReceiveComment(request, response);
                    break;
                default:
                    System.out.println("comment: invalid action: "+action);
                    break;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void getComment(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        System.out.println("enter activity getComment");
        CommentView.clear();
        String activityId=request.getParameter("activity_id");
        CommentView.set("activityId",Integer.parseInt(activityId));
        String sql= CommentView.getSelectStmt();
        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs=db.executeQuery(sql);
            processComment(request,rs);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        out.print(queryResult);
        session.setAttribute("queryResult",queryResult);
        out.flush();
        out.close();
        System.out.println("exit activity getComment");
    }
    private void getMyComment(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        System.out.println("enter activity getMyComment");
        CommentView.clear();
        String userId=session.getAttribute("id").toString();
        CommentView.set("creatorId",Integer.parseInt(userId));
        String sql= CommentView.getSelectStmt();
        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs=db.executeQuery(sql);
            processComment(request,rs);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        out.print(queryResult);
        session.setAttribute("queryResult",queryResult);
        out.flush();
        out.close();
        System.out.println("exit activity getMyComment");
    }
    private void getReceiveComment(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        System.out.println("enter activity getMyComment");
        CommentView.clear();
        String userId=session.getAttribute("id").toString();
        CommentView.set("citeId",Integer.parseInt(userId));
        String sql= CommentView.getSelectStmt();
        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs=db.executeQuery(sql);
            processComment(request,rs);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        out.print(queryResult);
        session.setAttribute("queryResult",queryResult);
        out.flush();
        out.close();
        System.out.println("exit activity getMyComment");
    }
    private void delComment(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");	//设置编码
        try(DatabaseHelper db = new DatabaseHelper()){
            String commentId=request.getParameter("comment_id");
            CommentTable.clear();
            CommentTable.set("id",Integer.parseInt(commentId));
            String sql=CommentTable.getDeleteStmt();
            db.execute(sql);
        }
    }
    private void replyComment(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");	//设置编码
        String commentId=request.getParameter("comment_id");
        String context=request.getParameter("context");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime=dateFormat.format(new Date());
        int userId=Integer.parseInt(session.getAttribute("id").toString());
        try(DatabaseHelper db = new DatabaseHelper()){
            String sql="SELECT * from `comment` WHERE id="+commentId;
            ResultSet rs=db.executeQuery(sql);
            if(rs.next()){
                int activityId=rs.getInt("activityId");
                int creatorId=rs.getInt("creatorId");
                CommentTable.clear();
                CommentTable.set("activityId",(activityId));
                CommentTable.set("creatorId",userId);
                CommentTable.set("citeId",creatorId);
                CommentTable.set("createTime",createTime);
                CommentTable.set("context",context);
                sql=CommentTable.getInsertStmt();
                db.execute(sql);
            }
        }
    }
    private void addComment(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{
        HttpSession session = request.getSession();
        request.setCharacterEncoding("utf-8");	//设置编码
        String activityId=request.getParameter("activity_id");
        String context=request.getParameter("context");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime=dateFormat.format(new Date());
        int userId=Integer.parseInt(session.getAttribute("id").toString());
        try(DatabaseHelper db = new DatabaseHelper()){
            CommentTable.clear();
            CommentTable.set("activityId",Integer.parseInt(activityId));
            CommentTable.set("creatorId",userId);
            CommentTable.set("citeId",0);
            CommentTable.set("createTime",createTime);
            CommentTable.set("context",context);
            db.execute(CommentTable.getInsertStmt());
        }
    }
    private void processComment(HttpServletRequest request,ResultSet rs) throws JSONException, SQLException, ParseException {
        HttpSession session = request.getSession();
        int user_id=Integer.parseInt(session.getAttribute("id").toString());
        int auth=Integer.parseInt(session.getAttribute("auth")==null?"0":session.getAttribute("auth").toString());
        queryResult = new JSONArray("[]");
        rs.beforeFirst();
        while(rs.next())
        {
            JSONObject item = new JSONObject();
            item.put("id", rs.getInt("id"));
            item.put("creator", rs.getString("creator"));
            item.put("activity_id", rs.getInt("activityId"));
            item.put("cite_id", rs.getInt("citeId"));
            item.put("citeuser", rs.getString("citeuser"));
            item.put("create_time", rs.getString("createTime"));
            item.put("context", rs.getString("context"));
            if(auth>1||rs.getInt("creatorId")==user_id){
                item.put("auth", 2);
            }else{
                item.put("auth", 0);
            }
            queryResult.put(item);
        }
    }
}
