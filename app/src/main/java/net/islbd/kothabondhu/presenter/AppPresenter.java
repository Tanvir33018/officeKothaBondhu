package net.islbd.kothabondhu.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.sinch.android.rtc.SinchClient;

import net.islbd.kothabondhu.config.ApiClient;
import net.islbd.kothabondhu.config.db.DbConfig;
import net.islbd.kothabondhu.config.db.DbCrud;
import net.islbd.kothabondhu.config.SinchConfig;
import net.islbd.kothabondhu.utility.DbUtils;
import net.islbd.kothabondhu.utility.SharedPrefUtils;
import net.islbd.kothabondhu.utility.WebUtils;

/**
 * Created by wahid.sadique on 9/12/2017.
 */

public class AppPresenter {

    public IApiInteractor getApiInterface() {
        return ApiClient.getClient(WebUtils.BASE_URL, WebUtils.REQUEST_TIMEOUT).create(IApiInteractor.class);
    }

    public IDbInteractor getDbInterface(Context context) {
        return new DbInteractor(new DbCrud(), new DbConfig(context, DbUtils.DB_NAME, DbUtils.DB_VERSION));
    }

    public SharedPreferences getSharedPrefInterface(Context context) {
        return context.getSharedPreferences(SharedPrefUtils.SPF_NAME, SharedPrefUtils.SPF_MODE);
    }

    public SinchClient getSinchClient(Context context, String userId) {
        return SinchConfig.getInstance().getClient(context, userId);
    }
}
