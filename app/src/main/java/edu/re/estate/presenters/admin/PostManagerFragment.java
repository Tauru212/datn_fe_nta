package edu.re.estate.presenters.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import edu.re.estate.databinding.FragmentPostManagerBinding;
import edu.re.estate.presenters.admin.adapter.PostStateAdapter;
import edu.re.estate.presenters.post.CreatePostActivity;

public class PostManagerFragment extends Fragment {

    private FragmentPostManagerBinding binding;
    private PostStateAdapter postStateAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPostManagerBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        postStateAdapter = new PostStateAdapter(this);
        binding.viewPager.setAdapter(postStateAdapter);
        binding.viewPager.setUserInputEnabled(false);

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Chờ duyệt"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Đã duyệt"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Từ chối"));

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                postStateAdapter.reloadData(position);
                binding.viewPager.setCurrentItem(position, false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position));
            }
        });
        binding.btnCreatePost.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), CreatePostActivity.class);
            startActivity(intent);
        });
    }

    public void reload() {
        postStateAdapter.reloadData(binding.tabLayout.getSelectedTabPosition());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
