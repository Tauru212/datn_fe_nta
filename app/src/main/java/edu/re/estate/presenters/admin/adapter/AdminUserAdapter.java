package edu.re.estate.presenters.admin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.re.estate.R;
import edu.re.estate.components.NConstants;
import edu.re.estate.data.models.User;
import edu.re.estate.databinding.ItemUserBinding;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.ViewHolder> {

    private List<User> users;
    private final OnAdminUserCallback callback;

    public AdminUserAdapter(List<User> users, OnAdminUserCallback callback) {
        this.users = users;
        this.callback = callback;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setSource(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding, callback);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < users.size()) {
            holder.bind(users.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return users != null ? users.size() : 0;
    }

    public interface OnAdminUserCallback {
        void onClickUser(User user);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final Context context;
        private final ItemUserBinding binding;
        private final OnAdminUserCallback callback;

        public ViewHolder(ItemUserBinding binding, OnAdminUserCallback callback) {
            super(binding.getRoot());
            this.context = binding.getRoot().getContext();
            this.binding = binding;
            this.callback = callback;
        }

        public void bind(User user) {
            if (!TextUtils.isEmpty(user.getAvatar())) {
                Glide.with(context)
                        .load(NConstants.BASE_IMAGE_URL + user.getAvatar())
                        .into(binding.img);
            } else {
                binding.img.setImageResource(R.drawable.logo);
            }

            binding.tvUsername.setText(user.getName());
            binding.tvAccountId.setText("Account ID: " + user.getAccountId());

            binding.getRoot().setOnClickListener(v -> {
                if (callback != null) {
                    callback.onClickUser(user);
                }
            });
        }
    }
}
