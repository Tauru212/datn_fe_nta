package edu.re.estate.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import edu.re.estate.components.ExCallback;
import edu.re.estate.data.entity.City;
import edu.re.estate.data.models.User;
import edu.re.estate.data.source.repository.AuthRepositoryImpl;

public class SessionManager {

    public static User currentUser;

    public static final ArrayList<City> cities = new ArrayList<>();

    private static String assetJSONFile(Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open("vietnamAddress.json");
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    public static void setCities(Context context) {
        new Thread(() -> {
            try {
                cities.clear();
                String result = assetJSONFile(context);
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    City newCity = new Gson().fromJson(jArray.getString(i), City.class);
                    cities.add(newCity);
                }
            } catch (Exception e) {
                Log.d("VietNamAddress", "Error: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }).start();
    }

    public static void refreshUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("re_state_shared_keys", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("access_token", null);

        if (!TextUtils.isEmpty(accessToken)) {
            AuthRepositoryImpl.getInstance().myInfo(accessToken, new ExCallback<User>() {
                @Override
                public void onResponse(User data) {
                    SessionManager.currentUser = data;
                }

                @Override
                public void onFailure(Throwable var2) {
                }
            });
        }
    }
}
