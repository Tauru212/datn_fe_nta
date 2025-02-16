package edu.re.estate.presenters.admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Collections;
import java.util.List;

import edu.re.estate.components.ExCallback;
import edu.re.estate.data.models.User;
import edu.re.estate.data.source.repository.AuthRepositoryImpl;
import edu.re.estate.databinding.FragmentUserManagerBinding;
import edu.re.estate.presenters.admin.adapter.AdminUserAdapter;

public class UserManagerFragment extends Fragment {

    private FragmentUserManagerBinding binding;
    private AdminUserAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUserManagerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new AdminUserAdapter(Collections.emptyList(), new AdminUserAdapter.OnAdminUserCallback() {
            @Override
            public void onClickUser(User user) {

            }
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
        loadData();
    }

    public void loadData() {
        ((AdminActivity) requireActivity()).showLoading();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        AuthRepositoryImpl.getInstance().getUsers(accessToken, new ExCallback<List<User>>() {
            @Override
            public void onResponse(List<User> data) {
                ((AdminActivity) requireActivity()).hideLoading();
                if (data != null) {
                    binding.tvEmpty.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    adapter.setSource(data);
                } else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Throwable var2) {
                ((AdminActivity) requireActivity()).hideLoading();
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
