package com.remygratwohl.taskery.models;

import com.remygratwohl.taskery.R;

import java.util.ArrayList;

/**
 * Created by Remy on 3/26/2017.
 */

public class CharacterClassData {

    private static int NUM_CLASSES = 3;

    public static ArrayList<CharacterClass> getCharacterClasses(){
        ArrayList<CharacterClass> list = new ArrayList<>();
        for(int i = 0; i < nameArray.length; i ++){
            list.add(getCharacterClassAtID(i));
        }
        return list;
    }

    public static CharacterClass getCharacterClassAtID(int i){
        try{
            return new CharacterClass(i, CharacterClassData.nameArray[i],
                    CharacterClassData.descriptionArray[i],
                    CharacterClassData.imageArray[i]
            );
        }catch(IndexOutOfBoundsException e){
            throw e;
        }
    }

    public static int getNumClasses(){
        return NUM_CLASSES;
    }

    private static String[] nameArray = {"Knight", "Mercenary", "Sorceress"};
    private static String[] descriptionArray = {"A noble warrior chasing honour and strength",
    "A lawless killer for hire, stopping at nothing in the pursuit of wealth",
    "A unbelievably intelligent sorceress with a wide variety of magical abilities. " +
            "Often able to outsmart their opponents, they will stop at nothing in the" +
            " pursuit of knowledge "};
    private static int[] imageArray = {R.drawable.knight_banner, R.drawable.mercenary_banner,
            R.drawable.sorceress_banner};

 }
