package edu.re.estate.data.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Post implements Serializable {

    @SerializedName("post_id")
    private int postId;
    @SerializedName("post_type")
    private String postType;
    @SerializedName("city_id")
    private String cityId;
    @SerializedName("district_id")
    private String districtId;
    @SerializedName("commune_id")
    private String communeId;
    @SerializedName("address")
    private String address;
    @SerializedName("re_state_type")
    private String reStateType;
    @SerializedName("acreage")
    private String acreage;
    @SerializedName("price")
    private String price;
    @SerializedName("legal_documents")
    private String legalDocuments;
    @SerializedName("interior")
    private String interior;
    @SerializedName("n_bedrooms")
    private int nBedrooms;
    @SerializedName("n_bathrooms")
    private int nBathrooms;
    @SerializedName("direction")
    private String direction;
    @SerializedName("account_id")
    private int accountId;
    @SerializedName("contacts_name")
    private String contactsName;
    @SerializedName("contacts_email")
    private String contactsEmail;
    @SerializedName("contacts_phone")
    private String contactsPhone;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("create_at")
    private String createAt;
    @SerializedName("images")
    private List<ImagePost> images;
    @SerializedName("liked")
    private boolean liked;
    private String status;
    private String state;

    public Post(int postId, String postType, String cityId, String districtId, String communeId, String address, String reStateType, String acreage, String price, String legalDocuments, String interior, int nBedrooms, int nBathrooms, String direction, int accountId, String contactsName, String contactsEmail, String contactsPhone, String title, String description, String createAt, List<ImagePost> images, boolean liked, String status, String state) {
        this.postId = postId;
        this.postType = postType;
        this.cityId = cityId;
        this.districtId = districtId;
        this.communeId = communeId;
        this.address = address;
        this.reStateType = reStateType;
        this.acreage = acreage;
        this.price = price;
        this.legalDocuments = legalDocuments;
        this.interior = interior;
        this.nBedrooms = nBedrooms;
        this.nBathrooms = nBathrooms;
        this.direction = direction;
        this.accountId = accountId;
        this.contactsName = contactsName;
        this.contactsEmail = contactsEmail;
        this.contactsPhone = contactsPhone;
        this.title = title;
        this.description = description;
        this.createAt = createAt;
        this.images = images;
        this.liked = liked;
        this.status = status;
        this.state = state;
    }

    public Post() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReStateType() {
        return reStateType;
    }

    public void setReStateType(String reStateType) {
        this.reStateType = reStateType;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getCommuneId() {
        return communeId;
    }

    public void setCommuneId(String communeId) {
        this.communeId = communeId;
    }

    public String getAcreage() {
        return acreage;
    }

    public void setAcreage(String acreage) {
        this.acreage = acreage;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLegalDocuments() {
        return legalDocuments;
    }

    public void setLegalDocuments(String legalDocuments) {
        this.legalDocuments = legalDocuments;
    }

    public String getInterior() {
        return interior;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }

    public int getnBedrooms() {
        return nBedrooms;
    }

    public void setnBedrooms(int nBedrooms) {
        this.nBedrooms = nBedrooms;
    }

    public int getnBathrooms() {
        return nBathrooms;
    }

    public void setnBathrooms(int nBathrooms) {
        this.nBathrooms = nBathrooms;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getContactsEmail() {
        return contactsEmail;
    }

    public void setContactsEmail(String contactsEmail) {
        this.contactsEmail = contactsEmail;
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public List<ImagePost> getImages() {
        return images;
    }

    public void setImages(List<ImagePost> images) {
        this.images = images;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
