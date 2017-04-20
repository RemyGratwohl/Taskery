package com.remygratwohl.taskery.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.remygratwohl.taskery.models.Character;
import com.remygratwohl.taskery.models.Quest;
import com.remygratwohl.taskery.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Class to Help with Database access
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logging Tag
    private static final String LOG = "Database";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "taskery.db";

    // Table Names
    private static final String TABLE_QUEST = "quests";
    private static final String TABLE_CHARACTER = "characters";

    // COMMON Table - Columns
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_UPDATED_AT = "updatedAt";

    // QUESTS Table - Columns
    private static final String KEY_QUEST_NAME = "quest_name";
    private static final String KEY_QUEST_DESC = "quest_desc";
    private static final String KEY_QUEST_DIFF = "quest_diff";
    private static final String KEY_QUEST_EXPIRY = "quest_expiry";
    private static final String KEY_QUEST_HAS_BEEN_COMPLETED = "quest_has_been_completed";

    // CHARACTERS Table - Columns
    private static final String KEY_CHAR_NAME = "char_name";
    private static final String KEY_CHAR_USER = "char_user";
    private static final String KEY_CHAR_ROLE   = "char_role_id";
    private static final String KEY_CHAR_MAXHP = "char_max_hp";
    private static final String KEY_CHAR_HP = "char_hp";
    private static final String KEY_CHAR_XP = "char_xp";
    private static final String KEY_CHAR_GOLD = "char_gold";

    // Create Quest Table
    private static final String CREATE_TABLE_QUEST = "CREATE TABLE "
            + TABLE_QUEST + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_QUEST_NAME + " TEXT,"
            + KEY_QUEST_DESC +  " TEXT," + KEY_QUEST_DIFF + " INTEGER,"
            + KEY_QUEST_HAS_BEEN_COMPLETED + " INTEGER," + KEY_QUEST_EXPIRY + " DATETIME,"
            + KEY_CREATED_AT + " DATETIME," + KEY_UPDATED_AT + " DATETIME" + ")";

    // Create Character Table
    private static final String CREATE_TABLE_CHARACTER = "CREATE TABLE " + TABLE_CHARACTER + "(" +
            KEY_CHAR_USER + " TEXT PRIMARY KEY," + KEY_CHAR_NAME + " TEXT," + KEY_CHAR_ROLE +
            " INTEGER," + KEY_CREATED_AT + " DATETIME," + KEY_UPDATED_AT
            + " DATETIME" + ")";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // Create Tables
        db.execSQL(CREATE_TABLE_QUEST);
        db.execSQL(CREATE_TABLE_CHARACTER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        Log.w(LOG, "Upgrading database from version " + oldVersion + " to "
                        + newVersion);

        // Drop and recreate the tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARACTER);
        onCreate(db);
    }

    /*
     * CRUD OPERATIONS
     */

    public long createQuest(Quest q){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUEST_NAME, q.getName());
        values.put(KEY_QUEST_DESC, q.getDescription());
        values.put(KEY_QUEST_DIFF, q.getDifficulty());
        values.put(KEY_QUEST_HAS_BEEN_COMPLETED, q.hasBeenCompleted() ? 1 : 0);

        if(q.getExpiryDate() != null){
            values.put(KEY_QUEST_EXPIRY, convertDateToString(q.getExpiryDate()));
        }


        if(q.getCreatedAt() == null){
            values.put(KEY_CREATED_AT, getDateTime());
        }else{
            values.put(KEY_CREATED_AT, convertDateToString(q.getCreatedAt()));
        }

        if(q.getUpdatedAt() == null){
            values.put(KEY_UPDATED_AT, getDateTime());
        }else{
            values.put(KEY_UPDATED_AT, convertDateToString(q.getUpdatedAt()));
        }

        try{
            return db.insertOrThrow(TABLE_QUEST, null, values);
        } catch (SQLiteConstraintException e){
            e.printStackTrace();
            return -1;
        }
    }

    public boolean createCharacter(Character c, User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CHAR_USER, user.getEmail());
        values.put(KEY_CHAR_NAME, c.getName());
        values.put(KEY_CHAR_ROLE, c.getChar_class_id());

        if(c.getCreatedAt() == null){
            values.put(KEY_CREATED_AT, getDateTime());
        }else{
            values.put(KEY_CREATED_AT, convertDateToString(c.getCreatedAt()));
        }

        if(c.getUpdatedAt() == null){
            values.put(KEY_UPDATED_AT, getDateTime());
        }else{
            values.put(KEY_UPDATED_AT, convertDateToString(c.getUpdatedAt()));
        }

        if(c.getQuests().size() != 0){
            for(Quest q: c.getQuests()){
                createQuest(q);
            }
        }

        if(c.getCompletedQuests().size() != 0){
            for(Quest q: c.getCompletedQuests()){
                createQuest(q);
            }
        }

        try{
            db.insertOrThrow(TABLE_CHARACTER, null, values);
        } catch (SQLiteConstraintException e){
            Log.d(LOG,"Failed Inserting Character ", e);
            return false;
        }

        return true;
    }

    // returns the character specified by an account username
    public Character getCharacter(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CHARACTER + " WHERE "
                + KEY_CHAR_USER + " = '" + username + "'";

        //Log.d(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null && c.getCount() > 0 ){
            Log.d(LOG,"Query: '" + selectQuery + "' retrieved - " + c.toString());
            c.moveToFirst();
        }else{
            Log.d(LOG,"Query: '" + selectQuery + "' retrieved null");
            return null;
        }

        Character newCharacter = new Character(
                c.getString(c.getColumnIndex(KEY_CHAR_NAME)),
                c.getInt(c.getColumnIndex(KEY_CHAR_ROLE))
        );

        newCharacter.setCreatedAt(convertStringToDate(c.getString(c.getColumnIndex(KEY_CREATED_AT))));
        newCharacter.setUpdatedAt(convertStringToDate(c.getString(c.getColumnIndex(KEY_UPDATED_AT))));

        for(Quest q : getQuests() ){
            if(q.hasBeenCompleted()){
                Log.d(LOG + " CQUESTS", q.toString());
                newCharacter.getCompletedQuests().add(q);
            }else{
                Log.d(LOG + " QUESTS", q.toString());
                newCharacter.getQuests().add(q);
            }
        }

        c.close();
        return newCharacter;

    }

    public ArrayList<Quest> getQuests(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Quest> quests = new ArrayList<Quest>();

        // TODO: Make sure it's only this characters quest
        String selectQuery = "SELECT  * FROM " + TABLE_QUEST; //+ " WHERE "
                //+ KEY_CHAR_USER + " = '" + username + "'";
        //Log.d(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            do {
                Quest q = new Quest(c.getString(c.getColumnIndex(KEY_QUEST_NAME)),
                        c.getString(c.getColumnIndex(KEY_QUEST_DESC)));
                q.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                q.setExpiryDate(convertStringToDate(c.getString(c.getColumnIndex(KEY_QUEST_EXPIRY))));
                q.setCreatedAt(convertStringToDate(c.getString(c.getColumnIndex(KEY_CREATED_AT))));
                q.setUpdatedAt(convertStringToDate(c.getString(c.getColumnIndex(KEY_UPDATED_AT))));
                q.setHasBeenCompleted(c.getInt(c.getColumnIndex(KEY_QUEST_HAS_BEEN_COMPLETED)) != 0);

                //TODO: UPDATEDAT AND CREATED
                // adding to todo list
                quests.add(q);
            } while (c.moveToNext());
        }else{
            Log.d(LOG,"Query: '" + selectQuery + "' retrieved null");
            return new ArrayList<Quest>(); // Return empty list
        }
        return quests;
    }

    public Quest getQuest(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_QUEST + " WHERE "
        + KEY_ID + " = " + id;

        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            Quest q = new Quest(c.getString(c.getColumnIndex(KEY_QUEST_NAME)),
                    c.getString(c.getColumnIndex(KEY_QUEST_DESC)));
            q.setId(c.getInt((c.getColumnIndex(KEY_ID))));
            q.setExpiryDate(convertStringToDate(c.getString(c.getColumnIndex(KEY_QUEST_EXPIRY))));
            q.setCreatedAt(convertStringToDate(c.getString(c.getColumnIndex(KEY_CREATED_AT))));
            q.setUpdatedAt(convertStringToDate(c.getString(c.getColumnIndex(KEY_UPDATED_AT))));
            q.setHasBeenCompleted(c.getInt(c.getColumnIndex(KEY_QUEST_HAS_BEEN_COMPLETED)) != 0);

            return q;
        }else{
            return null;
        }


    }

    public long updateQuest(Quest q) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUEST_NAME, q.getName());
        values.put(KEY_QUEST_DESC, q.getDescription());
        values.put(KEY_QUEST_DIFF, q.getDifficulty());
        values.put(KEY_QUEST_HAS_BEEN_COMPLETED, q.hasBeenCompleted() ? 1 : 0);

        if(q.getExpiryDate() != null){
            values.put(KEY_QUEST_EXPIRY, convertDateToString(q.getExpiryDate()));
        }

        return db.update(TABLE_QUEST,values, KEY_ID + " = ?",
                new String[] { String.valueOf(q.getId())
        });
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private Date convertStringToDate(String dateString){

        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
            String dts = dateString.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)","$1$2");
            return formatter.parse(dts);
        }catch (Exception e) {
            return null;
        }
    }

    private String convertDateToString(Date date){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

        try{
            return formatter.format(date);
        }catch(Exception e){
            Log.d(LOG,e.toString());
            return null;
        }

    }


    public void deleteDB(Context c){
        c.deleteDatabase(DATABASE_NAME);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
