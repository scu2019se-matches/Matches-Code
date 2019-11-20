package util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.ranges.Range;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by silenus on 2019/11/2.
 */

public class QueryBuilder{

    private static JSONObject dbconfig;

    static{
        dbconfig = new JSONObject();
        String sql = String.format(
                "SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE " +
                "FROM information_schema.COLUMNS " +
                "WHERE TABLE_SCHEMA = '%s'", DatabaseHelper.DB_NAME);
        try(DatabaseHelper db = new DatabaseHelper()){
            ResultSet rs = db.executeQuery(sql);
            try {
                while(rs.next()){
                    String tableName = rs.getString("TABLE_NAME");
                    String columnName = rs.getString("COLUMN_NAME");
                    String dataType = rs.getString("DATA_TYPE");
                    if(!dbconfig.has(tableName)){
                        dbconfig.put(tableName, new JSONObject());
                    }
                    DataType dataTypeId = DataType.None;
                    switch(dataType.toLowerCase()){
                        case "tinyint":case "smallint":case "mediumint":
                        case "int":case "integer":case "bigint":
                            dataTypeId = DataType.Integer;
                            break;
                        case "char":case "varchar":case "tinytext":
                        case "text":case "mediumtext":case "longtext":
                        case "datetime":case "date":case "time":case "timestamp":
                            dataTypeId = DataType.Text;
                            break;
                        default:
                            System.out.printf("不支持的字段类型\"%s\"\n", dataType);
                            throw new NotImplementedException();
                    }
                    dbconfig.getJSONObject(tableName).put(columnName, dataTypeId);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        System.out.println(dbconfig.toString());
    }

    private enum DataType{
        None, Text, Integer
    }

    private HashMap<String, Object> cons = new HashMap<>();
    private HashMap<String, Integer> queryKs = new HashMap<>();//0精确,1模糊
    String orderString=null;
    private JSONObject tableConfig = null;

    public QueryBuilder(String tableName){
        if(!dbconfig.has(tableName)){
            throw new InvalidParameterException(String.format("数据表\"%s\"不存在", tableName));
        }
        try {
            tableConfig = dbconfig.getJSONObject(tableName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.tableName = tableName;
    }

    private String tableName = null;
    public String getTableName(){
        return tableName;
    }
    public void setTableName(String value){
        tableName = value;
    }

    public void set(String key, Object value){
        if(value=="null"||value==null||value.equals(null)||value.equals("null")){
            return;
        }
        if(key=="orderBy"){
            orderString=value.toString();
            return;
        }else if(!tableConfig.has(key)){
            throw new InvalidParameterException(String.format("列名\"%s\"不存在", key));
        }

        try {
            switch ((DataType)tableConfig.get(key)){
                case Integer:
                    if(!(value instanceof Integer)){
                        throw new IllegalArgumentException("value不是整数类型");
                    }
                    cons.put(key, value);
                    break;
                case Text:
                    if(!(value instanceof String)){
                        throw new IllegalArgumentException("value不是字符串类型");
                    }
                    cons.put(key, value);
                    break;
                default:
                    throw new NotImplementedException();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void set(String key, Object value,int ks){
        if(value=="null"||value==null||value.equals(null)||value.equals("null")){
            return;
        }
        if(key=="orderBy"){
            orderString=value.toString();
            return;
        }else if(!tableConfig.has(key)){
            throw new InvalidParameterException(String.format("列名\"%s\"不存在", key));
        }
        try {
            switch ((DataType)tableConfig.get(key)){
                case Integer:
                    if(!(value instanceof Integer)){
                        throw new IllegalArgumentException("value不是整数类型");
                    }
                    cons.put(key, value);
                    queryKs.put(key,ks);
                    break;
                case Text:
                    if(!(value instanceof String)){
                        throw new IllegalArgumentException("value不是字符串类型");
                    }
                    cons.put(key, value);
                    queryKs.put(key,ks);
                    break;
                default:
                    throw new NotImplementedException();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Object get(String key){
        if(!tableConfig.has(key)){
            throw new InvalidParameterException(String.format("列名\"%s\"不存在", key));
        }
        return cons.get(key);
    }

    public void clear(){
        cons.clear();
        queryKs.clear();
        orderString=null;
    }

    /**
     * where ...
     * @return SQL的where子句
     */
    public String getWhereClause(){
        StringBuilder sql = new StringBuilder();
        if(cons.keySet().size()<1){
            return "";
        }
        try {
            for(String key : cons.keySet()){
                if(sql.length() != 0){
                    sql.append(" and ");
                }
                switch((DataType)tableConfig.get(key)){
                    case Integer:
//                        if(queryKs.get(key)!=null&&queryKs.get(key)==1){
//                            sql.append(String.format("`%s` like '%%%d%%'", key, cons.get(key)));
//                        }else{
                            sql.append(String.format("`%s`=%d", key, cons.get(key)));
//                        }
                        break;
                    case Text:
                        if(queryKs.get(key)!=null&&queryKs.get(key)==1){
                            sql.append(String.format("`%s` like '%%%s%%'", key, cons.get(key)));
                        }else{
                            sql.append(String.format("`%s`='%s'", key, cons.get(key)));
                        }
                        break;
                    default:
                        throw new NotImplementedException();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "where " + sql.toString();
    }

    /**
     * order by ...
     * @return SQL的order by子句
     */
    public String getOrderClause(){
        if(orderString==null||orderString=="null"){
            return "";
        }
        return " order by "+orderString;
    }

    /**
     * select * from tableName where ...
     * @return SQL查询语句
     */
    public String getSelectStmt(){
        String sql = String.format("select * from `%s` %s %s", tableName, getWhereClause(),getOrderClause());
        return sql;
    }

    /**
     * insert into tableName (k1, k2, ...) values(v1, v2, ...)
     * @return SQL插入语句
     */
    public String getInsertStmt(){
        StringBuilder keys = new StringBuilder();
        StringBuilder values = new StringBuilder();
        try {
            for(String key : cons.keySet()){
                if(key.equals("id"))continue;
                if(keys.length() != 0){
                    keys.append(", ");
                }
                if(values.length() != 0){
                    values.append(", ");
                }
                keys.append(String.format("`%s`", key));
                switch((DataType)tableConfig.get(key)){
                    case Integer:
                        values.append(cons.get(key));
                        break;
                    case Text:
                        values.append(String.format("'%s'", cons.get(key)));
                        break;
                    default:
                        throw new NotImplementedException();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String sql = String.format("insert into `%s` (%s) values(%s)", tableName, keys.toString(), values.toString());
        return sql;
    }

    /**
     * update tableName set k1=v1, k2=v2, ... where id=?
     * id必须设置，作为过滤条件
     * @return SQL修改语句
     */
    public String getUpdateStmt(){
        assert(cons.containsKey("id"));
        // [TODO] 如果没有修改怎么处理
        assert(cons.size() >= 2);
        StringBuilder s = new StringBuilder();
        try {
            for(String key : cons.keySet()){
                if(key.equals("id"))continue;
                if(s.length() != 0){
                    s.append(", ");
                }
                switch((DataType)tableConfig.get(key)){
                    case Integer:
                        s.append(String.format("`%s`=%d", key, cons.get(key)));
                        break;
                    case Text:
                        s.append(String.format("`%s`='%s'", key, cons.get(key)));
                        break;
                    default:
                        throw new NotImplementedException();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String sql = String.format("update `%s` set %s where `id`=%d", tableName, s.toString(), (int)cons.get("id"));
        return sql;
    }

    /**
     * delete from tableName where id=?
     * id必须设置，作为过滤条件，其他设置无效
     * @return SQL删除语句
     */
    public String getDeleteStmt(){
        assert(cons.containsKey("id"));
        String sql = String.format("delete from `%s` where `id`=%d", tableName, (int)cons.get("id"));
        return sql;
    }

    public static void main(String[] args){
        QueryBuilder queryBuilder = new QueryBuilder("user");
        queryBuilder.set("id", 1);
        queryBuilder.set("username", "abc");
        System.out.println(queryBuilder.getSelectStmt());
        System.out.println(queryBuilder.getInsertStmt());
        System.out.println(queryBuilder.getUpdateStmt());
        System.out.println(queryBuilder.getDeleteStmt());
    }
}
