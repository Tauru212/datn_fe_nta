package edu.re.estate.data.entity;

import java.util.List;

public class VietNamAddress {

    private List<City> city;

    public VietNamAddress(List<City> city) {
        this.city = city;
    }

    public VietNamAddress() {
    }

    public List<City> getCity() {
        return city;
    }

    public void setCity(List<City> city) {
        this.city = city;
    }
}