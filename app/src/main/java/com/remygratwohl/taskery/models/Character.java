package com.remygratwohl.taskery.models;

import java.util.ArrayList;

/**
 * Created by Remy on 3/27/2017.
 */

public class Character {
    private int id;
    private String name;
    private int char_class_id;
    private ArrayList<Quest> quests;

    //Stats
    private int numQuestsCompleted;
    private int numGold;

    // For new characters
    public Character(String name, int char_class_id) {
        this.name = name;
        this.char_class_id = char_class_id;

        quests = new ArrayList<>();
        numQuestsCompleted = 0;
        numGold = 0;
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
}
