package edu.re.estate.data.source.repository;

import androidx.annotation.NonNull;

import java.util.List;

import edu.re.estate.components.ExCallback;
import edu.re.estate.components.RetrofitBuilder;
import edu.re.estate.data.models.BaseResult;
import edu.re.estate.data.models.Token;
import edu.re.estate.data.models.User;
import edu.re.estate.data.request.LoginRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepositoryImpl implements AuthRepository {

    private AuthRepositoryImpl() {
        // throw new RuntimeException("No instance");
    }

    private static AuthRepositoryImpl instance;

    public static AuthRepositoryImpl getInstance() {
        if (instance == null) {
            instance = new AuthRepositoryImpl();
        }
        return instance;
    }

    @Override
    public void login(LoginRequest body, ExCallback<Token> callback) {
        RetrofitBuilder.authService.login(body).enqueue(new Callback<BaseResult<Token>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<Token>> call, @NonNull Response<BaseResult<Token>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<Token>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void myInfo(String accessToken, ExCallback<User> callback) {
        RetrofitBuilder.authService.getUser(accessToken).enqueue(new Callback<BaseResult<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<User>> call, @NonNull Response<BaseResult<User>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<User>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void getInfoUser(int accountId, ExCallback<User> callback) {
        RetrofitBuilder.authService.getInfoUserById(accountId).enqueue(new Callback<BaseResult<User>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<User>> call, @NonNull Response<BaseResult<User>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<User>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    @Override
    public void getUsers(String accessToken, ExCallback<List<User>> callback) {
        RetrofitBuilder.authService.getUsers(accessToken).enqueue(new Callback<BaseResult<List<User>>>() {
            @Override
            public void onResponse(@NonNull Call<BaseResult<List<User>>> call, @NonNull Response<BaseResult<List<User>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    callback.onResponse(response.body().getData());
                } else {
                    callback.onResponse(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<BaseResult<List<User>>> call, @NonNull Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }
}
