package edu.re.estate.components;

public interface ExCallback<T> {
    void onResponse(T data);

    void onFailure(Throwable var2);
}
