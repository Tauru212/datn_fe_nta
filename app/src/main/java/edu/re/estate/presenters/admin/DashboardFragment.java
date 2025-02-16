package edu.re.estate.presenters.admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.re.estate.components.ExCallback;
import edu.re.estate.data.models.Post;
import edu.re.estate.data.models.TotalPost;
import edu.re.estate.data.models.User;
import edu.re.estate.data.source.repository.AuthRepositoryImpl;
import edu.re.estate.data.source.repository.PostRepositoryImpl;
import edu.re.estate.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private String accessToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString("access_token", null);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadTotalUser();
    }

    public void reload() {
        loadTotalUser();
        loadTotalPost();
        loadTotalPostHighlightMark();
        statisticalPostViews();
        statisticalPostHighlightMarkViews();
    }

    private void loadTotalUser() {
        AuthRepositoryImpl.getInstance().getUsers(accessToken, new ExCallback<List<User>>() {
            @Override
            public void onResponse(List<User> data) {
                ((AdminActivity) requireActivity()).hideLoading();
                if (data != null) {
                    binding.tvTotalUser.setText(String.valueOf(data.size() + 1));

                    int newRegisterInMonth = 0;
                    for (User user : data) {
                        int year = Integer.parseInt(user.getCreateAt().substring(0, 4));
                        int month = Integer.parseInt(user.getCreateAt().substring(5, 7));

                        if (year == Calendar.getInstance().get(Calendar.YEAR) && month == Calendar.getInstance().get(Calendar.MONTH) + 1) {
                            newRegisterInMonth++;
                        }
                    }
                    binding.tvTotalNewUserInMonth.setText(String.valueOf(newRegisterInMonth));
                } else {
                    binding.tvTotalUser.setText("Đang xử lý");
                    binding.tvTotalNewUserInMonth.setText("Đang xử lý");
                }
            }

            @Override
            public void onFailure(Throwable var2) {
                binding.tvTotalUser.setText("Đang xử lý");
                binding.tvTotalNewUserInMonth.setText("Đang xử lý");
            }
        });
    }

    private void loadTotalPost() {
        PostRepositoryImpl.getInstance().statisticalPost(new ExCallback<TotalPost>() {
            @Override
            public void onResponse(TotalPost data) {
                if (data != null) {
                    binding.tvTotalPost.setText(String.valueOf(data.getAll()));
                    binding.tvTotalApproved.setText(String.valueOf(data.getApproved()));
                    binding.tvTotalProcessing.setText(String.valueOf(data.getProcessing()));
                    binding.tvTotalRefused.setText(String.valueOf(data.getRefused()));
                } else {
                    binding.tvTotalPost.setText("Đang xử lý");
                    binding.tvTotalApproved.setText("Đang xử lý");
                    binding.tvTotalProcessing.setText("Đang xử lý");
                    binding.tvTotalRefused.setText("Đang xử lý");
                }
            }

            @Override
            public void onFailure(Throwable var2) {
                binding.tvTotalPost.setText("Đang xử lý");
                binding.tvTotalApproved.setText("Đang xử lý");
                binding.tvTotalProcessing.setText("Đang xử lý");
                binding.tvTotalRefused.setText("Đang xử lý");
            }
        });
    }

    private void loadTotalPostHighlightMark() {
        PostRepositoryImpl.getInstance().statisticalPostHighlightMark(new ExCallback<Integer>() {
            @Override
            public void onResponse(Integer data) {
                if (data != null) {
                    binding.tvTotalHighlightMark.setText(String.valueOf(data));

                    double amount = data * 10000;
                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    String formattedAmount = currencyFormatter.format(amount);
                    binding.tvTotalPrice.setText(formattedAmount);
                } else {
                    binding.tvTotalHighlightMark.setText("Đang xử lý");
                    binding.tvTotalPrice.setText("Đang xử lý");
                }
            }

            @Override
            public void onFailure(Throwable var2) {
                binding.tvTotalHighlightMark.setText("Đang xử lý");
                binding.tvTotalPrice.setText("Đang xử lý");
            }
        });
    }

    private void statisticalPostViews() {
        PostRepositoryImpl.getInstance().statisticalPostViews(new ExCallback<Integer>() {
            @Override
            public void onResponse(Integer data) {
                if (data != null) {
                    binding.tvPostViews.setText(String.valueOf(data));
                } else {
                    binding.tvPostViews.setText("Đang xử lý");
                }
            }

            @Override
            public void onFailure(Throwable var2) {
                binding.tvPostViews.setText("Đang xử lý");
            }
        });
    }

    private void statisticalPostHighlightMarkViews() {
//        PostRepositoryImpl.getInstance().statisticalPostHighlightMarkViews(new ExCallback<Integer>() {
//            @Override
//            public void onResponse(Integer data) {
//                if (data != null) {
//                    binding.tvPostHighlightMarkViews.setText(String.valueOf(data));
//
//                    double amount = data * 10000;
//                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
//                    String formattedAmount = currencyFormatter.format(amount);
//                    binding.tvTotalPrice.setText(formattedAmount);
//                } else {
//                    binding.tvPostHighlightMarkViews.setText("Đang xử lý");
//                    binding.tvTotalPrice.setText("Đang xử lý");
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable var2) {
//                binding.tvPostHighlightMarkViews.setText("Đang xử lý");
//                binding.tvTotalPrice.setText("Đang xử lý");
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
