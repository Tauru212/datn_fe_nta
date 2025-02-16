package edu.re.estate.presenters.main.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import edu.re.estate.components.NConstants;
import edu.re.estate.data.models.Topic;
import edu.re.estate.databinding.ItemTopicChatBinding;
import edu.re.estate.presenters.chat.ChatActivity;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {

    private List<Topic> topics;

    public TopicAdapter(List<Topic> topics) {
        this.topics = topics;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTopics(List<Topic> topics) {
        this.topics = topics;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTopicChatBinding binding = ItemTopicChatBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TopicViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return topics != null ? topics.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        if (position < topics.size()) {
            holder.bind(topics.get(position));
        }
    }

    public static class TopicViewHolder extends RecyclerView.ViewHolder {

        private final ItemTopicChatBinding binding;

        public TopicViewHolder(ItemTopicChatBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Topic topic) {
            binding.tvTitle.setText(topic.getTitle());
            binding.tvLastMessage.setText(topic.getLastMessage());

            Glide.with(binding.getRoot().getContext())
                    .load(NConstants.BASE_IMAGE_URL + topic.getImage())
                    .into(binding.ivImage);
            binding.tvOwnerName.setText(topic.getName());
            Glide.with(binding.getRoot().getContext())
                    .load(NConstants.BASE_IMAGE_URL + topic.getAvatar())
                    .into(binding.ivAvatar);
            Date date = new Date(topic.getTimeLastMessage());
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            binding.tvLastTime.setText(sdf.format(date));

            binding.getRoot().setOnClickListener(v -> {
                Intent intent = new Intent(binding.getRoot().getContext(), ChatActivity.class);
                intent.putExtra("topic", topic);
                binding.getRoot().getContext().startActivity(intent);
            });
        }
    }
}
