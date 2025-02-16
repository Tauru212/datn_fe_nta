package edu.re.estate.presenters.chat;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.re.estate.components.NConstants;
import edu.re.estate.data.models.ChatMessage;
import edu.re.estate.databinding.ItemOtherMessageBinding;
import edu.re.estate.databinding.ItemSelfMessageBinding;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatMessage> chatMessages;
    private final int selfId;
    private final OnClickItemChatListener listener;

    public interface OnClickItemChatListener {
        void onClickItemImage(String path);
    }

    public ChatAdapter(int selfId, List<ChatMessage> chatMessages, OnClickItemChatListener listener) {
        this.chatMessages = chatMessages;
        this.selfId = selfId;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new SelfChatViewHolder(ItemSelfMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
        }
        return new OtherChatViewHolder(ItemOtherMessageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SelfChatViewHolder) {
            ((SelfChatViewHolder) holder).bind(chatMessages.get(position));
        } else {
            ((OtherChatViewHolder) holder).bind(chatMessages.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessages.get(position).getSenderId() == selfId) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return chatMessages != null ? chatMessages.size() : 0;
    }

    static class SelfChatViewHolder extends RecyclerView.ViewHolder {

        private final ItemSelfMessageBinding binding;
        private final OnClickItemChatListener listener;

        public SelfChatViewHolder(ItemSelfMessageBinding binding, OnClickItemChatListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
        }

        public void bind(ChatMessage chatMessage) {
            binding.tvTimeMessage.setText(getReadableDateTime(new Date(chatMessage.getTimeSendMessage())));

            if ("image".equals(chatMessage.getType()) && !TextUtils.isEmpty(chatMessage.getContent())) {
                binding.cardViewImage.setVisibility(View.VISIBLE);
                binding.tvMessage.setVisibility(View.GONE);

                Glide.with(binding.getRoot().getContext())
                        .load(NConstants.BASE_IMAGE_URL + chatMessage.getContent())
                        .into(binding.ivImage);
            } else {
                binding.cardViewImage.setVisibility(View.GONE);
                binding.tvMessage.setVisibility(View.VISIBLE);

                binding.tvMessage.setText(chatMessage.getContent());
            }

            binding.getRoot().setOnClickListener(v -> {
                if ("image".equals(chatMessage.getType()) && !TextUtils.isEmpty(chatMessage.getContent())) {
                    if (listener != null) {
                        listener.onClickItemImage(chatMessage.getContent());
                    }
                }
            });
        }

        private String getReadableDateTime(Date date) {
            return new SimpleDateFormat("hh:mm dd-MM-yyyy", Locale.getDefault()).format(date);
        }
    }

    static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private final ItemOtherMessageBinding binding;
        private final OnClickItemChatListener listener;

        public OtherChatViewHolder(ItemOtherMessageBinding binding, OnClickItemChatListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
        }

        public void bind(ChatMessage chatMessage) {
            binding.tvTimeMessage.setText(getReadableDateTime(new Date(chatMessage.getTimeSendMessage())));

            if ("image".equals(chatMessage.getType()) && !TextUtils.isEmpty(chatMessage.getContent())) {
                binding.cardViewImage.setVisibility(View.VISIBLE);
                binding.tvMessage.setVisibility(View.GONE);

                Glide.with(binding.getRoot().getContext())
                        .load(NConstants.BASE_IMAGE_URL + chatMessage.getContent())
                        .into(binding.ivImage);
            } else {
                binding.cardViewImage.setVisibility(View.GONE);
                binding.tvMessage.setVisibility(View.VISIBLE);

                binding.tvMessage.setText(chatMessage.getContent());
            }

            binding.getRoot().setOnClickListener(v -> {
                if ("image".equals(chatMessage.getType()) && !TextUtils.isEmpty(chatMessage.getContent())) {
                    if (listener != null) {
                        listener.onClickItemImage(chatMessage.getContent());
                    }
                }
            });
        }

        private String getReadableDateTime(Date date) {
            return new SimpleDateFormat("hh:mm dd-MM-yyyy", Locale.getDefault()).format(date);
        }
    }
}
