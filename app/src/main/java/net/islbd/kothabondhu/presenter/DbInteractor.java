package net.islbd.kothabondhu.presenter;

import net.islbd.kothabondhu.config.db.DbConfig;
import net.islbd.kothabondhu.config.db.DbCrud;
import net.islbd.kothabondhu.config.db.DbTables;
import net.islbd.kothabondhu.model.binder.DataRow;
import net.islbd.kothabondhu.model.binder.DataTable;

/**
 * Created by wahid.sadique on 9/11/2017.
 */

class DbInteractor implements IDbInteractor {
    private DbCrud dbCrud;
    private DbConfig dbConfig;

    DbInteractor(DbCrud dbCrud, DbConfig dbConfig) {
        this.dbCrud = dbCrud;
        this.dbConfig = dbConfig;
    }

    @Override
    public DataTable getCallLog() {
        String selectQuery = "SELECT * FROM " + DbTables.CALL_LOG_TABLE;
        return dbCrud.selectData(selectQuery, null, dbConfig.getSqLiteDatabase());
    }

    @Override
    public int markCall(String id, String name, Integer status) {
        DataTable dataTable = new DataTable(DbTables.CALL_LOG_TABLE);
        DataRow dataRow = new DataRow();
        dataRow.add("id", id);
        dataRow.add("agent", name);
        dataRow.add("status", 0);
        dataRow.add("time", 0);
        dataTable.add(0, dataRow);
        return dbCrud.insertData(dataTable, dbConfig.getSqLiteDatabase());
    }

    @Override
    public int removeCallLog(String id) {
        String tableName = DbTables.CALL_LOG_TABLE;
        String[] args = {id};
        String whereClause = " id = ? ";
        return dbCrud.deleteData(tableName, whereClause, args, dbConfig.getSqLiteDatabase());
    }

    @Override
    public boolean checkCallLog(String id) {
        String selectQuery = "SELECT * FROM " + DbTables.CALL_LOG_TABLE + " WHERE id = ?";
        String[] args = {id};
        return (dbCrud.selectData(selectQuery, args, dbConfig.getSqLiteDatabase())).size() > 0;
    }
}
