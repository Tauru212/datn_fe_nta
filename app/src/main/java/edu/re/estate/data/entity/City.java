package edu.re.estate.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class City {
    private String Id;
    private String Name;
    @SerializedName("Districts")
    private List<District> districts;

    public City(String id, String name, List<District> districts) {
        Id = id;
        Name = name;
        this.districts = districts;
    }

    public City() {
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

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }
}
