package edu.re.estate.components;

import edu.re.estate.data.source.network.AuthService;
import edu.re.estate.data.source.network.PostService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    public final static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(NConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public final static AuthService authService = retrofit.create(AuthService.class);

    public final static PostService postService = retrofit.create(PostService.class);
}
