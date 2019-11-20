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

@WebServlet("/MemberPanel")
public class MemberPanel extends HttpServlet {

    private static JSONArray queryResult = null;
    private static QueryBuilder MemberCommodityView = null;
    private static QueryBuilder MemberRecordView = null;
    private static QueryBuilder MemberRecordTable= null;
    private static QueryBuilder TaskHistory = null;
    private static QueryBuilder GroupMemberTable = null;
    static {
        try {
            queryResult = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MemberCommodityView = new QueryBuilder("membercommoditylist");
        MemberRecordView = new QueryBuilder("memberrecordlist");
        MemberRecordTable = new QueryBuilder("memberrecord");
        TaskHistory = new QueryBuilder("taskhistory");
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
                case "modify_grades":
                    modifyGrades(request, response);
                    break;
                default:
                    System.out.println("memberDetails: invalid action: "+action);
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
    }
    private void getRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException, ParseException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        String groupId=request.getParameter("group_id");
        String memberId=request.getParameter("member_id");
        try(DatabaseHelper db = new DatabaseHelper()){
            System.out.println("enter member_commodity getResult");
            MemberCommodityView.clear();
            MemberCommodityView.set("groupId",Integer.parseInt(groupId));
            MemberCommodityView.set("memberId",Integer.parseInt(memberId));

            String sql= MemberCommodityView.getSelectStmt();
            ResultSet rs=db.executeQuery(sql);
            processResult(request,rs);
            out.print(queryResult);
            session.setAttribute("queryResult",queryResult);
            out.flush();
            out.close();
            System.out.println("exit member_commodity getResult");
        }

    }
    private void deleteRecord(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException, SQLException {

    }
    private void modifyRecord(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{

    }
    private void getStatistics(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();

        String groupId=request.getParameter("group_id");
        String memberId=request.getParameter("member_id");
        try(DatabaseHelper db = new DatabaseHelper()){
            System.out.println("enter member_record getResult");
            MemberRecordView.clear();

            MemberRecordView.set("groupId",Integer.parseInt(groupId));
            MemberRecordView.set("memberId",Integer.parseInt(memberId));
            MemberRecordView.set("orderBy","createTime desc");

            String sql= MemberRecordView.getSelectStmt();
            ResultSet rs=db.executeQuery(sql);
            processRecord(request,rs);
            out.print(queryResult);
            session.setAttribute("queryResult",queryResult);
            out.flush();
            out.close();
            System.out.println("exit member_record getResult");
        }

    }

    private void modifyGrades(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{
        System.out.println("enter member_grades modify");
        request.setCharacterEncoding("utf-8");	//设置编码
        HttpSession session = request.getSession();
        String operatorId=request.getParameter("operator_id");
        String groupId=request.getParameter("group_id");
        String memberId=request.getParameter("member_id");
        String grades=request.getParameter("grades");
        String remarks=request.getParameter("remarks");

        GroupMemberTable.clear();
        GroupMemberTable.set("groupId",Integer.parseInt(groupId));
        GroupMemberTable.set("userId",Integer.parseInt(memberId));
        String sql= GroupMemberTable.getSelectStmt();
        //查询id
        try(DatabaseHelper db=new DatabaseHelper()){
            ResultSet rs=db.executeQuery(sql);
            rs.next();
            int id=rs.getInt("id");
            int init_grades=rs.getInt("grades");
            //更改积分
//        System.out.println("grades:"+grades+"init_grades:"+init_grades);
            GroupMemberTable.set("id",id);
            GroupMemberTable.set("grades",init_grades+Integer.parseInt(grades));
            sql= GroupMemberTable.getUpdateStmt();
            db.execute(sql);

            //添加记录
            MemberRecordTable.clear();
            MemberRecordTable.set("groupId",Integer.parseInt(groupId));
            MemberRecordTable.set("operatorId",Integer.parseInt(operatorId));
            MemberRecordTable.set("memberId",Integer.parseInt(memberId));
            MemberRecordTable.set("object","grades");
            MemberRecordTable.set("type","modify");
            MemberRecordTable.set("context","积分"+grades);
            MemberRecordTable.set("remarks",remarks);
            MemberRecordTable.set("createTime",(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()));
            sql= MemberRecordTable.getInsertStmt();
            db.execute(sql);
            db.close();
            System.out.println("exit member_grades modify");
        }

    }
    private void processResult(HttpServletRequest request,ResultSet rs) throws JSONException, SQLException, ParseException {
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
            item.put("commodity_id", rs.getInt("commodityId"));
            item.put("commodity", rs.getString("commodity"));
            item.put("grades", rs.getInt("grades"));
            item.put("count", rs.getInt("count"));
            if(rs.getInt("memberId")==user_id){
                item.put("auth", 1);
            }else{
                item.put("auth", 0);
            }
            queryResult.put(item);
        }
    }
    private void processRecord(HttpServletRequest request,ResultSet rs) throws JSONException, SQLException{
        HttpSession session = request.getSession();
        queryResult = new JSONArray("[]");
        rs.beforeFirst();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
//        String queryTime=dateFormat.format(new Date());
        while(rs.next())
        {
            JSONObject item = new JSONObject();
            item.put("id", rs.getInt("id"));
            item.put("group_id", rs.getInt("groupId"));
            item.put("operator_id", rs.getInt("operatorId"));
            item.put("operator", rs.getString("operator"));
            item.put("member_id", rs.getInt("memberId"));
            item.put("member", rs.getString("member"));
            item.put("object", rs.getString("object"));
            item.put("type", rs.getString("type"));
            item.put("context", rs.getString("context"));
            item.put("remarks", rs.getString("remarks"));
            item.put("type", rs.getString("type"));
//            item.put("create_time",dateFormat.format((rs.getString("createTime"))));
            item.put("create_time",rs.getString("createTime"));
            queryResult.put(item);
        }
    }
}
