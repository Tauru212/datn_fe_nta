package edu.re.estate.presenters.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.Collections;
import java.util.List;

import edu.re.estate.components.ExCallback;
import edu.re.estate.data.models.Post;
import edu.re.estate.data.source.repository.PostRepositoryImpl;
import edu.re.estate.databinding.FragmentPostBinding;
import edu.re.estate.presenters.main.adapter.PostVerticalAdapter;
import edu.re.estate.presenters.post.PostDetailActivity;

public class PostFragment extends Fragment {

    private FragmentPostBinding binding;
    private PostVerticalAdapter adapter;
    private String status;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new PostVerticalAdapter(Collections.emptyList(), new PostVerticalAdapter.OnPostCallback() {
            @Override
            public void onFavorite(Post post) {
                // nothing
            }

            @Override
            public void onDetail(Post post) {
                Intent intent = new Intent(requireActivity(), PostDetailActivity.class);
                intent.putExtra("post", post);
                intent.putExtra("isAdmin", true);
                startActivity(intent);
            }
        }, false);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setAdapter(adapter);

        if (getArguments() != null) {
            int position = getArguments().getInt("position");
            if (position == 0) {
                status = "processing";
            } else if (position == 1) {
                status = "approved";
            } else {
                status = "refuse";
            }
            reloadData();
        }
    }

    public void reloadData() {
        PostRepositoryImpl.getInstance().getPostsByStatus(status, new ExCallback<List<Post>>() {
            @Override
            public void onResponse(List<Post> data) {
                if (data != null) {
                    Log.d("PostFragment", "onResponse: " + data.size());
                    adapter.setSource(data);
                } else {
                    Log.d("PostFragment", "onResponse is null");
                }
            }

            @Override
            public void onFailure(Throwable var2) {
                Log.d("PostFragment", "onFailure is call");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
