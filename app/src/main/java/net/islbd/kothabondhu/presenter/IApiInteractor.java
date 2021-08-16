package net.islbd.kothabondhu.presenter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import net.islbd.kothabondhu.model.pojo.AamarPayPostInfo;
import net.islbd.kothabondhu.model.pojo.Agent;
import net.islbd.kothabondhu.model.pojo.AgentDetails;
import net.islbd.kothabondhu.model.pojo.AgentQuery;
import net.islbd.kothabondhu.model.pojo.BuyPack;
import net.islbd.kothabondhu.model.pojo.CallHistoryDetails;
import net.islbd.kothabondhu.model.pojo.CallHistoryQuery;
import net.islbd.kothabondhu.model.pojo.Communication;
import net.islbd.kothabondhu.model.pojo.CommunicationDetails;
import net.islbd.kothabondhu.model.pojo.CommunicationStatus;
import net.islbd.kothabondhu.model.pojo.MyDuration;
import net.islbd.kothabondhu.model.pojo.NagadResponse;
import net.islbd.kothabondhu.model.pojo.PackageInfo;
import net.islbd.kothabondhu.model.pojo.PackageInfoQuery;
import net.islbd.kothabondhu.model.pojo.PackageStatusInfo;
import net.islbd.kothabondhu.model.pojo.PackageStatusQuery;
import net.islbd.kothabondhu.model.pojo.RegisterInfo;
import net.islbd.kothabondhu.model.pojo.StatusInfo;
import net.islbd.kothabondhu.model.pojo.UserAccountInfo;
import net.islbd.kothabondhu.model.pojo.UserDetails;
import net.islbd.kothabondhu.model.pojo.UserDetailsSecond;
import net.islbd.kothabondhu.model.pojo.UserDuration;
import net.islbd.kothabondhu.model.pojo.UserGmailInfo;
import net.islbd.kothabondhu.model.pojo.UserQuery;
import net.islbd.kothabondhu.model.pojo.UserStatusDetails;

/**
 * Created by wahid.sadique on 8/30/2017.
 */

public interface IApiInteractor {
    @GET("api/agent.php")
    Call<List<Agent>> getAgentList();

    @GET("api/getNP_Status.php?user_orderid=&user_amount=")
    Call<NagadResponse> getNagadResponse(@Query("user_orderid") String id, @Query("user_amount")String tk);

    @POST("api/getDuration.php")
    Call<MyDuration> getMyDuration(@Body UserDuration userDuration);

    @POST("lumen-bc/public/api/communications")
    Call<CommunicationDetails> postCommunication(@Body Communication communication);

    @GET("lumen-bc/public/api/communications/{id}")
    Call<CommunicationDetails> findCommunicationRequest(@Path(value = "id", encoded = true) Integer phone);

    @POST("lumen-bc/public/api/communications/status")
    Call<CommunicationDetails> updateCommunicationStatus(@Body CommunicationStatus communicationStatus);

    @POST("api/getAllAgentProfile.php")
    Call<List<AgentDetails>> getAgentList(@Body AgentQuery agentQuery);

    @POST("api/getAgentProfile.php")
    Call<AgentDetails> getAgentDetails(@Body AgentQuery agentQuery);

    @POST("api/getPackage.php")
    Call<PackageInfo> getPackageDetails(@Body PackageInfoQuery packageInfoQuery);

    @POST("api/getPackage.php")
    Call<List<PackageInfo>> getPackageList(@Body PackageInfoQuery packageInfoQuery);

    @POST("api/setBuyPack.php")
    Call<PackageStatusInfo> setBuyPack(@Body BuyPack buyPack);

    @POST("api/setCallHistory.php")
    Call<StatusInfo> setCallHistory(@Body CallHistoryDetails callHistoryDetails);

    @POST("api/setBuyLog.php")
    Call<AamarPayPostInfo> setAamarPay(@Body AamarPayPostInfo aamarPayPostInfo);

    @POST("api/setUserRegistration.php")
    Call<StatusInfo> setUserRegistration(@Body UserDetails userDetails);

    @POST("api/setUserRegUp.php")
    Call<StatusInfo> setUserRegistrationSecond(@Body UserDetailsSecond userDetailsSecond);

    @POST("api/getUserStatus.php")
    Call<UserStatusDetails> getUserStatus(@Body UserQuery userQuery);

    @POST("api/getPackStatus.php")
    Call<PackageStatusInfo> getPackageStatus(@Body PackageStatusQuery packageStatusQuery);

    @POST("api/getUserCallHistory.php")
    Call<List<CallHistoryDetails>> getCallHistoryList(@Body CallHistoryQuery callHistoryQuery);

    @POST("api/getUserAccountInfo.php")
    Call<UserAccountInfo> getUserAccountInfo(@Body UserQuery userQuery);

    @POST("api/setUserRegistration.php")
    Call<RegisterInfo> getUserAccountInfoGMail(@Body UserGmailInfo userGmailInfo);
}
