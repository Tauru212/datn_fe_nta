package edu.re.estate.presenters.post;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

import edu.re.estate.databinding.ItemAlbumSelectedBinding;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private List<Uri> files;
    private OnClearListener listener;

    public AlbumAdapter(List<Uri> files, OnClearListener listener) {
        this.files = files;
        this.listener = listener;
    }

    interface OnClearListener {
        void onClear(Uri uri);
    }

    public void setFiles(List<Uri> files) {
        this.files = files;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAlbumSelectedBinding binding = ItemAlbumSelectedBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AlbumViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.bind(files.get(position));
    }

    @Override
    public int getItemCount() {
        return files != null ? files.size() : 0;
    }

    public static class AlbumViewHolder extends RecyclerView.ViewHolder {

        private final ItemAlbumSelectedBinding binding;
        private final OnClearListener listener;

        public AlbumViewHolder(ItemAlbumSelectedBinding binding, OnClearListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;
        }

        public void bind(Uri uri) {
            binding.iv.setImageURI(uri);
            binding.ivClear.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onClear(uri);
                }
            });
        }
    }
}
