package net.islbd.kothabondhu.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wahid.sadique on 8/30/2017.
 */

public class ApiClient {
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;





    public static Retrofit getClient(String baseUrl, Long requestTimeout) {
        /*Gson gson = new GsonBuilder()
                .setLenient()
                .create();*/
        if (retrofit == null) {
            retrofit = new Retrofit
                    .Builder()
                    .client(getOkHttpClient(requestTimeout))
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getOkHttpClient(Long requestTimeout) {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient()
                    .newBuilder()
                    .connectTimeout(requestTimeout, TimeUnit.MINUTES)
                    .readTimeout(requestTimeout, TimeUnit.MINUTES)
                    .writeTimeout(requestTimeout, TimeUnit.MINUTES)
                    .build();
        }
        return okHttpClient;
    }
}
