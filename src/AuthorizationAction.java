/**
 * Created by silenus on 2019/4/29.
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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/AuthorizationAction")
public class AuthorizationAction extends HttpServlet
{
    private static QueryBuilder queryBuilder = new QueryBuilder("user");
    private static JSONArray result = new JSONArray();

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        System.out.println("AuthorizationAction action="+action);
        try
        {
            switch(action)
            {
                case "getResult":
                    getResult(request, response);
                    break;
                case "query":
                    doQuery(request, response);
                    break;
                case "delete":
                    Delete(request, response);
                    break;
                case "update":
                    Update(request, response);
                    break;
                case "sort":
                    Sort(request, response);
                    break;
//                case "statistics":
//                    Statistics(request, response);
//                    break;
                case "clearQuery":
                    ClearQuery();
                    break;
                case "clearSort":
                    ClearSort();
                    break;
                default:
                    throw new Exception("AuthorizationAction: 未知的请求类型");
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void getResult(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException {
        System.out.println("enter AuthorizationAction.getResult");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String sql=queryBuilder.getSelectStmt();
        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs=db.executeQuery(sql);
            processResult(rs);
        }
        out.print(result);
        out.flush();
        out.close();
    }

    private void doQuery(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException{
        System.out.println("enter AuthorizationAction.doQuery");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        queryBuilder.clear();
        if(request.getParameter("id").length()>0) {
            queryBuilder.set("id", Integer.parseInt(request.getParameter("id")));
        }
        queryBuilder.set("username", request.getParameter("username"));
        queryBuilder.set("email", request.getParameter("email"));
        if(request.getParameter("auth").length()>0){
            queryBuilder.set("auth", Integer.parseInt(request.getParameter("auth")));
        }
        String sql=queryBuilder.getSelectStmt();
        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs=db.executeQuery(sql);
            processResult(rs);
        }
        JSONObject json = new JSONObject();
        json.put("errno", 0);
        out.print(json);
        out.flush();
        out.close();
    }

    private void Delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("enter AuthorizationAction.doQuery");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        QueryBuilder q=new QueryBuilder(queryBuilder.getTableName());
        q.set("id", Integer.parseInt(request.getParameter("id")));
        String sql=q.getDeleteStmt();
        try(DatabaseHelper db=new DatabaseHelper()) {
            db.execute(sql);
        }
        JSONObject json = new JSONObject();
        json.put("errno", 0);
        out.print(json);
        out.flush();
        out.close();
    }

    private void Update(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("enter AuthorizationAction.Update");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        QueryBuilder q=new QueryBuilder(queryBuilder.getTableName());
        q.set("id", Integer.parseInt(request.getParameter("id")));
        JSONObject json = new JSONObject();
        q.set("auth", Integer.parseInt(request.getParameter("auth")));
        if(!json.has("errno")) {
            String sql = q.getUpdateStmt();
            try(DatabaseHelper db = new DatabaseHelper()){
                db.execute(sql);
            }
            json.put("errno", 0);
        }
        out.print(json);
        out.flush();
        out.close();
    }

    private void Sort(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException {
        System.out.println("enter AuthorizationAction.Sort");
        response.setContentType("application/json; charset=UTF-8");
        String sortBy = request.getParameter("sortBy");
        System.out.println(sortBy);
        queryBuilder.set("orderBy", sortBy);
        PrintWriter out = response.getWriter();
        JSONObject json = new JSONObject();
        json.put("errno", 0);
        out.print(json);
        out.flush();
        out.close();
    }

//    private void Statistics(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException, IOException {
//        System.out.println("enter AuthorizationAction.Statistics");
//        String sql = String.format("select " +
//                        " authorization as auth, " +
//                        " count(*) as cnt " +
//                        " from (%s) as tmp " +
//                        " group by authorization ",
//                queryBuilder.getSelectStmt());
//        System.out.println("AuthorizationAction.Statistics: sql = "+sql);
//        DatabaseHelper db=new DatabaseHelper();
//        ResultSet rs=db.executeQuery(sql);
//        JSONArray list = new JSONArray();
//        String[] cla = new String[]{"学生", "教师", "管理员", "开发者"};
//        while(rs.next())
//        {
//            JSONObject item = new JSONObject();
//            int auth = rs.getInt("auth");
//            int i=0;
//            while(auth > 0){ i++; auth>>=1;}
//            item.put("class", cla[i-1]);
//            item.put("count", rs.getString("cnt"));
//            list.put(item);
//        }
//        response.setContentType("application/json; charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        out.print(list);
//        out.flush();
//        out.close();
//    }

    private void ClearQuery(){
        System.out.println("enter AuthorizationAction.ClearQuery");
        queryBuilder.clear();
    }

    private void ClearSort(){
        System.out.println("enter AuthorizationAction.ClearSort");
        queryBuilder.set("orderBy", "");
    }

    private void processResult(ResultSet rs) throws JSONException, SQLException {
        result = new JSONArray("[]");
        rs.beforeFirst();
        while(rs.next())
        {
            JSONObject item = new JSONObject();
            item.put("id", rs.getInt("id"));
            item.put("email", rs.getString("email"));
            item.put("username", rs.getString("username"));
            item.put("auth", rs.getInt("auth"));
            result.put(item);
        }
    }
}