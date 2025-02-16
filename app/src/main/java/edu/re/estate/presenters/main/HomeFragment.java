package edu.re.estate.presenters.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.re.estate.components.ExCallback;
import edu.re.estate.components.RetrofitBuilder;
import edu.re.estate.data.models.BaseResult;
import edu.re.estate.data.models.Post;
import edu.re.estate.data.request.FavoriteRequest;
import edu.re.estate.data.source.repository.PostRepositoryImpl;
import edu.re.estate.databinding.FragmentHomeBinding;
import edu.re.estate.presenters.auth.AuthActivity;
import edu.re.estate.presenters.main.adapter.PostHorizontalAdapter;
import edu.re.estate.presenters.main.adapter.PostVerticalAdapter;
import edu.re.estate.presenters.post.PostDetailActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static HomeFragment instance;

    public static HomeFragment getInstance() {
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }

    private FragmentHomeBinding binding;
    private PostVerticalAdapter adapter;
    private PostHorizontalAdapter highlightMarkAdapter;

    private String filter;
    private List<Post> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new PostVerticalAdapter(Collections.emptyList(), new PostVerticalAdapter.OnPostCallback() {
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
                            }

                            @Override
                            public void onFailure(Call<BaseResult<String>> call, Throwable throwable) {
                            }
                        });
                    } else {
                        RetrofitBuilder.postService.likePost(accessToken, new FavoriteRequest(post.getPostId())).enqueue(new Callback<BaseResult<String>>() {
                            @Override
                            public void onResponse(Call<BaseResult<String>> call, Response<BaseResult<String>> response) {
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
        highlightMarkAdapter = new PostHorizontalAdapter(Collections.emptyList(), new PostHorizontalAdapter.OnPostCallback() {
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
                            }

                            @Override
                            public void onFailure(Call<BaseResult<String>> call, Throwable throwable) {
                            }
                        });
                    } else {
                        RetrofitBuilder.postService.likePost(accessToken, new FavoriteRequest(post.getPostId())).enqueue(new Callback<BaseResult<String>>() {
                            @Override
                            public void onResponse(Call<BaseResult<String>> call, Response<BaseResult<String>> response) {
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

        binding.recyclerViewHotNew.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerViewHotNew.setHasFixedSize(true);
        binding.recyclerViewHotNew.setAdapter(highlightMarkAdapter);

        binding.edtSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String keyword = binding.edtSearch.getText().toString().trim();
                resetUIFilter();
                filter = null;

                if (getActivity() != null) {
                    InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.edtSearch.getWindowToken(), 0);
                }

                if (keyword.isEmpty()) {
                    adapter.setSource(data);
                } else {
                    ArrayList<Post> filters = new ArrayList<>();
                    for (Post item : data) {
                        if (item.getTitle().contains(keyword) || item.getDescription().contains(keyword)) {
                            filters.add(item);
                        }
                    }
                    adapter.setSource(filters);
                }
            }
            return false;
        });
        binding.ivHouse.setOnClickListener(v -> {
            resetUIFilter();
            if (TextUtils.isEmpty(filter)) {
                filter = "house";
                binding.ivCloseFilterHouse.setVisibility(View.VISIBLE);
            } else if (!filter.equals("house")) {
                filter = "house";
                binding.ivCloseFilterHouse.setVisibility(View.VISIBLE);
            } else {
                filter = null;
            }
            filterData();
        });
        binding.ivVilla.setOnClickListener(v -> {
            resetUIFilter();
            if (TextUtils.isEmpty(filter)) {
                filter = "villa";
                binding.ivCloseFilterVilla.setVisibility(View.VISIBLE);
            } else if (!filter.equals("villa")) {
                filter = "villa";
                binding.ivCloseFilterVilla.setVisibility(View.VISIBLE);
            } else {
                filter = null;
            }
            filterData();
        });

        binding.ivApartment.setOnClickListener(v -> {
            resetUIFilter();
            if (TextUtils.isEmpty(filter)) {
                filter = "apartment";
                binding.ivCloseFilterApartment.setVisibility(View.VISIBLE);
            } else if (!filter.equals("apartment")) {
                filter = "apartment";
                binding.ivCloseFilterApartment.setVisibility(View.VISIBLE);
            } else {
                filter = null;
            }
            filterData();
        });

        binding.ivBungalow.setOnClickListener(v -> {
            resetUIFilter();
            if (TextUtils.isEmpty(filter)) {
                filter = "bungalow";
                binding.ivCloseFilterBungalow.setVisibility(View.VISIBLE);
            } else if (!filter.equals("bungalow")) {
                filter = "bungalow";
                binding.ivCloseFilterBungalow.setVisibility(View.VISIBLE);
            } else {
                filter = null;
            }
            filterData();
        });
        refreshData();
    }

    private void filterData() {
        if (data == null || data.isEmpty()) {
            showEmpty();
        } else {
            if (TextUtils.isEmpty(filter)) {
                adapter.setSource(data);
            } else {
                ArrayList<Post> filters = new ArrayList<>();
                for (Post item : data) {
                    if (item.getReStateType().equals(filter)) {
                        filters.add(item);
                    }
                }
                if (filters.isEmpty()) {
                    showEmpty();
                } else {
                    hideEmpty();
                    adapter.setSource(filters);
                }
            }
        }
    }

    private void resetUIFilter() {
        binding.ivCloseFilterHouse.setVisibility(View.GONE);
        binding.ivCloseFilterVilla.setVisibility(View.GONE);
        binding.ivCloseFilterApartment.setVisibility(View.GONE);
        binding.ivCloseFilterBungalow.setVisibility(View.GONE);
    }

    public void refreshData() {
        loadData();
        getHighlightMark();
    }

    private void showEmpty() {
        binding.tvEmpty.setVisibility(View.VISIBLE);
        binding.recyclerView.setVisibility(View.GONE);
    }

    private void hideEmpty() {
        binding.tvEmpty.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    private void loadData() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);
        PostRepositoryImpl.getInstance().getAll(accessToken, new ExCallback<List<Post>>() {
            @Override
            public void onResponse(List<Post> data) {
                HomeFragment.this.data = data;
                filterData();
            }

            @Override
            public void onFailure(Throwable var2) {
                showEmpty();
            }
        });
    }

    public void getHighlightMark() {
        PostRepositoryImpl.getInstance().getAllHighLightMarkPost(new ExCallback<List<Post>>() {
            @Override
            public void onResponse(List<Post> data) {
                if (data != null) {
                    highlightMarkAdapter.setSource(data);
                    if (data.isEmpty()) {
                        binding.layoutHotNew.setVisibility(View.GONE);
                    } else {
                        binding.layoutHotNew.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.layoutHotNew.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Throwable var2) {
                binding.layoutHotNew.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
