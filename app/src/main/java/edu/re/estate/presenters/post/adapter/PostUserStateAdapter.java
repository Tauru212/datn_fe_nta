package edu.re.estate.presenters.post.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import edu.re.estate.presenters.post.PostUserFragment;

public class PostUserStateAdapter extends FragmentStateAdapter {

    public PostUserStateAdapter(@NonNull FragmentActivity fragment) {
        super(fragment);
    }

    private final ArrayList<PostUserFragment> postFragments = new ArrayList<>();

    public void reloadData(int position) {
        if (position < postFragments.size()) {
            postFragments.get(position).reloadData();
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        PostUserFragment fragment = new PostUserFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        postFragments.add(fragment);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
