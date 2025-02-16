package edu.re.estate.presenters.admin.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

import edu.re.estate.presenters.admin.PostFragment;

public class PostStateAdapter extends FragmentStateAdapter {

    public PostStateAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    private final ArrayList<PostFragment> postFragments = new ArrayList<>();

    public void reloadData(int position) {
        if (position < postFragments.size()) {
            postFragments.get(position).reloadData();
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        PostFragment fragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        postFragments.add(fragment);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
