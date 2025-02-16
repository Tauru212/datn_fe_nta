package edu.re.estate.data.models;

public class Address {

    private String cityId;
    private String districtId;
    private String wardId;

    public Address(String cityId, String districtId, String wardId) {
        this.cityId = cityId;
        this.districtId = districtId;
        this.wardId = wardId;
    }

    public Address() {
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

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }
}
