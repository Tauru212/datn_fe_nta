package edu.re.estate.presenters.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import edu.re.estate.R;
import edu.re.estate.components.ExCallback;
import edu.re.estate.components.NConstants;
import edu.re.estate.data.models.BaseResult;
import edu.re.estate.data.models.ChatMessage;
import edu.re.estate.data.models.Path;
import edu.re.estate.data.models.Topic;
import edu.re.estate.data.request.PutImageRequest;
import edu.re.estate.data.source.network.AuthService;
import edu.re.estate.data.source.repository.PostRepositoryImpl;
import edu.re.estate.databinding.ActivityChatBinding;
import edu.re.estate.presenters.post.CreatePostActivity;
import edu.re.estate.presenters.showimage.ShowImageActivity;
import edu.re.estate.utils.FileUtils;
import edu.re.estate.utils.SessionManager;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1000;
    private ActivityChatBinding binding;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private int selfId;
    private Topic topic;

    private final ArrayList<ChatMessage> chatMessages = new ArrayList<>();
    private ChatAdapter adapter;

    private final Retrofit retrofit = new Retrofit.Builder().baseUrl(NConstants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    private final AuthService service = retrofit.create(AuthService.class);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cardBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        Window window = getWindow();
        if (window != null) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.bg_main));
        }
        if (getIntent() != null) {
            topic = (Topic) getIntent().getSerializableExtra("topic");
        }
        if (SessionManager.currentUser == null || topic == null) {
            finish();
            return;
        }
        binding.ivGallery.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
        selfId = SessionManager.currentUser.getAccountId();
        adapter = new ChatAdapter(selfId, Collections.emptyList(), path -> {
            Intent intent = new Intent(this, ShowImageActivity.class);
            intent.putExtra("path", path);
            startActivity(intent);
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        Glide.with(binding.getRoot().getContext())
                .load(NConstants.BASE_IMAGE_URL + topic.getImage())
                .into(binding.ivAvatar);
        binding.tvName.setText(topic.getName());

        loadMessage();

        binding.tvSend.setOnClickListener(v -> {
            String content = binding.edtMessage.getText().toString().trim();
            if (TextUtils.isEmpty(content)) return;
            sendMessage(content);
        });
    }

    private void loadMessage() {
        if (topic != null) {
            db.collection("messages")
                    .whereEqualTo("topicId", topic.getTopicId())
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null || value == null) {
                                return;
                            }
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                                    // Parse new messages added to the chat collection
                                    ChatMessage chatMessage = new ChatMessage();

                                    Long timeSendMessage = documentChange.getDocument().getLong("timeSendMessage");
                                    if (timeSendMessage != null) {
                                        chatMessage.setTimeSendMessage(timeSendMessage);
                                    }
                                    chatMessage.setTopicId(documentChange.getDocument().getString("topicId"));
                                    chatMessage.setContent(documentChange.getDocument().getString("content"));
                                    chatMessage.setType(documentChange.getDocument().getString("type"));
                                    Long senderId = documentChange.getDocument().getLong("senderId");
                                    if (senderId != null) {
                                        chatMessage.setSenderId(senderId.intValue());
                                    }
                                    chatMessages.add(chatMessage);
                                }
                            }
                            // Sort messages by timeSendMessage
                            chatMessages.sort(Comparator.comparingLong(ChatMessage::getTimeSendMessage));

                            adapter.setChatMessages(chatMessages);
                            binding.recyclerView.smoothScrollToPosition(chatMessages.size() - 1);
                        }
                    });
        }
    }

    private void sendMessage(String content) {
        ChatMessage chatMessage = new ChatMessage();

        long timeSendMessage = System.currentTimeMillis();
        chatMessage.setTopicId(topic.getTopicId());
        chatMessage.setSenderId(selfId);
        chatMessage.setContent(content);
        chatMessage.setTimeSendMessage(timeSendMessage);

        db.collection("messages")
                .add(chatMessage)
                .addOnCompleteListener(task -> {
                    binding.edtMessage.setText("");

                    // update group chat
                    HashMap<String, Object> maps = new HashMap<>();
                    maps.put("lastMessage", content);
                    maps.put("timeLastMessage", timeSendMessage);

                    db.collection("topics")
                            .document(topic.getDocumentId())
                            .update(maps);
                })
                .addOnFailureListener(e -> {
                    binding.edtMessage.setText("");
                });
    }

    private void sendImageMessage(String path) {
        ChatMessage chatMessage = new ChatMessage();

        long timeSendMessage = System.currentTimeMillis();
        chatMessage.setTopicId(topic.getTopicId());
        chatMessage.setSenderId(selfId);
        chatMessage.setContent(path);
        chatMessage.setType("image");
        chatMessage.setTimeSendMessage(timeSendMessage);

        db.collection("messages")
                .add(chatMessage)
                .addOnCompleteListener(task -> {
                    binding.edtMessage.setText("");

                    // update group chat
                    HashMap<String, Object> maps = new HashMap<>();
                    maps.put("lastMessage", "Tin nhắn hình ảnh");
                    maps.put("timeLastMessage", timeSendMessage);

                    db.collection("topics")
                            .document(topic.getDocumentId())
                            .update(maps);
                })
                .addOnFailureListener(e -> {
                    binding.edtMessage.setText("");
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            try {
                File f = FileUtils.getFile(this, data.getData());
                RequestBody requestFile = RequestBody.create(MultipartBody.FORM, f);

                MultipartBody.Part filePartImage = MultipartBody.Part.createFormData("file", "image.png", requestFile);

                binding.layoutLoading.setVisibility(View.VISIBLE);
                service.uploadFile(filePartImage).enqueue(new Callback<BaseResult<Path>>() {
                    @Override
                    public void onResponse(@NonNull Call<BaseResult<Path>> call, @NonNull Response<BaseResult<Path>> response) {
                        if (response.body() != null && !TextUtils.isEmpty(response.body().getData().getPath())) {
                            binding.layoutLoading.setVisibility(View.GONE);
                            sendImageMessage(response.body().getData().getPath());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BaseResult<edu.re.estate.data.models.Path>> call, @NonNull Throwable throwable) {
                        binding.layoutLoading.setVisibility(View.GONE);
                        Toast.makeText(ChatActivity.this, "Hiện tại không thể gửi được hình ảnh!", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                Log.e("TAGs", "Get images is error!");
                binding.layoutLoading.setVisibility(View.GONE);
                Toast.makeText(this, "Hiện tại không thể gửi được hình ảnh!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
