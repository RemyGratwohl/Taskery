package com.remygratwohl.taskery.models;

/**
 *
 */

public class CharacterClass {
    private String name;
    private String description;
    private int imageID;

    public CharacterClass(String name, String description, int imageID) {
        this.name = name;
        this.description = description;
        this.imageID = imageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
