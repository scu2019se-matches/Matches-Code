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
                case "getRecord":
                    getRecord(request, response);
                    break;
                case "addCommodity":
                    addCommodity(request, response);
                    break;
                case "modifyRecord":
                    modifyRecord(request, response);
                    break;
                case "deleteCommodity":
                    deleteCommodity(request, response);
                    break;
                case "buyCommodity":
                    buyCommodity(request, response);
                    break;
                case "sellCommodity":
                    sellCommodity(request, response);
                    break;
                case "checkMember":
                    checkMember(request, response);
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

    private void getRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException, ParseException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        String groupId=request.getParameter("groupId");
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

    private void addCommodity(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        int groupId = Integer.parseInt(request.getParameter("groupId"));
        int userId = (int)session.getAttribute("id");
        String context = request.getParameter("context");
        int grades = Integer.parseInt(request.getParameter("grades"));
        queryBuilder.clear();
        queryBuilder.set("groupId", groupId);
        queryBuilder.set("creatorId", userId);
        queryBuilder.set("context", context);
        queryBuilder.set("grades", grades);
        String sql = queryBuilder.getInsertStmt();
        try(DatabaseHelper db = new DatabaseHelper()){
            db.execute(sql);
        }
        JSONObject res = new JSONObject();
        res.put("errno", 0);
        out.print(res);
        out.flush();
        out.close();
    }

    private void modifyRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        int groupId = Integer.parseInt(request.getParameter("groupId"));
        int commodityId = Integer.parseInt(request.getParameter("id"));
        String context = request.getParameter("context");
        int grades = Integer.parseInt(request.getParameter("grades"));
        queryBuilder.clear();
        queryBuilder.set("id", commodityId);
        queryBuilder.set("context", context);
        queryBuilder.set("grades", grades);
        String sql = queryBuilder.getUpdateStmt();
        try(DatabaseHelper db = new DatabaseHelper()){
            db.execute(sql);
        }
        JSONObject res = new JSONObject();
        res.put("errno", 0);
        out.print(res);
        out.flush();
        out.close();
    }

    private void deleteCommodity(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        int commodityId = Integer.parseInt(request.getParameter("id"));
        queryBuilder.clear();
        queryBuilder.set("id", commodityId);
        String sql = queryBuilder.getDeleteStmt();
        try(DatabaseHelper db = new DatabaseHelper()){
            db.execute(sql);
        }
        JSONObject res = new JSONObject();
        res.put("errno", 0);
        out.print(res);
        out.flush();
        out.close();
    }

    private void checkMember(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        int userId = (int)session.getAttribute("id");
        int groupId = Integer.parseInt(request.getParameter("groupId"));
        QueryBuilder queryBuilder = new QueryBuilder("group");
        queryBuilder.set("id", groupId);
        String sql = queryBuilder.getSelectStmt();
        JSONObject res = new JSONObject();
        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs = db.executeQuery(sql);
            if(rs.next()){
                if(rs.getInt("creatorId") == userId)res.put("role",1);
                else res.put("role", 0);
            }else{
                res.put("role", -1);
            }
        }
        out.print(res);
        out.flush();
        out.close();
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
                    "select groupmember.grades-commodity.grades newgrades, groupmember.id memberId, `commodity`.`context` `context`" +
                    " from commodity join groupmember" +
                    " where groupmember.userId=%d and groupmember.groupId=%d and commodity.id=%d",
                    userId, groupId, commodityId);
            ResultSet rs = db.executeQuery(sql);
            if(rs.next() && rs.getInt("newgrades") >= 0){
                int memberId =  rs.getInt("memberId");
                String commmodityName = rs.getString("context");
                int newGrade = rs.getInt("newgrades");
                result.put("newgrades", newGrade);
                sql = String.format(
                        "update `groupmember` set `grades`=%d where `id`=%d",
                        newGrade, memberId);
                db.execute(sql);
                sql = String.format(
                        "insert into membercommodity (groupId, memberId, commodityId) values(%d, %d, %d)",
                        groupId, userId, commodityId);
                db.execute(sql);
                QueryBuilder queryBuilder = new QueryBuilder("memberrecord");
                queryBuilder.set("groupId", groupId);
                queryBuilder.set("operatorId", userId);
                queryBuilder.set("memberId", userId);
                queryBuilder.set("object", "commodity");
                queryBuilder.set("type", "buy");
                queryBuilder.set("context", commmodityName);
                queryBuilder.set("createTime", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
                sql = queryBuilder.getInsertStmt();
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

    private void sellCommodity(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        int commodityId = Integer.parseInt(request.getParameter("commodityId"));
        int groupId = Integer.parseInt(request.getParameter("groupId"));
        int userId = (int)session.getAttribute("id");
        JSONObject result = new JSONObject();
        try(DatabaseHelper db = new DatabaseHelper()){
            String sql = String.format(
                    "select groupmember.grades+commodity.grades newgrades, groupmember.id memberId, `commodity`.`context` `context`" +
                            " from commodity join groupmember" +
                            " where groupmember.userId=%d and groupmember.groupId=%d and commodity.id=%d",
                    userId, groupId, commodityId);
            ResultSet rs = db.executeQuery(sql);
            rs.next();
            int memberId =  rs.getInt("memberId");
            String commodityName = rs.getString("context");
//            sql = String.format(
//                    "update `groupmember` set `grades`=%d where `id`=%d",
//                    rs.getInt("newgrades"), memberId);
//            db.execute(sql);
            sql = String.format(
                    "select `id` from `membercommodity` where `commodityId`=%d and `memberId`=%d",
                    commodityId, userId
            );
            rs = db.executeQuery(sql);
            rs.next();
            sql = String.format(
                    "delete from `membercommodity` where `id`=%d",
                    rs.getInt(1));
            db.execute(sql);
            QueryBuilder queryBuilder = new QueryBuilder("memberrecord");
            queryBuilder.set("groupId", groupId);
            queryBuilder.set("operatorId", userId);
            queryBuilder.set("memberId", userId);
            queryBuilder.set("object", "commodity");
            queryBuilder.set("type", "use");
            queryBuilder.set("context", commodityName);
            queryBuilder.set("createTime", (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
            sql = queryBuilder.getInsertStmt();
            db.execute(sql);
            result.put("errno", 0);
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
