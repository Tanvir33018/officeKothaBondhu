package net.islbd.kothabondhu.config.db;

/**
 * Created by UserDetails on 11/7/2017.
 */

public final class DbTables {
    public static final String CALL_LOG_TABLE = "call_log";
    public static final String NOTIFICATIONS_TABLE = "notifications";

    protected static final String[] CREATE_TABLES = {
            "   CREATE TABLE " + CALL_LOG_TABLE + " (" +
                    "   id varchar(50), " +
                    "   agent varchar(50), " +
                    "   status int, " +
                    "   time datetime " +
                    "   );"
            ,
            "   CREATE TABLE " + NOTIFICATIONS_TABLE + " (" +
                    "   id varchar(50) UNIQUE, " +
                    "   name varchar(50), " +
                    "   description varchar(200), " +
                    "   url varchar(200)" +
                    "   );"
    };
}
