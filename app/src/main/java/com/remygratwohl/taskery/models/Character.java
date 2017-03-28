package com.remygratwohl.taskery.models;

/**
 * Created by Remy on 3/27/2017.
 */

public class Character {
    private int id;
    private String name;
    private CharacterClass class_type;
    private Quest[] quests;

    //Stats
    private int numQuestsCompleted;
    private int numGold;

}
