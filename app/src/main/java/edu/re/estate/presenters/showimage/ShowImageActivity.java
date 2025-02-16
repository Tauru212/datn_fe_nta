package edu.re.estate.presenters.showimage;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import edu.re.estate.components.NConstants;
import edu.re.estate.databinding.ActivityShowImageBinding;

public class ShowImageActivity extends AppCompatActivity {

    private ActivityShowImageBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String path = getIntent().getStringExtra("path");
        if (TextUtils.isEmpty(path)) {
            finish();
            return;
        }
        Glide.with(this).load(NConstants.BASE_IMAGE_URL + path).into(binding.photoView);
        binding.cardBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
