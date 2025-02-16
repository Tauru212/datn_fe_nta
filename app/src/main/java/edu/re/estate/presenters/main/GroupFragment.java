package edu.re.estate.presenters.main;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.Collections;

import edu.re.estate.data.models.Topic;
import edu.re.estate.databinding.FragmentGroupBinding;
import edu.re.estate.presenters.auth.AuthActivity;
import edu.re.estate.presenters.main.adapter.TopicAdapter;
import edu.re.estate.utils.SessionManager;

public class GroupFragment extends Fragment {
    private static GroupFragment instance;

    public static GroupFragment getInstance() {
        if (instance == null) {
            instance = new GroupFragment();
        }
        return instance;
    }

    private FragmentGroupBinding binding;

    private TopicAdapter topicAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGroupBinding.inflate(inflater, container, false);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvNeedLogin.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AuthActivity.class);
            intent.putExtra("EXTRA_IS_FROM_MAIN", true);
            startActivity(intent);
        });
        topicAdapter = new TopicAdapter(Collections.emptyList());
        binding.recyclerView.setAdapter(topicAdapter);
        loadTopics();
    }

    public void loadTopics() {
        if (SessionManager.currentUser == null) {
            binding.tvNeedLogin.setVisibility(View.VISIBLE);
            return;
        }
        binding.tvNeedLogin.setVisibility(View.GONE);
        int userId = SessionManager.currentUser.getAccountId();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("topics")
                .where(
                        Filter.or(
                                Filter.equalTo("guestId", userId),
                                Filter.equalTo("ownerId", userId)
                        )
                )
                .get()
                .addOnCompleteListener(task -> {
                    ArrayList<Topic> topics = new ArrayList<>();
                    for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                        Topic topic = snapshot.toObject(Topic.class);
                        if (topic != null) {
                            topic.setDocumentId(snapshot.getId());
                            Log.d("3GT45_x", "topic= " + topic.getTitle() + "; snapshot.getId() = " + snapshot.getId());
                            topics.add(topic);
                        }
                    }
                    topicAdapter.setTopics(topics);
                })
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
