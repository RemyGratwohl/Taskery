package com.remygratwohl.taskery.models;

import java.util.Date;

/**
 * Created by Remy on 3/27/2017.
 */

public class Quest {


    private long id;
    private String name;
    private String description;
    private boolean hasBeenCompleted;
    private Date expiryDate;
    private int difficulty;

    private Date createdAt;
    private Date updatedAt;


    public Quest(String name, String description) {
        this.name = name;
        this.description = description;
        this.hasBeenCompleted = false;
        this.expiryDate = null;
        this.createdAt = null;
        this.updatedAt = null;
    }

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

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean hasBeenCompleted() {
        return hasBeenCompleted;
    }

    public void setHasBeenCompleted(boolean hasBeenCompleted) {
        this.hasBeenCompleted = hasBeenCompleted;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Quest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", hasBeenCompleted=" + hasBeenCompleted +
                ", expiryDate=" + expiryDate +
                ", difficulty=" + difficulty +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
