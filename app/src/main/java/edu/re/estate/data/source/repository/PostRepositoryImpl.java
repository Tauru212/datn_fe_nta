package edu.re.estate.data.source.repository;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.List;

import edu.re.estate.components.ExCallback;
import edu.re.estate.components.RetrofitBuilder;
import edu.re.estate.data.models.BaseResult;
import edu.re.estate.data.models.Post;
import edu.re.estate.data.models.Token;
import edu.re.estate.data.models.TotalPost;
import edu.re.estate.data.request.FavoriteRequest;
import edu.re.estate.data.request.PostRequest;
import edu.re.estate.data.request.PutImageRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostRepositoryImpl implements PostRepository {

    private PostRepositoryImpl() {
        // throw new RuntimeException("No instance");
    }

    private static PostRepositoryImpl instance;

    public static PostRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new PostRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void getAll(String accessToken, ExCallback<List<Post>> callback) {
        RetrofitBuilder.postService.getAllPost(accessToken).enqueue(new Callback<BaseResult<List<Post>>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<List<Post>>> call, @NonNull Response<BaseResult<List<Post>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<List<Post>>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void post(String accessToken, PostRequest request, ExCallback<Integer> callback) {
        RetrofitBuilder.postService.createPost(accessToken, request).enqueue(new Callback<BaseResult<Integer>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<Integer>> call, @NonNull Response<BaseResult<Integer>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<Integer>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void putImages(PutImageRequest request, ExCallback<String> callback) {
        RetrofitBuilder.postService.putImages(request).enqueue(new Callback<BaseResult<String>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<String>> call, @NonNull Response<BaseResult<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse("");
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<String>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void getPostsByStatus(String status, ExCallback<List<Post>> callback) {
        RetrofitBuilder.postService.getPostsByStatus(status).enqueue(new Callback<BaseResult<List<Post>>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<List<Post>>> call, @NonNull Response<BaseResult<List<Post>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<List<Post>>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void getMyPostByStatus(int accountId, String status, ExCallback<List<Post>> callback) {
        RetrofitBuilder.postService.getMyPostsByStatus(accountId, status).enqueue(new Callback<BaseResult<List<Post>>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<List<Post>>> call, @NonNull Response<BaseResult<List<Post>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<List<Post>>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void approvedPost(String accessToken, int postId, ExCallback<String> callback) {
        RetrofitBuilder.postService.approvedPost(accessToken, new FavoriteRequest(postId)).enqueue(new Callback<BaseResult<String>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<String>> call, @NonNull Response<BaseResult<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<String>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void removePost(String accessToken, int postId, ExCallback<String> callback) {
        RetrofitBuilder.postService.removePost(accessToken, new FavoriteRequest(postId)).enqueue(new Callback<BaseResult<String>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<String>> call, @NonNull Response<BaseResult<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<String>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void processingPost(String accessToken, int postId, ExCallback<String> callback) {
        RetrofitBuilder.postService.processingPost(accessToken, new FavoriteRequest(postId)).enqueue(new Callback<BaseResult<String>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<String>> call, @NonNull Response<BaseResult<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<String>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void advertisementPost(String accessToken, int postId, ExCallback<String> callback) {
        RetrofitBuilder.postService.advertisementPost(accessToken, new FavoriteRequest(postId)).enqueue(new Callback<BaseResult<String>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<String>> call, @NonNull Response<BaseResult<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<String>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void rentedPost(String accessToken, int postId, ExCallback<String> callback) {
        RetrofitBuilder.postService.rentedPost(accessToken, new FavoriteRequest(postId)).enqueue(new Callback<BaseResult<String>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<String>> call, @NonNull Response<BaseResult<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<String>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void refusePost(String accessToken, int postId, ExCallback<String> callback) {
        RetrofitBuilder.postService.refusePost(accessToken, new FavoriteRequest(postId)).enqueue(new Callback<BaseResult<String>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<String>> call, @NonNull Response<BaseResult<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<String>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void tickView(int postId) {
        RetrofitBuilder.postService.tickView(postId).enqueue(new Callback<BaseResult<String>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<String>> call, @NonNull Response<BaseResult<String>> response) {

            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<String>> call, @NonNull Throwable throwable) {

            }
        });
    }

    @Override
    public void highLightMarkPost(String accessToken, int postId, ExCallback<String> callback) {
        RetrofitBuilder.postService.addHighLightMark(accessToken, new FavoriteRequest(postId)).enqueue(new Callback<BaseResult<String>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<String>> call, @NonNull Response<BaseResult<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<String>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void removeHighLightMarkPost(String accessToken, int postId, ExCallback<String> callback) {
        RetrofitBuilder.postService.removeHighLightMark(accessToken, new FavoriteRequest(postId)).enqueue(new Callback<BaseResult<String>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<String>> call, @NonNull Response<BaseResult<String>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<String>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void getAllHighLightMarkPost(ExCallback<List<Post>> callback) {
        RetrofitBuilder.postService.getAllHighLightMark().enqueue(new Callback<BaseResult<List<Post>>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<List<Post>>> call, @NonNull Response<BaseResult<List<Post>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<List<Post>>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void statisticalPost(ExCallback<TotalPost> callback) {
        RetrofitBuilder.postService.statisticalPost().enqueue(new Callback<BaseResult<TotalPost>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<TotalPost>> call, @NonNull Response<BaseResult<TotalPost>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<TotalPost>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void statisticalPostHighlightMark(ExCallback<Integer> callback) {
        RetrofitBuilder.postService.statisticalPostHighlightMark().enqueue(new Callback<BaseResult<Integer>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<Integer>> call, @NonNull Response<BaseResult<Integer>> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isSuccess()
                ) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(0);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<Integer>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void statisticalPostViews(ExCallback<Integer> callback) {
        RetrofitBuilder.postService.statisticalPostViews().enqueue(new Callback<BaseResult<String>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<String>> call, @NonNull Response<BaseResult<String>> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isSuccess()
                        && !TextUtils.isEmpty(response.body().getData())
                ) {
                    callback.onResponse(Integer.parseInt(response.body().getData()));
                } else {
                    callback.onResponse(0);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<String>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void statisticalPostHighlightMarkViews(ExCallback<Integer> callback) {
        RetrofitBuilder.postService.statisticalPostHighlightMarkViews().enqueue(new Callback<BaseResult<String>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<String>> call, @NonNull Response<BaseResult<String>> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isSuccess()
                        && !TextUtils.isEmpty(response.body().getData())
                ) {
                    callback.onResponse(Integer.parseInt(response.body().getData()));
                } else {
                    callback.onResponse(0);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<String>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }
}
