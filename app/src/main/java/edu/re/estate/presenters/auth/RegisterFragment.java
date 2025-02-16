package edu.re.estate.presenters.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.regex.Pattern;

import edu.re.estate.R;
import edu.re.estate.components.ExCallback;
import edu.re.estate.components.RetrofitBuilder;
import edu.re.estate.data.models.BaseResult;
import edu.re.estate.data.models.Token;
import edu.re.estate.data.models.User;
import edu.re.estate.data.request.RegisterRequest;
import edu.re.estate.data.source.network.AuthService;
import edu.re.estate.data.source.repository.AuthRepositoryImpl;
import edu.re.estate.databinding.FragmentRegisterBinding;
import edu.re.estate.presenters.admin.AdminActivity;
import edu.re.estate.presenters.main.MainActivity;
import edu.re.estate.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static RegisterFragment instance;

    public static RegisterFragment getInstance() {
        if (instance == null) {
            instance = new RegisterFragment();
        }
        return instance;
    }

    public static RegisterFragment getInstance(Bundle bundle) {
        if (instance == null) {
            instance = new RegisterFragment();
            if (bundle != null && !bundle.isEmpty()) {
                instance.setArguments(bundle);
            }
        }
        return instance;
    }

    private FragmentRegisterBinding binding;
    private boolean isShowPassword = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.cardClose.setOnClickListener(v -> {
            if (getActivity() != null && getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).showLoginFragment();
            }
        });
        binding.edtName.setOnFocusChangeListener((view1, hasFocus) -> {
            if (hasFocus) {
                binding.layoutName.setBackgroundResource(R.drawable.bg_edt_focus);
            } else {
                binding.layoutName.setBackgroundResource(R.drawable.bg_edt_default);
            }
        });
        binding.edtEmail.setOnFocusChangeListener((view1, hasFocus) -> {
            if (hasFocus) {
                binding.layoutEmail.setBackgroundResource(R.drawable.bg_edt_focus);
            } else {
                binding.layoutEmail.setBackgroundResource(R.drawable.bg_edt_default);
            }
        });
        binding.edtPassword.setOnFocusChangeListener((view1, hasFocus) -> {
            if (hasFocus) {
                binding.layoutPassword.setBackgroundResource(R.drawable.bg_edt_focus);
            } else {
                binding.layoutPassword.setBackgroundResource(R.drawable.bg_edt_default);
            }
        });

        binding.ivShowHidePassword.setOnClickListener(v -> {
            if (isShowPassword) {
                binding.ivShowHidePassword.setImageResource(R.drawable.icons8_hide_password);
                isShowPassword = false;
                binding.edtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                binding.edtPassword.setSelection(binding.edtPassword.getText().length());
            } else {
                binding.ivShowHidePassword.setImageResource(R.drawable.icons8_show_password);
                isShowPassword = true;
                binding.edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                binding.edtPassword.setSelection(binding.edtPassword.getText().length());
            }
        });
        binding.btnRegister.setOnClickListener(v -> {
            binding.edtName.clearFocus();
            binding.edtEmail.clearFocus();
            binding.edtPassword.clearFocus();

            String name = binding.edtName.getText().toString().trim();
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(requireContext(), "Chưa nhập Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(requireContext(), "Chưa nhập mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(requireContext(), "Chưa nhập tên", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!EMAIL_PATTERN.matcher(email).matches()) {
                Toast.makeText(requireContext(), "Email không đúng định dạng!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (getActivity() != null) {
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.edtName.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(binding.edtEmail.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(binding.edtPassword.getWindowToken(), 0);
            }
            if (getActivity() != null && getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).showLoading();
            }

            RetrofitBuilder.authService.register(new RegisterRequest(name, email, password, "user")).enqueue(new Callback<BaseResult<Token>>() {
                @Override
                public void onResponse(@NonNull Call<BaseResult<Token>> call, @NonNull Response<BaseResult<Token>> response) {
                    if (getActivity() != null && getActivity() instanceof AuthActivity) {
                        ((AuthActivity) getActivity()).hideLoading();
                    }
                    if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                        Token data = response.body().getData();
                        if (data != null && !TextUtils.isEmpty(data.getAccessToken())) {
                            String accessToken = data.getAccessToken();
                            saveAccessToken(accessToken);
                            getUserInfo(accessToken);
                        } else {
                            hideLoading();
                            binding.edtEmail.setError("Email này đã tồn tại");
                        }
                    } else {
                        binding.edtEmail.setError("Email này đã tồn tại");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<BaseResult<Token>> call, @NonNull Throwable throwable) {
                    if (getActivity() != null && getActivity() instanceof AuthActivity) {
                        ((AuthActivity) getActivity()).hideLoading();
                    }
                    Toast.makeText(requireActivity(), "Có lỗi xảy ra. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.layoutLogin.setOnClickListener(v -> {
            if (getActivity() != null && getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).showLoginFragment();
            }
        });
    }

    private void saveAccessToken(String accessToken) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("access_token", accessToken);
        editor.apply();
    }

    private void clearAccessToken() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("access_token");
        editor.apply();
    }

    private void getUserInfo(String accessToken) {
        AuthRepositoryImpl.getInstance().myInfo(accessToken, new ExCallback<User>() {
            @Override
            public void onResponse(User data) {
                hideLoading();
                SessionManager.currentUser = data;
                if (data != null) {
                    ((AuthActivity) requireActivity()).db.collection("user")
                            .add(data);
                    goToMain(data);
                } else {
                    clearAccessToken();
                    binding.edtEmail.setError("Lỗi xảy ra! Vui lòng thử lại");
                }
            }

            @Override
            public void onFailure(Throwable var2) {
                hideLoading();
                binding.edtEmail.setError("Lỗi xảy ra! Error = " + var2.getMessage());
            }
        });
    }

    private void goToMain(User data) {
        Intent intent;
        if ("admin".equals(data.getRole())) {
            intent = new Intent(requireActivity(), AdminActivity.class);
        } else {
            intent = new Intent(requireActivity(), MainActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void hideLoading() {
        if (getActivity() != null && getActivity() instanceof AuthActivity) {
            ((AuthActivity) getActivity()).hideLoading();
        }
    }

    public void requestFocus() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            binding.edtName.requestFocus();
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(binding.edtName, InputMethodManager.SHOW_IMPLICIT);
        }, 300L);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
