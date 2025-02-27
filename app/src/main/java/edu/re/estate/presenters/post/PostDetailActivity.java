package edu.re.estate.presenters.post;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import edu.re.estate.R;
import edu.re.estate.components.ExCallback;
import edu.re.estate.components.NConstants;
import edu.re.estate.components.RetrofitBuilder;
import edu.re.estate.data.models.BaseResult;
import edu.re.estate.data.models.ImagePost;
import edu.re.estate.data.models.Post;
import edu.re.estate.data.models.Topic;
import edu.re.estate.data.models.User;
import edu.re.estate.data.request.FavoriteRequest;
import edu.re.estate.data.source.repository.AuthRepositoryImpl;
import edu.re.estate.data.source.repository.PostRepositoryImpl;
import edu.re.estate.databinding.ActivityPostDetailBinding;
import edu.re.estate.presenters.auth.AuthActivity;
import edu.re.estate.presenters.chat.ChatActivity;
import edu.re.estate.presenters.post.adapter.PostImageAdapter;
import edu.re.estate.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

public class PostDetailActivity extends AppCompatActivity {

    private ActivityPostDetailBinding binding;
    private Post post;
    private boolean isAdmin;
    private boolean isSelf;
    private boolean isHighLightMark;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (SessionManager.currentUser != null && SessionManager.currentUser.getRole().equals("admin")) {
                return;
            }
            Log.d("Tracking_Event", "tickView");
            PostRepositoryImpl.getInstance().tickView(post.getPostId());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.cardBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        if (getIntent() != null) {
            post = (Post) getIntent().getSerializableExtra("post");
            isAdmin = getIntent().getBooleanExtra("isAdmin", false);
            isSelf = getIntent().getBooleanExtra("is_self", false);
            isHighLightMark = getIntent().getBooleanExtra("isHighLightMark", false);
        }

        if (isAdmin) {
            binding.cardFavorite.setVisibility(View.GONE);
            binding.cardAction.setVisibility(View.GONE);

            if (isHighLightMark) {
                binding.cardAdminApproved.setVisibility(View.GONE);
                binding.cardAdminRefuse.setVisibility(View.GONE);
                binding.cardAdminHighlightMark.setVisibility(View.GONE);
                binding.cardAdminRemoveHighlightMark.setVisibility(View.VISIBLE);
            } else {
                binding.cardAdminRemoveHighlightMark.setVisibility(View.GONE);
                switch (post.getStatus()) {
                    case "processing":
                        binding.cardAdminApproved.setVisibility(View.VISIBLE);
                        binding.cardAdminRefuse.setVisibility(View.VISIBLE);
                        break;
                    case "approved":
                        binding.cardAdminApproved.setVisibility(View.GONE);
                        binding.cardAdminHighlightMark.setVisibility(View.VISIBLE);
                        binding.cardAdminRefuse.setVisibility(View.VISIBLE);
                        break;
                    case "refuse":
                        binding.cardAdminApproved.setVisibility(View.VISIBLE);
                        binding.cardAdminHighlightMark.setVisibility(View.GONE);
                        binding.cardAdminRefuse.setVisibility(View.GONE);
                        break;
                }
            }
        }

