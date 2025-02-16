package edu.re.estate.data.request;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PostRequest implements Serializable {
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
    private int bedrooms;

    @SerializedName("n_bathrooms")
    private int bathrooms;

    @SerializedName("direction")
    private String direction;

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

    public PostRequest(String postType, String cityId, String districtId, String communeId, String address, String reStateType, String acreage, String price, String legalDocuments, String interior, int bedrooms, int bathrooms, String direction, String contactsName, String contactsEmail, String contactsPhone, String title, String description) {
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
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.direction = direction;
        this.contactsName = contactsName;
        this.contactsEmail = contactsEmail;
        this.contactsPhone = contactsPhone;
        this.title = title;
        this.description = description;
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

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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
}
