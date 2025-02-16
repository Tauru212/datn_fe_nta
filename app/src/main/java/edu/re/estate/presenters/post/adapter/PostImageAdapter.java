package edu.re.estate.presenters.post.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import edu.re.estate.data.models.ImagePost;
import edu.re.estate.presenters.admin.PostFragment;
import edu.re.estate.presenters.post.PostImageFragment;

public class PostImageAdapter extends FragmentStateAdapter {

    private final List<ImagePost> imagePosts;

    public PostImageAdapter(@NonNull FragmentActivity fragmentActivity, List<ImagePost> imagePosts) {
        super(fragmentActivity);
        this.imagePosts = imagePosts;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        PostImageFragment fragment = new PostImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", imagePosts.get(position).getImage());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return imagePosts.size();
    }
}
