package com.remygratwohl.taskery.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.remygratwohl.taskery.models.Quest;

/**
 * Created by Remy on 3/27/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logging Tag
    private static final String LOG = "Database";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "taskerydb";

    // Table Names
    private static final String TABLE_QUEST = "quests";

    // COMMON Table - Columns
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String KEY_UPDATED_AT = "updatedAt";

    // QUESTS Table - Columns
    private static final String KEY_QUEST_NAME = "quest_name";
    private static final String KEY_QUEST_DESC = "quest_desc";

    // Create Quest Table
    private static final String CREATE_TABLE_QUEST = "CREATE TABLE "
            + TABLE_QUEST + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_QUEST_NAME + " TEXT,"
            + KEY_QUEST_DESC + " TEXT," + KEY_CREATED_AT + " DATETIME," + KEY_UPDATED_AT
            + " DATETIME" + ")";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // Create Tables
        db.execSQL(CREATE_TABLE_QUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        Log.w(LOG, "Upgrading database from version " + oldVersion + " to "
                        + newVersion);

        // Drop and recreate the tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUEST);
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

        db.insert(TABLE_QUEST, null, values);

        return true;
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
