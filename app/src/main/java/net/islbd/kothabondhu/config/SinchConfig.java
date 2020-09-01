package net.islbd.kothabondhu.config;

import android.content.Context;

import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;

/**
 * Created by User on 10/9/2019.
 */

public class SinchConfig {
    private SinchClient sinchClient = null;
    private static SinchConfig sinchConfig = null;
    private static final String APPLICATION_KEY = "57ec7cb7-4d22-43e5-9e25-aeefab22428e";
    private static final String APPLICATION_SECRET = "g+PQN4QB5U2Q2EFNU0BNEw==";
    private static final String APPLICATION_HOST = "sandbox.sinch.com";

    public static SinchConfig getInstance() {
        if (sinchConfig == null) sinchConfig = new SinchConfig();
        return sinchConfig;
    }

    public SinchClient getClient(Context context, String userId) {
        if (sinchClient == null) {
            sinchClient = Sinch.getSinchClientBuilder().context(context)
                    .applicationKey(APPLICATION_KEY)
                    .applicationSecret(APPLICATION_SECRET)
                    .environmentHost(APPLICATION_HOST)
                    .userId(userId)
                    .build();
        }
        /*sinchClient.setSupportMessaging(true);
        sinchClient.setSupportCalling(true);
        sinchClient.setSupportManagedPush(true);
        sinchClient.setSupportActiveConnectionInBackground(true);
        sinchClient.startListeningOnActiveConnection();*/
        return sinchClient;
    }
}
