package edu.re.estate.presenters.profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;

import edu.re.estate.R;
import edu.re.estate.components.NConstants;
import edu.re.estate.components.RetrofitBuilder;
import edu.re.estate.data.models.BaseResult;
import edu.re.estate.data.models.Path;
import edu.re.estate.data.models.User;
import edu.re.estate.databinding.ActivityProfileBinding;
import edu.re.estate.utils.FileUtils;
import edu.re.estate.utils.SessionManager;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1000;
    private ActivityProfileBinding binding;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.cardBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
        binding.cardViewEdit.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        if (SessionManager.currentUser == null) {
            finish();
            return;
        }
        user = SessionManager.currentUser;
        if (!TextUtils.isEmpty(user.getAvatar())) {
            Glide.with(this)
                    .load(NConstants.BASE_IMAGE_URL + user.getAvatar())
                    .into(binding.ivAvatar);
        } else {
            binding.ivAvatar.setImageResource(R.drawable.logo);
        }
        binding.edtName.setText(user.getName());
        binding.edtPhone.setText(user.getPhone());

        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(
                this,
                R.array.gender,
                android.R.layout.simple_spinner_dropdown_item
        );
        binding.spinnerGender.setAdapter(adapterGender);

        if (!TextUtils.isEmpty(user.getGender())) {
            if (user.getGender().equals("male")) {
                binding.spinnerGender.setSelection(0);
            } else if (user.getGender().equals("female")) {
                binding.spinnerGender.setSelection(1);
            } else {
                binding.spinnerGender.setSelection(2);
            }
        } else {
            binding.spinnerGender.setSelection(2);
        }

        binding.cardViewUpdate.setOnClickListener(v -> {
            if (user == null) {
                return;
            }
            binding.layoutLoading.setVisibility(View.VISIBLE);
            user.setName(binding.edtName.getText().toString());
            user.setPhone(binding.edtPhone.getText().toString());

            RetrofitBuilder.authService.updateUser(user).enqueue(new Callback<BaseResult<User>>() {
                @Override
                public void onResponse(@NonNull Call<BaseResult<User>> call, @NonNull Response<BaseResult<User>> response) {
                    Log.d("GT56_x", "updateUser onResponse");
                    binding.layoutLoading.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    SessionManager.currentUser = user;
                }

                @Override
                public void onFailure(@NonNull Call<BaseResult<User>> call, @NonNull Throwable throwable) {
                    binding.layoutLoading.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, "Cập nhật không thành công!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    user.setGender("male");
                } else if (i == 1) {
                    user.setGender("female");
                } else {
                    user.setGender("not_disclosed");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            assert data != null;
            try {
                File f = FileUtils.getFile(this, data.getData());

                RequestBody requestFile = RequestBody.create(MultipartBody.FORM, f);

                MultipartBody.Part filePartImage = MultipartBody.Part.createFormData("file", "image.png", requestFile);

                RetrofitBuilder.authService.uploadFile(filePartImage).enqueue(new Callback<BaseResult<Path>>() {
                    @Override
                    public void onResponse(@NonNull Call<BaseResult<Path>> call, @NonNull Response<BaseResult<Path>> response) {
                        if (response.body() != null && !TextUtils.isEmpty(response.body().getData().getPath())) {
                            user.setAvatar(response.body().getData().getPath());
                            Glide.with(ProfileActivity.this)
                                    .load(NConstants.BASE_IMAGE_URL + user.getAvatar())
                                    .into(binding.ivAvatar);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BaseResult<edu.re.estate.data.models.Path>> call, @NonNull Throwable throwable) {
                        // Toast.makeText(this, "Upload ảnh thất bại: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
