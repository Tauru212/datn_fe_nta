package edu.re.estate.presenters.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;

import edu.re.estate.R;
import edu.re.estate.components.ExCallback;
import edu.re.estate.components.NConstants;
import edu.re.estate.data.models.BaseResult;
import edu.re.estate.data.models.User;
import edu.re.estate.data.source.network.AuthService;
import edu.re.estate.data.source.repository.AuthRepositoryImpl;
import edu.re.estate.databinding.FragmentProfileBinding;
import edu.re.estate.presenters.admin.AdminActivity;
import edu.re.estate.presenters.auth.AuthActivity;
import edu.re.estate.presenters.post.CreatePostActivity;
import edu.re.estate.utils.FileUtils;
import edu.re.estate.utils.SessionManager;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileFragment extends Fragment {

    private static ProfileFragment instance;

    public static ProfileFragment getInstance() {
        if (instance == null) {
            instance = new ProfileFragment();
        }
        return instance;
    }

    private FragmentProfileBinding binding;

    private static final int PICK_IMAGE_REQUEST = 1000;
    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(NConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private final AuthService service = retrofit.create(AuthService.class);
    private User currentUser = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("IntentReset")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.menuMoreLogout.setOnClickListener(v -> {
            logout();
        });
        binding.cardViewEdit.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
        binding.cardViewCreatePost.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), CreatePostActivity.class);
            startActivity(intent);
        });

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        if (!TextUtils.isEmpty(accessToken)) {
            AuthRepositoryImpl.getInstance().myInfo(accessToken, new ExCallback<User>() {
                @Override
                public void onResponse(User data) {
                    hideLoading();
                    SessionManager.currentUser = data;
                    if (data != null) {
                        currentUser = SessionManager.currentUser;
                        if (!TextUtils.isEmpty(currentUser.getAvatar())) {
                            Glide.with(requireContext())
                                    .load(NConstants.BASE_IMAGE_URL + currentUser.getAvatar())
                                    .into(binding.ivAvatar);
                        } else {
                            binding.ivAvatar.setImageResource(R.drawable.logo);
                        }
                        if ("admin".equals(currentUser.getRole())) {
                            binding.menuMoreAdmin.setVisibility(View.VISIBLE);
                        } else {
                            binding.menuMoreAdmin.setVisibility(View.GONE);
                        }
                        binding.tvName.setText(currentUser.getName());
                    } else {
                        clearAccessToken();
                        logout();
                    }
                }

                @Override
                public void onFailure(Throwable var2) {
                    hideLoading();
                    logout();
                }
            });
        }

        binding.menuMoreAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AdminActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            requireActivity().finish();
        });
    }

    public void reloadProfile() {
        currentUser = SessionManager.currentUser;
        if (!TextUtils.isEmpty(currentUser.getAvatar())) {
            Glide.with(requireContext())
                    .load(NConstants.BASE_IMAGE_URL + currentUser.getAvatar())
                    .into(binding.ivAvatar);
        } else {
            binding.ivAvatar.setImageResource(R.drawable.logo);
        }
        binding.tvName.setText(currentUser.getName());
    }

    private void logout() {
        SessionManager.currentUser = null;
        clearAccessToken();
        ((MainActivity) requireActivity()).handleLogoutMenu();
    }

    private void clearAccessToken() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("access_token");
        editor.apply();
    }

    private void showLoading() {
        if (getActivity() != null && getActivity() instanceof AuthActivity) {
            ((AuthActivity) getActivity()).showLoading();
        }
    }

    private void hideLoading() {
        if (getActivity() != null && getActivity() instanceof AuthActivity) {
            ((AuthActivity) getActivity()).hideLoading();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            assert data != null;

            try {
                File f = FileUtils.getFile(requireContext(), data.getData());

                RequestBody requestFile = RequestBody.create(MultipartBody.FORM, f);

                MultipartBody.Part filePartImage = MultipartBody.Part.createFormData("file", "image.png", requestFile);

                service.uploadFile(filePartImage).enqueue(new Callback<BaseResult<edu.re.estate.data.models.Path>>() {
                    @Override
                    public void onResponse(@NonNull Call<BaseResult<edu.re.estate.data.models.Path>> call, @NonNull Response<BaseResult<edu.re.estate.data.models.Path>> response) {
                        if (response.body() != null && !TextUtils.isEmpty(response.body().getData().getPath())) {
                            currentUser.setAvatar(response.body().getData().getPath());
                            service.updateUser(currentUser).enqueue(new Callback<BaseResult<User>>() {
                                @Override
                                public void onResponse(Call<BaseResult<User>> call, Response<BaseResult<User>> response) {
                                    Log.d("GT56_x", "updateUser onResponse");
                                    Glide.with(requireContext())
                                            .load(NConstants.BASE_IMAGE_URL + currentUser.getAvatar())
                                            .into(binding.ivAvatar);
                                }

                                @Override
                                public void onFailure(Call<BaseResult<User>> call, Throwable throwable) {
                                    Toast.makeText(requireContext(), "Thay đổi ảnh đại diện không thành công!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BaseResult<edu.re.estate.data.models.Path>> call, @NonNull Throwable throwable) {
                        Toast.makeText(requireContext(), "Upload ảnh thất bại: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
