package edu.re.estate.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class District {

    private String Id;
    private String Name;
    @SerializedName("Wards")
    private List<Ward> wards;

    public District(String id, String name, List<Ward> wards) {
        Id = id;
        Name = name;
        this.wards = wards;
    }

    public District() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<Ward> getWards() {
        return wards;
    }

    public void setWards(List<Ward> wards) {
        this.wards = wards;
    }
}
