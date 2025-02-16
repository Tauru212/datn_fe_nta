package edu.re.estate.presenters.post;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import edu.re.estate.R;
import edu.re.estate.databinding.ActivityMyPostListBinding;
import edu.re.estate.presenters.post.adapter.PostUserStateAdapter;

public class MyPostListActivity extends AppCompatActivity {

    private ActivityMyPostListBinding binding;
    private PostUserStateAdapter postStateAdapter;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMyPostListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Đang hiển thị"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Chờ duyệt"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Đã cho thuê"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Đã gỡ"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Bị từ chối"));

        postStateAdapter = new PostUserStateAdapter(this);
        binding.viewPager.setAdapter(postStateAdapter);
        binding.viewPager.setUserInputEnabled(false);

        binding.cardBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        reload();
    }

    public void reload() {
        postStateAdapter.reloadData(binding.tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
