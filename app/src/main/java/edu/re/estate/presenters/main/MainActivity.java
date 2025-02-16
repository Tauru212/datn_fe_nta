package edu.re.estate.presenters.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import edu.re.estate.R;
import edu.re.estate.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private Fragment currentFragment;
    private int currentSelected = 0;

    public Location currentLocation;
    private LocationManager locationManager;

    @SuppressLint("MissingPermission")
    private final ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        Boolean fineLocationGranted = result.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false);
        Boolean coarseLocationGranted = result.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false);

        LocationListener networkLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                currentLocation = location;
            }
        };
        if (Boolean.TRUE.equals(fineLocationGranted) && Boolean.TRUE.equals(coarseLocationGranted)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, networkLocationListener);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        locationPermissionRequest.launch(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION});

        getSupportFragmentManager().beginTransaction()
                .add(binding.container.getId(), HomeFragment.getInstance())
                .hide(HomeFragment.getInstance())
                .add(binding.container.getId(), ExploreFragment.getInstance())
                .hide(ExploreFragment.getInstance())
                .add(binding.container.getId(), FavoriteFragment.getInstance())
                .hide(FavoriteFragment.getInstance())
                .add(binding.container.getId(), GroupFragment.getInstance())
                .hide(GroupFragment.getInstance())
                .add(binding.container.getId(), ProfileFragment.getInstance())
                .hide(ProfileFragment.getInstance())
                .add(binding.container.getId(), NeedLoginFragment.getInstance())
                .hide(NeedLoginFragment.getInstance())
                .commit();

        binding.layoutHome.setOnClickListener(v -> {
            if (currentSelected == 0) return;
            currentSelected = 0;
            getSupportFragmentManager().beginTransaction()
                    .hide(currentFragment)
                    .show(HomeFragment.getInstance())
                    .commit();
            HomeFragment.getInstance().refreshData();
            currentFragment = HomeFragment.getInstance();
            selectedMenu(0);

            changeStatusBarWhite();
        });
        binding.layoutExplore.setOnClickListener(v -> {
            if (currentSelected == 1) return;
            currentSelected = 1;
            getSupportFragmentManager().beginTransaction()
                    .hide(currentFragment)
                    .show(ExploreFragment.getInstance())
                    .commit();
            currentFragment = ExploreFragment.getInstance();
            selectedMenu(1);
            ExploreFragment.getInstance().reloadData();
            changeStatusBarWhite();
        });
        binding.layoutFavorite.setOnClickListener(v -> {
            if (currentSelected == 2) return;
            currentSelected = 2;
            getSupportFragmentManager().beginTransaction()
                    .hide(currentFragment)
                    .show(FavoriteFragment.getInstance())
                    .commit();
            FavoriteFragment.getInstance().refreshData();
            currentFragment = FavoriteFragment.getInstance();
            selectedMenu(2);

            changeStatusBarWhite();
        });
        binding.layoutChat.setOnClickListener(v -> {
            if (currentSelected == 3) return;
            currentSelected = 3;
            getSupportFragmentManager().beginTransaction()
                    .hide(currentFragment)
                    .show(GroupFragment.getInstance())
                    .commit();
            currentFragment = GroupFragment.getInstance();
            GroupFragment.getInstance().loadTopics();
            selectedMenu(3);

            changeStatusBarMain();
        });
        binding.layoutAccount.setOnClickListener(v -> {
            if (currentSelected == 4) return;
            currentSelected = 4;

            SharedPreferences sharedPreferences = getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
            String accessToken = sharedPreferences.getString("access_token", null);
            if (TextUtils.isEmpty(accessToken)) {
                getSupportFragmentManager().beginTransaction()
                        .hide(currentFragment)
                        .show(NeedLoginFragment.getInstance())
                        .commit();
                currentFragment = NeedLoginFragment.getInstance();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .hide(currentFragment)
                        .show(ProfileFragment.getInstance())
                        .commit();
                currentFragment = ProfileFragment.getInstance();
            }
            selectedMenu(4);
            changeStatusBarWhite();
        });
        selectedMenu(0);

        // default
        getSupportFragmentManager().beginTransaction()
                .show(HomeFragment.getInstance())
                .commit();
        currentFragment = HomeFragment.getInstance();
    }

    private void changeStatusBarWhite() {
        Window window = getWindow();
        if (window != null) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        }
    }

    private void changeStatusBarMain() {
        Window window = getWindow();
        if (window != null) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.bg_main));
        }
    }

    private void resetMenu() {
        int color = ContextCompat.getColor(this, R.color.color_menu_disable);
        binding.ivIconHome.setVisibility(View.INVISIBLE);
        binding.ivHome.setColorFilter(color);
        binding.ivHome.setAlpha(0.4F);
        binding.tvHome.setTextColor(color);

        binding.ivIconExplore.setVisibility(View.INVISIBLE);
        binding.ivExplore.setColorFilter(color);
        binding.ivExplore.setAlpha(0.4F);
        binding.tvExplore.setTextColor(color);

        binding.ivIconFavorite.setVisibility(View.INVISIBLE);
        binding.ivFavorite.setColorFilter(color);
        binding.ivFavorite.setAlpha(0.4F);
        binding.tvFavorite.setTextColor(color);

        binding.ivIconChat.setVisibility(View.INVISIBLE);
        binding.ivChat.setColorFilter(color);
        binding.ivChat.setAlpha(0.4F);
        binding.tvChat.setTextColor(color);

        binding.ivIconAccount.setVisibility(View.INVISIBLE);
        binding.ivAccount.setColorFilter(color);
        binding.ivAccount.setAlpha(0.4F);
        binding.tvAccount.setTextColor(color);
    }

    private void selectedMenu(int selected) {
        resetMenu();
        int color = ContextCompat.getColor(this, R.color.bg_main);
        switch (selected) {
            case 0:
                binding.ivIconHome.setVisibility(View.VISIBLE);
                binding.ivHome.setColorFilter(color);
                binding.ivHome.setAlpha(1F);
                binding.tvHome.setTextColor(color);
                break;
            case 1:
                binding.ivIconExplore.setVisibility(View.VISIBLE);
                binding.ivExplore.setColorFilter(color);
                binding.ivExplore.setAlpha(1F);
                binding.tvExplore.setTextColor(color);
                break;
            case 2:
                binding.ivIconFavorite.setVisibility(View.VISIBLE);
                binding.ivFavorite.setColorFilter(color);
                binding.ivFavorite.setAlpha(1F);
                binding.tvFavorite.setTextColor(color);
                break;
            case 3:
                binding.ivIconChat.setVisibility(View.VISIBLE);
                binding.ivChat.setColorFilter(color);
                binding.ivChat.setAlpha(1F);
                binding.tvChat.setTextColor(color);
                break;
            case 4:
                binding.ivIconAccount.setVisibility(View.VISIBLE);
                binding.ivAccount.setColorFilter(color);
                binding.ivAccount.setAlpha(1F);
                binding.tvAccount.setTextColor(color);
                break;
        }
    }

    public void handleLogoutMenu() {
        currentSelected = 4;
        getSupportFragmentManager().beginTransaction()
                .hide(currentFragment)
                .show(NeedLoginFragment.getInstance())
                .commit();
        HomeFragment.getInstance().refreshData();
        FavoriteFragment.getInstance().refreshData();
        currentFragment = NeedLoginFragment.getInstance();
        selectedMenu(4);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentFragment instanceof ProfileFragment) {
            ((ProfileFragment) currentFragment).reloadProfile();
        } else if (currentFragment instanceof FavoriteFragment) {
            ((FavoriteFragment) currentFragment).refreshData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}