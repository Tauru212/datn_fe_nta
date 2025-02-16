package edu.re.estate.presenters.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import edu.re.estate.R;
import edu.re.estate.components.ExCallback;
import edu.re.estate.components.RetrofitBuilder;
import edu.re.estate.data.models.BaseResult;
import edu.re.estate.data.models.Post;
import edu.re.estate.data.request.FavoriteRequest;
import edu.re.estate.data.source.repository.PostRepositoryImpl;
import edu.re.estate.databinding.FragmentFavoriteBinding;
import edu.re.estate.presenters.auth.AuthActivity;
import edu.re.estate.presenters.main.adapter.PostVerticalAdapter;
import edu.re.estate.presenters.post.PostDetailActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteFragment extends Fragment {

    private static FavoriteFragment instance;

    public static FavoriteFragment getInstance() {
        if (instance == null) {
            instance = new FavoriteFragment();
        }
        return instance;
    }

    private FragmentFavoriteBinding binding;
    private PostVerticalAdapter adapter;
    ArrayList<Post> result = new ArrayList<>();

    private String sFilter = "all";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvNeedLogin.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AuthActivity.class);
            intent.putExtra("EXTRA_IS_FROM_MAIN", true);
            startActivity(intent);
        });
        adapter = new PostVerticalAdapter(result, new PostVerticalAdapter.OnPostCallback() {

            @Override
            public void onFavorite(Post post) {
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
                String accessToken = sharedPreferences.getString("access_token", null);
                if (TextUtils.isEmpty(accessToken)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setCancelable(false).setTitle("Thông báo").setMessage("Cần đăng nhập để thực hiện chức năng này!").setPositiveButton("Đăng nhập", (dialog, which) -> {
                        startActivity(new Intent(requireContext(), AuthActivity.class));
                    }).setNegativeButton("Thoát", (dialog, which) -> {
                        dialog.dismiss();
                    }).create();
                    alertDialog.show();
                } else {
                    if (post.isLiked()) {
                        RetrofitBuilder.postService.unLikePost(accessToken, new FavoriteRequest(post.getPostId())).enqueue(new Callback<BaseResult<String>>() {
                            @Override
                            public void onResponse(Call<BaseResult<String>> call, Response<BaseResult<String>> response) {
                                refreshData();
                            }

                            @Override
                            public void onFailure(Call<BaseResult<String>> call, Throwable throwable) {
                            }
                        });
                    } else {
                        RetrofitBuilder.postService.likePost(accessToken, new FavoriteRequest(post.getPostId())).enqueue(new Callback<BaseResult<String>>() {
                            @Override
                            public void onResponse(Call<BaseResult<String>> call, Response<BaseResult<String>> response) {
                                refreshData();
                            }

                            @Override
                            public void onFailure(Call<BaseResult<String>> call, Throwable throwable) {
                            }
                        });
                    }
                }
            }

            @Override
            public void onDetail(Post post) {
                Intent intent = new Intent(requireActivity(), PostDetailActivity.class);
                intent.putExtra("post", post);
                startActivity(intent);
            }
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);
        refreshData();

        binding.tvAll.setOnClickListener(v -> {
            sFilter = "all";
            reset();
            binding.tvAll.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.tvAll.setBackgroundResource(R.drawable.bg_re_state_type_v3);
            adapter.setSource(result);

            if (result.isEmpty()) {
                binding.tvEmpty.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            } else {
                binding.tvEmpty.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                adapter.setSource(result);
            }
        });
        binding.tvHouse.setOnClickListener(v -> {
            sFilter = "house";
            reset();
            binding.tvHouse.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.tvHouse.setBackgroundResource(R.drawable.bg_re_state_type_v3);

            ArrayList<Post> filter = new ArrayList<>();
            for (Post post : result) {
                if (post.getReStateType().equals(sFilter)) {
                    filter.add(post);
                }
            }

            if (filter.isEmpty()) {
                binding.tvEmpty.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            } else {
                binding.tvEmpty.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                adapter.setSource(filter);
            }
        });
        binding.tvVilla.setOnClickListener(v -> {
            sFilter = "villa";
            reset();
            binding.tvVilla.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.tvVilla.setBackgroundResource(R.drawable.bg_re_state_type_v3);

            ArrayList<Post> filter = new ArrayList<>();
            for (Post post : result) {
                if (post.getReStateType().equals(sFilter)) {
                    filter.add(post);
                }
            }

            if (filter.isEmpty()) {
                binding.tvEmpty.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            } else {
                binding.tvEmpty.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                adapter.setSource(filter);
            }
        });
        binding.tvApartment.setOnClickListener(v -> {
            sFilter = "apartment";
            reset();
            binding.tvApartment.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.tvApartment.setBackgroundResource(R.drawable.bg_re_state_type_v3);

            ArrayList<Post> filter = new ArrayList<>();
            for (Post post : result) {
                if (post.getReStateType().equals(sFilter)) {
                    filter.add(post);
                }
            }

            if (filter.isEmpty()) {
                binding.tvEmpty.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            } else {
                binding.tvEmpty.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                adapter.setSource(filter);
            }
        });
        binding.tvBungalow.setOnClickListener(v -> {
            sFilter = "bungalow";
            reset();
            binding.tvBungalow.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.tvBungalow.setBackgroundResource(R.drawable.bg_re_state_type_v3);

            ArrayList<Post> filter = new ArrayList<>();
            for (Post post : result) {
                if (post.getReStateType().equals(sFilter)) {
                    filter.add(post);
                }
            }

            if (filter.isEmpty()) {
                binding.tvEmpty.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            } else {
                binding.tvEmpty.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
                adapter.setSource(filter);
            }
        });
    }

    private void reset() {
        int black = ContextCompat.getColor(requireContext(), R.color.black);
        binding.tvAll.setTextColor(black);
        binding.tvAll.setBackgroundResource(R.drawable.bg_re_state_type_v2);
        binding.tvHouse.setTextColor(black);
        binding.tvHouse.setBackgroundResource(R.drawable.bg_re_state_type_v2);
        binding.tvVilla.setTextColor(black);
        binding.tvVilla.setBackgroundResource(R.drawable.bg_re_state_type_v2);
        binding.tvApartment.setTextColor(black);
        binding.tvApartment.setBackgroundResource(R.drawable.bg_re_state_type_v2);
        binding.tvBungalow.setTextColor(black);
        binding.tvBungalow.setBackgroundResource(R.drawable.bg_re_state_type_v2);
    }

    public void refreshData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);
        if (accessToken == null) {
            binding.tvNeedLogin.setVisibility(View.VISIBLE);
            binding.layout.setVisibility(View.GONE);
        } else {
            binding.tvNeedLogin.setVisibility(View.GONE);
            binding.layout.setVisibility(View.VISIBLE);
            loadData();
        }
    }

    private void loadData() {
        result.clear();
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);
        PostRepositoryImpl.getInstance().getAll(accessToken, new ExCallback<List<Post>>() {
            @Override
            public void onResponse(List<Post> data) {
                if (data != null) {
                    ArrayList<Post> filter = new ArrayList<>();
                    for (Post post : data) {
                        if (post.isLiked()) {
                            result.add(post);

                            if (sFilter.equals("all")) {
                                filter.add(post);
                            } else {
                                if (post.getReStateType().equals(sFilter)) {
                                    filter.add(post);
                                }
                            }
                        }
                    }
                    if (filter.isEmpty()) {
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                        binding.recyclerView.setVisibility(View.GONE);
                    } else {
                        binding.tvEmpty.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        adapter.setSource(filter);
                    }

                } else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Throwable var2) {
                binding.tvEmpty.setVisibility(View.VISIBLE);
                binding.recyclerView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