        if (isSelf) {
            if ("approved".equals(post.getStatus())) {
                // xác định nếu tin đang quanrg cáo thì hiển thị : đánh dấu cho thuê + gỡ tin
                // tin đang cho thuê: đánh dấu đã thuê
                if ("rented".equals(post.getState())) {
                    binding.cardSelfAdvertisement.setVisibility(View.VISIBLE);
                    binding.cardSelfRented.setVisibility(View.GONE);
                } else if ("advertisement".equals(post.getState())) {
                    binding.cardSelfAdvertisement.setVisibility(View.GONE);
                    binding.cardSelfRented.setVisibility(View.VISIBLE);
                }
                binding.cardSelfRemovePost.setVisibility(View.VISIBLE);
            } else if ("processing".equals(post.getStatus())) {
                // chỉ hiển thị gỡ tin
                binding.cardSelfRemovePost.setVisibility(View.VISIBLE);
            } else if ("remove_post".equals(post.getStatus())) {
                binding.cardSelfAdvertisement.setVisibility(View.VISIBLE);
            }

            binding.cardSelfAdvertisement.setOnClickListener(v -> {
                SharedPreferences sharedPreferences = getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
                String accessToken = sharedPreferences.getString("access_token", null);

                if ("remove_post".equals(post.getStatus())) {
                    // tin đã xoá -> Mở lại
                    // status: remote_post -> processing
                    PostRepositoryImpl.getInstance().processingPost(accessToken, post.getPostId(), new ExCallback<String>() {
                        @Override
                        public void onResponse(String data) {
                            Toast.makeText(PostDetailActivity.this, "Chuyển trạng thái thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(Throwable var2) {
                            Toast.makeText(PostDetailActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // tin đã cho thuê -> mở lại
                    // state: rented -> advertisement
                    PostRepositoryImpl.getInstance().advertisementPost(accessToken, post.getPostId(), new ExCallback<String>() {
                        @Override
                        public void onResponse(String data) {
                            Toast.makeText(PostDetailActivity.this, "Chuyển trạng thái thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onFailure(Throwable var2) {
                            Toast.makeText(PostDetailActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            binding.cardSelfRemovePost.setOnClickListener(v -> {
                SharedPreferences sharedPreferences = getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
                String accessToken = sharedPreferences.getString("access_token", null);

                PostRepositoryImpl.getInstance().removePost(accessToken, post.getPostId(), new ExCallback<String>() {
                    @Override
                    public void onResponse(String data) {
                        Toast.makeText(PostDetailActivity.this, "Chuyển trạng thái thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable var2) {
                        Toast.makeText(PostDetailActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                    }
                });
            });
            binding.cardSelfRented.setOnClickListener(v-> {
                SharedPreferences sharedPreferences = getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
                String accessToken = sharedPreferences.getString("access_token", null);

                PostRepositoryImpl.getInstance().rentedPost(accessToken, post.getPostId(), new ExCallback<String>() {
                    @Override
                    public void onResponse(String data) {
                        Toast.makeText(PostDetailActivity.this, "Chuyển trạng thái thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable var2) {
                        Toast.makeText(PostDetailActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }

        if (post == null) {
            getOnBackPressedDispatcher().onBackPressed();
            return;
        }

        if (!TextUtils.isEmpty(post.getReStateType())) {
            switch (post.getReStateType()) {
                case "house":
                    binding.tvReStateType.setText("Căn nhà");
                    break;
                case "villa":
                    binding.tvReStateType.setText("Biệt thự");
                    break;
                case "apartment":
                    binding.tvReStateType.setText("Chung cư");
                    break;
                case "bungalow":
                    binding.tvReStateType.setText("Homestay");
                    break;
            }
        }

        binding.tvTitle.setText(post.getTitle());

        StringBuilder address = getStringBuilder(post);
        binding.tvAddress.setText(address.toString());

        binding.cardAdminApproved.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
            String accessToken = sharedPreferences.getString("access_token", null);

            PostRepositoryImpl.getInstance().approvedPost(accessToken, post.getPostId(), new ExCallback<String>() {
                @Override
                public void onResponse(String data) {
                    Toast.makeText(PostDetailActivity.this, "Duyệt đơn thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Throwable var2) {
                    Toast.makeText(PostDetailActivity.this, "Duyệt đơn thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.cardAdminRefuse.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
            String accessToken = sharedPreferences.getString("access_token", null);

            PostRepositoryImpl.getInstance().refusePost(accessToken, post.getPostId(), new ExCallback<String>() {
                @Override
                public void onResponse(String data) {
                    Toast.makeText(PostDetailActivity.this, "Đã từ chối đơn!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Throwable var2) {
                    Toast.makeText(PostDetailActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.cardAdminHighlightMark.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
            String accessToken = sharedPreferences.getString("access_token", null);

            PostRepositoryImpl.getInstance().highLightMarkPost(accessToken, post.getPostId(), new ExCallback<String>() {
                @Override
                public void onResponse(String data) {
                    Toast.makeText(PostDetailActivity.this, "Bài đăng sẽ xuất hiện ở mục Tin nổi bật!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Throwable var2) {
                    Toast.makeText(PostDetailActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.cardAdminRemoveHighlightMark.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
            String accessToken = sharedPreferences.getString("access_token", null);

            PostRepositoryImpl.getInstance().removeHighLightMarkPost(accessToken, post.getPostId(), new ExCallback<String>() {
                @Override
                public void onResponse(String data) {
                    Toast.makeText(PostDetailActivity.this, "Đã xoá bài đăng ở mục Tin nổi bật!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Throwable var2) {
                    Toast.makeText(PostDetailActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                }
            });
        });
        binding.tvAction.setOnClickListener(v -> {
            // 1: Chưa login -> mở màn hình đăng nhập
            if (SessionManager.currentUser == null) {
                Intent intent = new Intent(this, AuthActivity.class);
                intent.putExtra("EXTRA_IS_FROM_MAIN", true);
                startActivity(intent);
            };
            // 2: Nếu user đó mở bài đăng của chính mình -> Nhấn liên hệ không có gì xảy ra.
            if (post.getAccountId() == SessionManager.currentUser.getAccountId()) return;
            // 3: Mở chat
            // Kiểm tra xem đã tồn tại topic chat với thông tin uuid = [user_id đã login trên máy]_[post_id]_[account_id đăng bài]
            try {
                binding.layoutLoading.setVisibility(View.VISIBLE);
                String uuid = SessionManager.currentUser.getAccountId() + "_" + post.getPostId() + "_" + post.getAccountId();
                db.collection("topics")
                        .document(uuid)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                                //Trường hợp này đã tồn tại topic
                                binding.layoutLoading.setVisibility(View.GONE);
                                Topic topic = task.getResult().toObject(Topic.class);
                                if (topic != null) {
                                    topic.setDocumentId(uuid);
                                    goToChat(topic);
                                }
                            } else {
                                // Trường hợp này là chưa tồn tại topic với uuid trên
                                // 1: Lấy thông tin của người đăng tin
                                AuthRepositoryImpl.getInstance().getInfoUser(post.getAccountId(), new ExCallback<User>() {
                                    @Override
                                    public void onResponse(User data) {
                                        if (data != null) {
                                            //Lấy thông tin thành cong
                                            //Tạo 1 topic chat mới
                                            HashMap<String, Object> maps = new HashMap<>();

                                            String topicId = UUID.randomUUID().toString().trim();
                                            long timeLastMessage = System.currentTimeMillis();

                                            maps.put("topicId", topicId);
                                            maps.put("title", post.getTitle());
                                            maps.put("ownerId", SessionManager.currentUser.getAccountId());
                                            maps.put("guestId", post.getAccountId());
                                            maps.put("lastMessage", "");
                                            maps.put("timeLastMessage", timeLastMessage);
                                            maps.put("image", post.getImages().get(0).getImage());
                                            maps.put("name", data.getName());
                                            maps.put("avatar", data.getAvatar());

                                            Topic topic = new Topic(
                                                    post.getAccountId(),
                                                    data.getAvatar(),
                                                    "",
                                                    SessionManager.currentUser.getAccountId(),
                                                    post.getTitle(),
                                                    timeLastMessage,
                                                    data.getName(),
                                                    data.getAvatar(),
                                                    topicId
                                            );
                                            topic.setDocumentId(uuid);

                                            db.collection("topics")
                                                    .document(uuid)
                                                    .set(maps)
                                                    .addOnCompleteListener(task1 -> {
                                                        //Tạo thành công mở màn hình chat
                                                        binding.layoutLoading.setVisibility(View.GONE);
                                                        goToChat(topic);
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        //Tạo không thành công
                                                        binding.layoutLoading.setVisibility(View.GONE);
                                                        Toast.makeText(PostDetailActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                                                    });
                                        } else {
                                            binding.layoutLoading.setVisibility(View.GONE);
                                            Toast.makeText(PostDetailActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable var2) {
                                        binding.layoutLoading.setVisibility(View.GONE);
                                        Toast.makeText(PostDetailActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(e -> {
                            binding.layoutLoading.setVisibility(View.GONE);
                            Toast.makeText(this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
                        });
            } catch (Exception exception) {
                binding.layoutLoading.setVisibility(View.GONE);
                Toast.makeText(this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();
            }
        });
        if (TextUtils.isEmpty(post.getInterior())) {
            binding.tvInterior.setText("K/c TT");
        } else {
            switch (post.getReStateType()) {
                case "full":
                    binding.tvInterior.setText("Đầy đủ");
                    break;
                case "basic":
                    binding.tvInterior.setText("Cơ bản");
                    break;
                case "nothing":
                    binding.tvInterior.setText("K/c");
                    break;
            }
        }

        if (post.getnBedrooms() == 0) {
            binding.tvBedrooms.setText("K/c");
        } else {
            binding.tvBedrooms.setText(post.getnBedrooms() + " ngủ");
        }
        if (post.getnBathrooms() == 0) {
            binding.tvBathrooms.setText("K/c");
        } else {
            binding.tvBathrooms.setText(post.getnBathrooms() + " nhà tắm");
        }
        binding.tvDescription.setText(post.getDescription());
        if (TextUtils.isEmpty(post.getDirection())) {
            binding.tvDirection.setText("Không xác định");
        } else {
            binding.tvDirection.setText(post.getDirection());
        }
        binding.tvAcreage.setText(post.getAcreage() + " m2");

        binding.tvContactName.setText(post.getContactsName());
        binding.tvContactEmail.setText(post.getContactsEmail());
        binding.tvContactPhone.setText(post.getContactsPhone());

        if (post.isLiked()) {
            binding.ivFavorite.setImageResource(R.drawable.baseline_favorite_24);
        } else {
            binding.ivFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
        }

        binding.ivFavorite.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
            String accessToken = sharedPreferences.getString("access_token", null);
            if (TextUtils.isEmpty(accessToken)) {
                AlertDialog alertDialog = new AlertDialog.Builder(this).setCancelable(false).setTitle("Thông báo").setMessage("Cần đăng nhập để thực hiện chức năng này!").setPositiveButton("Đăng nhập", (dialog, which) -> {
                    startActivity(new Intent(this, AuthActivity.class));
                }).setNegativeButton("Thoát", (dialog, which) -> {
                    dialog.dismiss();
                }).create();
                alertDialog.show();
            } else {
                if (post.isLiked()) {
                    RetrofitBuilder.postService.unLikePost(accessToken, new FavoriteRequest(post.getPostId())).enqueue(new Callback<BaseResult<String>>() {
                        @Override
                        public void onResponse(Call<BaseResult<String>> call, Response<BaseResult<String>> response) {
                            binding.ivFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
                        }

                        @Override
                        public void onFailure(Call<BaseResult<String>> call, Throwable throwable) {
                        }
                    });
                } else {
                    RetrofitBuilder.postService.likePost(accessToken, new FavoriteRequest(post.getPostId())).enqueue(new Callback<BaseResult<String>>() {
                        @Override
                        public void onResponse(Call<BaseResult<String>> call, Response<BaseResult<String>> response) {
                            binding.ivFavorite.setImageResource(R.drawable.baseline_favorite_24);
                        }

                        @Override
                        public void onFailure(Call<BaseResult<String>> call, Throwable throwable) {
                        }
                    });
                }
            }
        });

        double amount = Double.parseDouble(post.getPrice().replace(".", ""));
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedAmount = currencyFormatter.format(amount);

        StringBuilder price = new StringBuilder();
        if (post.getPostType().equals("sell")) {
            price.append(formattedAmount);
            // binding.tvAction.setText("Chat ngay");
        } else {
            price.append(formattedAmount).append("/ tháng");
            // binding.tvAction.setText("Thuê");
        }
        binding.tvPrice.setText(price.toString());

        List<ImagePost> list = new ArrayList<>();
        if (post == null || post.getImages() == null || post.getImages().isEmpty()) {
            list.add(new ImagePost("000.png"));
        } else {
            list.addAll(post.getImages());
        }

        PostImageAdapter postImageAdapter = new PostImageAdapter(this, list);
        binding.viewPager.setAdapter(postImageAdapter);
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
        }).attach();

        handler.postDelayed(runnable, 3000L);
    }

    private @NonNull StringBuilder getStringBuilder(Post post) {
        StringBuilder address = new StringBuilder();
        if (!SessionManager.cities.isEmpty()) {
            for (int i = 0; i < SessionManager.cities.size(); i++) {
                if (Objects.equals(SessionManager.cities.get(i).getId(), post.getCityId())) {
                    for (int j = 0; j < SessionManager.cities.get(i).getDistricts().size(); j++) {
                        if (Objects.equals(SessionManager.cities.get(i).getDistricts().get(j).getId(), post.getDistrictId())) {
                            for (int k = 0; k < SessionManager.cities.get(i).getDistricts().get(j).getWards().size(); k++) {
                                if (Objects.equals(SessionManager.cities.get(i).getDistricts().get(j).getWards().get(k).getId(), post.getCommuneId())) {
                                    address.append(SessionManager.cities.get(i).getDistricts().get(j).getWards().get(k).getName());
                                    address.append(", ");
                                    address.append(SessionManager.cities.get(i).getDistricts().get(j).getName());
                                    address.append(", ");
                                    address.append(SessionManager.cities.get(i).getName());
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return address;
    }

    private void goToChat(Topic topic) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("topic", topic);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        handler.removeCallbacks(runnable);
        handler = null;
    }
}
