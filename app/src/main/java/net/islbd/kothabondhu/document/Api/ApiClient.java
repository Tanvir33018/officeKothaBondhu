
package net.islbd.kothabondhu.document.Api;

import net.islbd.kothabondhu.document.DocumentActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiClient {
    String BASE_URL = "http://kothabondhu.com/kothaapi/api/";
    @GET
    Call<ArrayList<MyContent>> getMyContent(@Url String url);
}
