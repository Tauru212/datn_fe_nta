package edu.re.estate.data.source.repository;

import java.util.List;

import edu.re.estate.components.ExCallback;
import edu.re.estate.data.models.Token;
import edu.re.estate.data.models.User;
import edu.re.estate.data.request.LoginRequest;

public interface AuthRepository {

    void login(LoginRequest body, ExCallback<Token> callback);

    void myInfo(String accessToken, ExCallback<User> callback);

    void getInfoUser(int accountId, ExCallback<User> callback);

    void getUsers(String accessToken, ExCallback<List<User>> callback);
}
