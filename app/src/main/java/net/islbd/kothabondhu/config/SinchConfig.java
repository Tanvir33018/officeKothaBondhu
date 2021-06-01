package net.islbd.kothabondhu.config;

import android.content.Context;

import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;

import net.islbd.kothabondhu.service.SinchService;

/**
 * Created by User on 10/9/2019.
 */

public class SinchConfig {
    private SinchClient sinchClient = null;
    private static SinchConfig sinchConfig = null;
/*
    public static final String APPLICATION_KEY = "3292e9fe-1055-4b58-be38-ca44a295813e"; //"57ec7cb7-4d22-43e5-9e25-aeefab22428e";
    public static final String APPLICATION_SECRET = "0M6kXNxmrEW5V+cvM+lm3Q=="; //"g+PQN4QB5U2Q2EFNU0BNEw==";
    public static final String APPLICATION_HOST = "clientapi.sinch.com"; //"sandbox.sinch.com" ;
*/

    public static SinchConfig getInstance() {
        if (sinchConfig == null) sinchConfig = new SinchConfig();
        return sinchConfig;
    }

    /*public SinchClient getClient(Context context, String userId) {
        if (sinchClient == null) {
            sinchClient = Sinch.getSinchClientBuilder().context(context)
                    .applicationKey(APPLICATION_KEY)
                    .applicationSecret(APPLICATION_SECRET)
                    .environmentHost(APPLICATION_HOST)
                    .userId(userId)
                    .build();
        }
        //sinchClient.setSupportMessaging(true);
        *//*sinchClient.setSupportCalling(true);
        sinchClient.setSupportManagedPush(true);
        sinchClient.setSupportActiveConnectionInBackground(true);
        sinchClient.startListeningOnActiveConnection();*//*
        return sinchClient;
    }*/
}
