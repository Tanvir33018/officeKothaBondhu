package net.islbd.kothabondhu.presenter;


import net.islbd.kothabondhu.model.binder.DataTable;

/**
 * Created by wahid.sadique on 8/30/2017.
 */

public interface IDbInteractor {
    DataTable getCallLog();

    int markCall(String id, String name, Integer status);

    int removeCallLog(String id);

    boolean checkCallLog(String id);
}
