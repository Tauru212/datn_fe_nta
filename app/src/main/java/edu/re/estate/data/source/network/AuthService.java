package edu.re.estate.data.source.network;

import java.util.List;

import edu.re.estate.data.models.BaseResult;
import edu.re.estate.data.models.Token;
import edu.re.estate.data.models.User;
import edu.re.estate.data.request.LoginRequest;
import edu.re.estate.data.request.RegisterRequest;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AuthService {

//    @GET("api/users/{accountId}")
//    Call<User> getUser(@Path("accountId") int accountId);

    @GET("api/users")
    Call<BaseResult<User>> getUser(@Header("Authorization") String authorization);

    @GET("api/users/info/{account_id}")
    Call<BaseResult<User>> getInfoUserById(@Path("account_id") int accountId);

    @POST("api/login")
    Call<BaseResult<Token>> login(@Body LoginRequest request);

    @POST("api/register")
    Call<BaseResult<Token>> register(@Body RegisterRequest request);

    @Multipart
    @POST("api/file-upload/")
    Call<BaseResult<edu.re.estate.data.models.Path>> uploadFile(@Part MultipartBody.Part image);

    @POST("api/users/update")
    Call<BaseResult<User>> updateUser(@Body User user);

    @GET("api/users/get_others")
    Call<BaseResult<List<User>>> getUsers(@Header("Authorization") String authorization);
}
