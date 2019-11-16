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


@WebServlet("/Commodity")
public class Commodity extends HttpServlet {

    private static JSONArray queryResult = null;
    private static QueryBuilder queryBuilder = null;
    static {
        try {
            queryResult = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        queryBuilder = new QueryBuilder("commodity");
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
                case "buyCommodity":
                    buyCommodity(request, response);
                    break;
                default:
                    System.out.println("Commodity: invalid action: "+action);
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
            try(DatabaseHelper db=new DatabaseHelper()){
                ResultSet rs=db.executeQuery(sql);
                processResult(request,rs);
            }
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
    private void buyCommodity(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        int commodityId = Integer.parseInt(request.getParameter("commodityId"));
        int groupId = Integer.parseInt(request.getParameter("groupId"));
        int userId = (int)session.getAttribute("id");
        JSONObject result = new JSONObject();
        try(DatabaseHelper db = new DatabaseHelper()){
            String sql = String.format(
                    "select groupmember.grades-commodity.grades newgrades, groupmember.id memberId" +
                    " from commodity join groupmember" +
                    " where groupmember.userId=%d and groupmember.groupId=%d and commodity.id=%d",
                    userId, groupId, commodityId);
            ResultSet rs = db.executeQuery(sql);
            if(rs.next() && rs.getInt("newgrades") >= 0){
                int memberId =  rs.getInt("memberId");
                sql = String.format(
                        "update `groupmember` set `grades`=%d where `id`=%d",
                        rs.getInt("newgrades"), memberId);
                db.execute(sql);
                sql = String.format(
                        "select `count` from membercommodity where memberId=%d and commodityId=%d",
                        memberId, commodityId);
                ResultSet rs1 = db.executeQuery(sql);
                if(rs1.next()){
                    sql = String.format(
                            "update membercommidity set `count`=%d where memberId=%d and commodityId=%d",
                            rs1.getInt(0)+1, memberId, commodityId);
                }else{
                    sql = String.format(
                            "insert into membercommidity (memberId, commodityId, `count`) values(%d, %d, %d)",
                            memberId, commodityId, 1);
                }
                db.execute(sql);
                result.put("errno", 0);
            }else{
                result.put("errno", 1);
                result.put("msg", "积分不足");
            }
        }
        out.print(result);
        out.flush();
        out.close();
        System.out.println("exit group_task getResult");
    }


    private void processResult(HttpServletRequest request,ResultSet rs) throws JSONException, SQLException, ParseException {
        queryResult = new JSONArray("[]");
        rs.beforeFirst();
        while(rs.next())
        {
            JSONObject item = new JSONObject();
            item.put("id", rs.getInt("id"));
            item.put("groupId", rs.getInt("groupId"));
            item.put("creatorId", rs.getInt("creatorId"));
            item.put("context", rs.getString("context"));
            item.put("grades", rs.getInt("grades"));
            queryResult.put(item);
        }
    }
}
