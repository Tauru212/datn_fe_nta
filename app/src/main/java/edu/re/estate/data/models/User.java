package edu.re.estate.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("account_id")
    private int accountId;

    private String email;
    private String role;
    private String name;
    private String phone;
    private String address;
    private String birthday;
    private String gender;
    private String avatar;

    @SerializedName("create_at")
    private String createAt;

    public User(int accountId, String email, String role, String name, String phone, String address, String birthday, String gender, String avatar, String createAt) {
        this.accountId = accountId;
        this.email = email;
        this.role = role;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.birthday = birthday;
        this.gender = gender;
        this.avatar = avatar;
        this.createAt = createAt;
    }

    public User() {
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
