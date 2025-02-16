package edu.re.estate.presenters.auth;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import edu.re.estate.R;
import edu.re.estate.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    private Fragment currentFragment;
    public boolean isFromMain;

    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        isFromMain = getIntent().getBooleanExtra("EXTRA_IS_FROM_MAIN", false);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportFragmentManager().beginTransaction()
                .add(binding.container.getId(), LoginFragment.getInstance())
                .hide(LoginFragment.getInstance())
                .add(binding.container.getId(), RegisterFragment.getInstance())
                .hide(RegisterFragment.getInstance())
                .commit();

        currentFragment = LoginFragment.getInstance();
        showLoginFragment();

        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (binding.layoutLoading.getVisibility() != View.VISIBLE) {
                    finish();
                }
            }
        });
    }

    public void showRegisterFragment() {
        getSupportFragmentManager().beginTransaction()
                .hide(currentFragment)
                .show(RegisterFragment.getInstance())
                .commit();
        currentFragment = RegisterFragment.getInstance();
        RegisterFragment.getInstance().requestFocus();
    }

    public void showLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .hide(currentFragment)
                .show(LoginFragment.getInstance())
                .commit();
        currentFragment = LoginFragment.getInstance();
        LoginFragment.getInstance().requestFocus();
    }

    public void showLoading() {
        binding.layoutLoading.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        binding.layoutLoading.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
