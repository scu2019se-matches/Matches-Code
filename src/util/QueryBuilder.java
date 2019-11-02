package util;

/**
 * Created by silenus on 2019/11/2.
 */

abstract class QueryBuilderBase {

    protected static final int INUL = 0x80000000;

    protected static final String SNUL = null;

    protected static boolean isNull(String arg)
    {
        return arg == SNUL;
    }

    protected static boolean isNull(int arg)
    {
        return arg == INUL;
    }

    public abstract String getTableName();

    public abstract void reset();
    public abstract String getSelectStmt();
    public abstract String getUpdateStmt();
    public abstract String getInsertStmt();
    public abstract String getDeleteStmt();
}

public class QueryBuilder extends QueryBuilderBase {

    private int id = INUL;
    public int getId(){return id;}
    public void setId(int value){id = value;}

    private String username = SNUL;
    public String getUsername(){return username;}
    public void setUsername(String value){username = value;}

    private String password = SNUL;
    public String getPassword(){return password;}
    public void setPassword(String value){password = value;}

    private String email = SNUL;
    public String getEmail(){return email;}
    public void setEmail(String value){email = value;}

    private String fullname = SNUL;
    public String getFullname(){return fullname;}
    public void setFullname(String value){fullname = value;}

    private int gender = INUL;
    public int getGender(){return gender;}
    public void setGender(int value){gender = value;}

    @Override
    public String getTableName() {
        return "user";
    }

    @Override
    public void reset() {

    }

    @Override
    public String getSelectStmt() {
        StringBuilder sql = new StringBuilder(String.format("select * from `%s` where 1=1", getTableName()));
//        if(!isNull(getId()))
//            sql.append(String.format("and `id`=%d", getId()));
        if(!isNull(getUsername()))
            sql.append(String.format("and `username`='%s'", getUsername()));
        if(!isNull(getPassword()))
            sql.append(String.format("and `password`='%s'", getPassword()));
        if(!isNull(getEmail()))
            sql.append(String.format("and `email`='%s'", getEmail()));
        if(!isNull(getFullname()))
            sql.append(String.format("and `fullname`='%s'", getFullname()));
        if(!isNull(getGender()))
            sql.append(String.format("and `gender`=%d", getGender()));
        return sql.toString();
    }

    @Override
    public String getUpdateStmt() {
        return null;
    }

    @Override
    public String getInsertStmt() {
        StringBuilder keys = new StringBuilder();
        StringBuilder values = new StringBuilder();
        if(!isNull(getId())){
            if(keys.length() != 0){
                keys.append(", ");
                values.append(", ");
            }
            keys.append("`id`");
            values.append(String.format("%d", getId()));
        }
        if(!isNull(getUsername())){
            if(keys.length() != 0){
                keys.append(", ");
                values.append(", ");
            }
            keys.append("`username`");
            values.append(String.format("'%s'", getUsername()));
        }
        if(!isNull(getPassword())){
            if(keys.length() != 0){
                keys.append(", ");
                values.append(", ");
            }
            keys.append("`password`");
            values.append(String.format("'%s'", getPassword()));
        }
        if(!isNull(getEmail())){
            if(keys.length() != 0){
                keys.append(", ");
                values.append(", ");
            }
            keys.append("`email`");
            values.append(String.format("'%s'", getEmail()));
        }
        if(!isNull(getFullname())){
            if(keys.length() != 0){
                keys.append(", ");
                values.append(", ");
            }
            keys.append("`fullname`");
            values.append(String.format("'%s'", getFullname()));
        }
        if(!isNull(getGender())){
            if(keys.length() != 0){
                keys.append(", ");
                values.append(", ");
            }
            keys.append("`gender`");
            values.append(String.format("%d", getGender()));
        }
        String sql = String.format(
                "insert into `%s` (%s) values(%s)",
                getTableName(), keys.toString(), values.toString()
        );
        return sql;
    }

    @Override
    public String getDeleteStmt() {
        return null;
    }
}
