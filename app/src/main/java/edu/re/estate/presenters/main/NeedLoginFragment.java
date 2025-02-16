package edu.re.estate.presenters.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.re.estate.databinding.FragmentNeedLoginBinding;
import edu.re.estate.presenters.auth.AuthActivity;
import edu.re.estate.presenters.post.CreatePostActivity;

public class NeedLoginFragment extends Fragment {

    private static NeedLoginFragment instance;

    public static NeedLoginFragment getInstance() {
        if (instance == null) {
            instance = new NeedLoginFragment();
        }
        return instance;
    }

    private FragmentNeedLoginBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNeedLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AuthActivity.class);
            intent.putExtra("EXTRA_IS_FROM_MAIN", true);
            startActivity(intent);
        });
        binding.menuMoreCreatePost.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AuthActivity.class);
            intent.putExtra("EXTRA_IS_FROM_MAIN", true);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
