package edu.re.estate.data.entity;

public class Ward {

    private String Id;
    private String Name;

    public Ward(String id, String name) {
        Id = id;
        Name = name;
    }

    public Ward() {
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
}
