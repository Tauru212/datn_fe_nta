package edu.re.estate.presenters.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import edu.re.estate.R;
import edu.re.estate.databinding.ActivityAdminBinding;
import edu.re.estate.presenters.main.MainActivity;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;

    private DashboardFragment dashboardFragment;
    private UserManagerFragment userManagerFragment;
    private PostManagerFragment postManagerFragment;
    private HighlightMarkFragment highlightMarkFragment;

    private Fragment currentFragment = new Fragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dashboardFragment = new DashboardFragment();
        userManagerFragment = new UserManagerFragment();
        postManagerFragment = new PostManagerFragment();
        highlightMarkFragment = new HighlightMarkFragment();

        binding.ivMenu.setOnClickListener(v -> {
            binding.drawerLayout.openDrawer(binding.layoutMenu);
        });

        getSupportFragmentManager().beginTransaction()
                .add(binding.container.getId(), dashboardFragment)
                .hide(dashboardFragment)
                .add(binding.container.getId(), userManagerFragment)
                .hide(userManagerFragment)
                .add(binding.container.getId(), postManagerFragment)
                .hide(postManagerFragment)
                .add(binding.container.getId(), highlightMarkFragment)
                .hide(highlightMarkFragment)
                .commitAllowingStateLoss();
        showDashboard();

        binding.menu.layoutDashboard.setOnClickListener(v -> {
            showDashboard();
            binding.drawerLayout.close();
        });
        binding.menu.layoutUser.setOnClickListener(v -> {
            showUserManager();
            binding.drawerLayout.close();
        });
        binding.menu.layoutPost.setOnClickListener(v -> {
            showPostManager();
            binding.drawerLayout.close();
        });
        binding.menu.layoutHighlightMark.setOnClickListener(v -> {
            showHighlightMark();
            binding.drawerLayout.close();
        });
        binding.menu.layoutReturnApp.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private void showDashboard() {
        getSupportFragmentManager().beginTransaction()
                .hide(currentFragment)
                .show(dashboardFragment)
                .commitAllowingStateLoss();
        currentFragment = dashboardFragment;
        dashboardFragment.reload();

        binding.tvTitle.setText("Dashboard");
    }

    private void showUserManager() {
        getSupportFragmentManager().beginTransaction()
                .hide(currentFragment)
                .show(userManagerFragment)
                .commitAllowingStateLoss();
        userManagerFragment.loadData();
        currentFragment = userManagerFragment;

        binding.tvTitle.setText("Quản lý người dùng");
    }

    private void showPostManager() {
        getSupportFragmentManager().beginTransaction()
                .hide(currentFragment)
                .show(postManagerFragment)
                .commitAllowingStateLoss();
        currentFragment = postManagerFragment;

        binding.tvTitle.setText("Quản lý bài đăng");
    }

    private void showHighlightMark() {
        getSupportFragmentManager().beginTransaction()
                .hide(currentFragment)
                .show(highlightMarkFragment)
                .commitAllowingStateLoss();
        currentFragment = highlightMarkFragment;
        highlightMarkFragment.reloadData();
        binding.tvTitle.setText("Tin nổi bật");
    }

    public void showLoading() {
        binding.layoutLoading.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        binding.layoutLoading.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentFragment != null) {
            if (currentFragment instanceof PostManagerFragment) {
                ((PostManagerFragment) currentFragment).reload();
            } else if (currentFragment instanceof HighlightMarkFragment) {
                ((HighlightMarkFragment) currentFragment).reloadData();
            } else if (currentFragment instanceof DashboardFragment) {
                ((DashboardFragment) currentFragment).reload();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
