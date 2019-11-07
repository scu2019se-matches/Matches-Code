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
//                            throw new NotImplementedException();
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
    private String tableName = null;

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
                        if(queryKs.get(key)!=null&&queryKs.get(key)==1){
                            sql.append(String.format("`%s` like '%%%d%%'", key, cons.get(key)));
                        }else{
                            sql.append(String.format("`%s`=%d", key, cons.get(key)));
                        }
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
    public String getOrderClause(){
        if(orderString==null||orderString=="null"){
            return "";
        }
        return " order by "+orderString;
    }
    public String getSelectStmt(){
        String sql = String.format("select * from `%s` %s %s", tableName, getWhereClause(),getOrderClause());
        return sql;
    }

    public String getInsertStmt(){
        StringBuilder keys = new StringBuilder();
        StringBuilder values = new StringBuilder();
        try {
            for(String key : cons.keySet()){
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

    public static void main(String[] args){
        QueryBuilder queryBuilder = new QueryBuilder("user");
        queryBuilder.set("id", 1);
        queryBuilder.set("username", "abc");
        System.out.println(queryBuilder.getSelectStmt());
        System.out.println(queryBuilder.getInsertStmt());
    }
}
