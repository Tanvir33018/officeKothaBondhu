package net.islbd.kothabondhu.document.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtilities {
    private ApiUtilities(){}
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient = null;
    private static ApiClient apiClient = null;
    private static OkHttpClient getOkHttpClient(){
        if(okHttpClient == null){
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        return okHttpClient;
    }
    public static Retrofit getRetrofit(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiClient.BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiClient getApiClient(){
        if(apiClient == null) apiClient = getRetrofit().create(ApiClient.class);
        return apiClient;
    }
}
