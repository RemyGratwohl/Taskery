package com.remygratwohl.taskery.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Remy on 3/27/2017.
 */

public class Character {
    private int id;
    private String name;
    private int char_class_id;
    private ArrayList<Quest> quests;

    // Database
    private Date createdAt;
    private Date updatedAt;

    //Stats
    private int numQuestsCompleted;
    private int numGold;
    private Date lastLogin;
    private int currentHP;
    private int maxHP;
    private int xp;


    // For new characters
    public Character(String name, int char_class_id) {
        this.name = name;
        this.char_class_id = char_class_id;

        // Defaults
        this.createdAt = null;
        this.updatedAt = null;
        quests = new ArrayList<>();
        numQuestsCompleted = 0;
        numGold = 0;
        lastLogin = null;
        currentHP = 100;
        maxHP   = 100;
        xp = 0;
    }

    // TODO: Constructor for importing characters from server

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getNumQuestsCompleted() {
        return numQuestsCompleted;
    }

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
        return "Character{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", char_class_id=" + char_class_id +
                ", quests=" + quests +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", numQuestsCompleted=" + numQuestsCompleted +
                ", numGold=" + numGold +
                ", lastLogin=" + lastLogin +
                ", currentHP=" + currentHP +
                ", maxHP=" + maxHP +
                ", xp=" + xp +
                '}';
    }

    /*
    public static int convertLevelToXp(int level){
        return
    } */
}
