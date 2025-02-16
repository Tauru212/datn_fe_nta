package edu.re.estate.presenters.main.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import edu.re.estate.R;
import edu.re.estate.components.NConstants;
import edu.re.estate.data.models.Post;
import edu.re.estate.databinding.ItemPostHorizontalBinding;
import edu.re.estate.databinding.ItemPostVerticalBinding;
import edu.re.estate.utils.SessionManager;

public class PostHorizontalAdapter extends RecyclerView.Adapter<PostHorizontalAdapter.ViewHolder> {

    private List<Post> posts;
    private OnPostCallback callback;

    public interface OnPostCallback {
        void onFavorite(Post post);

        void onDetail(Post post);
    }

    private boolean isShowFavorite = true;

    public PostHorizontalAdapter(List<Post> posts, OnPostCallback callback) {
        this.posts = posts;
        this.callback = callback;
    }

    public PostHorizontalAdapter(List<Post> posts, OnPostCallback callback, boolean isShowFavorite) {
        this.posts = posts;
        this.callback = callback;
        this.isShowFavorite = isShowFavorite;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSource(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostHorizontalBinding binding = ItemPostHorizontalBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, callback);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(posts.get(position), isShowFavorite);
    }

    @Override
    public int getItemCount() {
        return posts != null ? posts.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemPostHorizontalBinding binding;
        private final OnPostCallback callback;

        public ViewHolder(ItemPostHorizontalBinding binding, OnPostCallback callback) {
            super(binding.getRoot());
            this.binding = binding;
            this.callback = callback;
        }

        public void bind(Post post, boolean isShowFavorite) {
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                Glide.with(binding.getRoot().getContext())
                        .load(NConstants.BASE_IMAGE_URL + post.getImages().get(0).getImage())
                        .into(binding.img);
            } else {
                binding.img.setImageResource(R.drawable.logo);
            }

            if (post.isLiked()) {
                binding.ivLiked.setImageResource(R.drawable.baseline_favorite_24);
            } else {
                binding.ivLiked.setImageResource(R.drawable.baseline_favorite_border_24);
            }

            binding.getRoot().setOnClickListener(v -> {
                if (callback != null) {
                    callback.onDetail(post);
                }
            });
            if (!isShowFavorite) {
                binding.ivLiked.setVisibility(View.GONE);
            }
            binding.ivLiked.setOnClickListener(v -> {
                if (!isShowFavorite) return;
                if (callback != null) {
                    callback.onFavorite(post);
                }
                post.setLiked(!post.isLiked());
                if (post.isLiked()) {
                    binding.ivLiked.setImageResource(R.drawable.baseline_favorite_24);
                } else {
                    binding.ivLiked.setImageResource(R.drawable.baseline_favorite_border_24);
                }
            });
            if ("rented".equals(post.getState())) {
                binding.tvRented.setVisibility(View.VISIBLE);
                binding.container.setAlpha(0.6F);
            } else {
                binding.tvRented.setVisibility(View.GONE);
                binding.container.setAlpha(1.0F);
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

            StringBuilder price = new StringBuilder();

            double amount = Double.parseDouble(post.getPrice().replace(".", ""));
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedAmount = currencyFormatter.format(amount);

            if (post.getPostType().equals("sell")) {
                price.append(formattedAmount);
            } else {
                price.append(formattedAmount).append("/ tháng");
            }
            binding.tvPrice.setText(price.toString());
        }

        private static @NonNull StringBuilder getStringBuilder(Post post) {
            StringBuilder address = new StringBuilder();
            if (!SessionManager.cities.isEmpty()) {
                for (int i = 0; i < SessionManager.cities.size(); i++) {
                    if (Objects.equals(SessionManager.cities.get(i).getId(), post.getCityId())) {
                        for (int j = 0; j < SessionManager.cities.get(i).getDistricts().size(); j++) {
                            if (Objects.equals(SessionManager.cities.get(i).getDistricts().get(j).getId(), post.getDistrictId())) {
                                address.append(SessionManager.cities.get(i).getDistricts().get(j).getName());
                                address.append(", ");
                                address.append(SessionManager.cities.get(i).getName());
                                break;
                            }
                        }
                    }
                }
            }
            return address;
        }
    }
}
