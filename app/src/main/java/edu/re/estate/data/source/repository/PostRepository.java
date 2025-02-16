package edu.re.estate.data.source.repository;

import java.util.List;

import edu.re.estate.components.ExCallback;
import edu.re.estate.data.models.Post;
import edu.re.estate.data.models.TotalPost;
import edu.re.estate.data.request.PostRequest;
import edu.re.estate.data.request.PutImageRequest;

public interface PostRepository {

    void getAll(String accessToken, ExCallback<List<Post>> callback);

    void post(String accessToken, PostRequest request, ExCallback<Integer> callbackPostRequest);

    void putImages(PutImageRequest request, ExCallback<String> callback);

    void getPostsByStatus(String status, ExCallback<List<Post>> callback);

    void getMyPostByStatus(int accountId, String status, ExCallback<List<Post>> callback);

    void approvedPost(String accessToken, int postId, ExCallback<String> callback);

    void removePost(String accessToken, int postId, ExCallback<String> callback);

    void processingPost(String accessToken, int postId, ExCallback<String> callback);

    void advertisementPost(String accessToken, int postId, ExCallback<String> callback);

    void rentedPost(String accessToken, int postId, ExCallback<String> callback);

    void refusePost(String accessToken, int postId, ExCallback<String> callback);

    void tickView(int postId);

    void highLightMarkPost(String accessToken, int postId, ExCallback<String> callback);

    void removeHighLightMarkPost(String accessToken, int postId, ExCallback<String> callback);

    void getAllHighLightMarkPost(ExCallback<List<Post>> callback);

    void statisticalPost(ExCallback<TotalPost> callback);

    void statisticalPostHighlightMark(ExCallback<Integer> callback);

    void statisticalPostViews(ExCallback<Integer> callback);

    void statisticalPostHighlightMarkViews(ExCallback<Integer> callback);
}
