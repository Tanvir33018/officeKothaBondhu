package net.islbd.kothabondhu.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;




public class NotificationService extends Service {
    private Context context;
    private String TAG = "NotificationService";

    public NotificationService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();


        return START_STICKY;
    }

}
