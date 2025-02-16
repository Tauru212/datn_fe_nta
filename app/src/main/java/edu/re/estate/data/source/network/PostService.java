package edu.re.estate.data.source.network;

import java.util.List;

import edu.re.estate.data.models.BaseResult;
import edu.re.estate.data.models.Post;
import edu.re.estate.data.models.TotalPost;
import edu.re.estate.data.request.FavoriteRequest;
import edu.re.estate.data.request.PostRequest;
import edu.re.estate.data.request.PutImageRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostService {

    @GET("api/post/get_all")
    Call<BaseResult<List<Post>>> getAllPost(@Header("Authorization") String accessToken);

    @POST("api/post/")
    Call<BaseResult<Integer>> createPost(@Header("Authorization") String accessToken, @Body PostRequest body);

    @POST("api/post/images")
    Call<BaseResult<String>> putImages(@Body PutImageRequest body);

    @POST("api/favorite/like")
    Call<BaseResult<String>> likePost(@Header("Authorization") String accessToken, @Body FavoriteRequest body);

    @POST("api/favorite/un_like")
    Call<BaseResult<String>> unLikePost(@Header("Authorization") String accessToken, @Body FavoriteRequest body);

    @GET("api/post")
    Call<BaseResult<List<Post>>> getPostsByStatus(@Query("status") String status);

    @GET("api/post")
    Call<BaseResult<List<Post>>> getMyPostsByStatus(@Query("account_id") int accountId, @Query("status") String status);

    @PUT("api/post/approved")
    Call<BaseResult<String>> approvedPost(@Header("Authorization") String accessToken, @Body FavoriteRequest body);

    @PUT("api/post/removePost")
    Call<BaseResult<String>> removePost(@Header("Authorization") String accessToken, @Body FavoriteRequest body);

    @PUT("api/post/processing")
    Call<BaseResult<String>> processingPost(@Header("Authorization") String accessToken, @Body FavoriteRequest body);

    @PUT("api/post/advertisement")
    Call<BaseResult<String>> advertisementPost(@Header("Authorization") String accessToken, @Body FavoriteRequest body);

    @PUT("api/post/rented")
    Call<BaseResult<String>> rentedPost(@Header("Authorization") String accessToken, @Body FavoriteRequest body);

    @PUT("api/post/refuse")
    Call<BaseResult<String>> refusePost(@Header("Authorization") String accessToken, @Body FavoriteRequest body);

    @PUT("api/post/view/{post_id}")
    Call<BaseResult<String>> tickView(@Path("post_id") int postId);

    @POST("api/highlight_mark/add")
    Call<BaseResult<String>> addHighLightMark(@Header("Authorization") String accessToken, @Body FavoriteRequest body);

    @POST("api/highlight_mark/remove")
    Call<BaseResult<String>> removeHighLightMark(@Header("Authorization") String accessToken, @Body FavoriteRequest body);

    @GET("api/highlight_mark/")
    Call<BaseResult<List<Post>>> getAllHighLightMark();

    @GET("api/post/count")
    Call<BaseResult<TotalPost>> statisticalPost();

    @GET("api/post/count/highlight_mark")
    Call<BaseResult<Integer>> statisticalPostHighlightMark();

    @GET("api/post/count/views")
    Call<BaseResult<String>> statisticalPostViews();

    @GET("api/post/count/highlight_mark/views")
    Call<BaseResult<String>> statisticalPostHighlightMarkViews();
}
