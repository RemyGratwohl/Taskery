package com.remygratwohl.taskery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.remygratwohl.taskery.database.DatabaseHelper;
import com.remygratwohl.taskery.models.Character;
import com.remygratwohl.taskery.models.CharacterClass;
import com.remygratwohl.taskery.models.CharacterClassAdapter;
import com.remygratwohl.taskery.models.CharacterClassData;
import com.remygratwohl.taskery.models.SessionManager;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

public class CharacterClassSelectActivity extends AppCompatActivity implements CharacterClassAdapter.AdapterCallback{

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CharacterClass> data;
    private static EditText characterNameText;

    private View mProgressView;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private CreateCharacterTask mRegTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_class_select);

        setTitle("Pick a Class");

        mProgressView = findViewById(R.id.create_character_progress);

        characterNameText = (EditText) findViewById(R.id.characterName);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<>();
        populateCharacterData(data);

        adapter = new CharacterClassAdapter(data, this);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onMethodCallback(CharacterClass c){

        final CharacterClass role = c;
        final String characterName = characterNameText.getText().toString();
        View focusView;

        if (TextUtils.isEmpty(characterName)) {
            characterNameText.setError(getString(R.string.error_missing_char_name));
            focusView = characterNameText;
            focusView.requestFocus();
        }else {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Confirmation");
            alert.setMessage("Are you sure you want to choose the " +
                    c.getName() +
                    " as your class? This can not be changed later");

            alert.setPositiveButton("Yes",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Create new Character object with chosen class
                            // writes it to the database
                            // transition to main view.

                            Character playerCharacter = new Character(
                                    characterName, role.getId());

                            DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());
                            SessionManager sm = new SessionManager(getApplicationContext());

                            dbh.createCharacter(playerCharacter,sm.retrieveSessionsUser());
                            playerCharacter = dbh.getCharacter(sm.retrieveSessionsUser().getEmail());

                            Log.d("LOG", playerCharacter.toString());

                            showProgress(true);
                            mRegTask = new CreateCharacterTask(playerCharacter);
                            mRegTask.execute((Void) null);

                            Intent intent = new Intent(CharacterClassSelectActivity.this,
                                    QuestLogActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });

            alert.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            alert.show();
        }
    }

    @Override
    public void onBackPressed(){
        // Do nothing :^)
    }

    private class CreateCharacterTask extends AsyncTask<Void, Void, Response<JsonObject>>{

        private final Character c;

        CreateCharacterTask(Character c){
            this.c = c;
        }

        @Override
        protected Response<JsonObject> doInBackground(Void... params){
            TaskeryAPI taskeryAPI = TaskeryAPI.retrofit.create(TaskeryAPI.class);

            try{
                SessionManager s = new SessionManager(getApplicationContext());

                Call<JsonObject> call = taskeryAPI.sendCharacter(s.retrieveUserToken(), c);
                Response<JsonObject> response = call.execute();
                return response;
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Response<JsonObject> response) {
            mRegTask = null;
            showProgress(false);
            Log.d("LOG",response.body().toString());
        }

        @Override
        protected void onCancelled() {
            mRegTask = null;
            showProgress(false);
        }
    }

    private void populateCharacterData(ArrayList<CharacterClass> data){
        for(int i = 0; i < CharacterClassData.getNumClasses(); i ++){
            data.add(CharacterClassData.getCharacterClassAtID(i));
        }
    }

    /**
     * Shows the progress UI and disable the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // Use these APIs to fade-in the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
        recyclerView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                recyclerView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        characterNameText.setVisibility(show ? View.GONE : View.VISIBLE);
        characterNameText.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                characterNameText.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
}
