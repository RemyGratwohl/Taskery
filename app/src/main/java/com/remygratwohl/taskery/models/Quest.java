package com.remygratwohl.taskery.models;

/**
 * Created by Remy on 3/27/2017.
 */

public class Quest {

    int id;
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        description = d;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}