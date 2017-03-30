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
import java.util.Date;
import java.util.Locale;

/**
 * Created by Remy on 3/27/2017.
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

    // CHARACTERS Table - Columns
    private static final String KEY_CHAR_NAME = "char_name";
    private static final String KEY_CHAR_USER = "char_user";
    private static final String KEY_CHAR_ROLE   = "char_role_id";

    // Create Quest Table
    private static final String CREATE_TABLE_QUEST = "CREATE TABLE "
            + TABLE_QUEST + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_QUEST_NAME + " TEXT,"
            + KEY_QUEST_DESC + " TEXT," + KEY_CREATED_AT + " DATETIME," + KEY_UPDATED_AT
            + " DATETIME" + ")";

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

    public boolean createQuest(Quest q){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QUEST_NAME, q.getName());
        values.put(KEY_QUEST_DESC, q.getDescription());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_UPDATED_AT, getDateTime());

        try{
            db.insertOrThrow(TABLE_QUEST, null, values);
        } catch (SQLiteConstraintException e){
            Log.d(LOG,"Failed Inserting Quest ", e);
            return false;
        }

        return true;
    }

    public boolean createCharacter(Character c, User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CHAR_USER, user.getEmail());
        values.put(KEY_CHAR_NAME, c.getName());
        values.put(KEY_CHAR_ROLE, c.getChar_class_id());
        values.put(KEY_CREATED_AT, getDateTime());
        values.put(KEY_UPDATED_AT, getDateTime());

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

        Log.d(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null){
            c.moveToFirst();
        }else{
            return null;
        }

        Character newCharacter = new Character(c.getString(c.getColumnIndex(KEY_CHAR_NAME)),
                c.getInt(c.getColumnIndex(KEY_CHAR_ROLE)));

        newCharacter.setCreatedAt(convertStringtoDate(c.getString(c.getColumnIndex(KEY_CREATED_AT))));
        newCharacter.setUpdatedAt(convertStringtoDate(c.getString(c.getColumnIndex(KEY_UPDATED_AT))));
        return newCharacter;

    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSZ\"", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    private Date convertStringtoDate(String dateString){

        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            String dts = dateString.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)","$1$2");
            return formatter.parse(dts);
        }catch (Exception e) {
            return null;
        }
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
