package com.remygratwohl.taskery.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Remy on 3/27/2017.
 */

public class Character {

    private String name;
    private int char_class_id;

    private ArrayList<Quest> quests;
    private ArrayList<Quest> questsCompleted;

    private Date createdAt;
    private Date updatedAt;

    // Statistics
    private int numQuestsCompleted;
    private int numGold;
    private int currentHP;
    private int maxHP;
    private int xp;
    private Date lastLogin;

    public Character(String name, int char_class_id) {
        this.name = name;
        this.char_class_id = char_class_id;

        // Defaults
        this.createdAt = null;
        this.updatedAt = null;
        this.quests = new ArrayList<>();
        this.questsCompleted = new ArrayList<>();
        this.numQuestsCompleted = 0;
        this.numGold = 0;
        this.lastLogin = null;
        this.currentHP = 100;
        this.maxHP   = 100;
        this.xp = 0;
    }

    public String getName() {
        return name;
    }

    public int getChar_class_id() {
        return char_class_id;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public ArrayList<Quest> getCompletedQuests() { return questsCompleted; }

    public int getNumGold() {
        return numGold;
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

    public int getMaxHP() {
        return maxHP;
    }

    public void setMaxHP(int maxHP) {
        this.maxHP = maxHP;
    }

    public int getCurrentHP() {
        return currentHP;
    }

    public void setCurrentHP(int currentHP) {
        this.currentHP = currentHP;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", char_class_id=" + char_class_id +
                ", quests=" + quests +
                ", questsCompleted=" + questsCompleted +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", numQuestsCompleted=" + numQuestsCompleted +
                ", numGold=" + numGold +
                ", currentHP=" + currentHP +
                ", maxHP=" + maxHP +
                ", xp=" + xp +
                ", lastLogin=" + lastLogin +
                '}';
    }

/*
    public static int convertLevelToXp(int level){
        return
    } */
}
