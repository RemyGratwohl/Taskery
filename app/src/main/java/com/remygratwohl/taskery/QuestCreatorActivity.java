package com.remygratwohl.taskery;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.remygratwohl.taskery.database.DatabaseHelper;
import com.remygratwohl.taskery.models.Quest;
import com.remygratwohl.taskery.models.SessionManager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

public class QuestCreatorActivity extends AppCompatActivity {

    private TextView mQuestName;
    private TextView mQuestDescription;
    private TextView mQuestExpiryDate;
    private Spinner  mDifficultySpinner;

    private View mCreatorView;
    private View mProgressView;

    private CreateQuestTask mQuestTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_creator);

        setTitle("Create a New Quest");

        mQuestName = (TextView) findViewById(R.id.questname);
        mQuestDescription = (TextView) findViewById(R.id.questdescription);
        mQuestExpiryDate = (TextView) findViewById(R.id.questexpirydate);
        mProgressView = findViewById(R.id.quest_creator_pb);
        mCreatorView = findViewById(R.id.quest_creator_scroll_view);
        mDifficultySpinner = (Spinner) findViewById(R.id.difficulty_dropdown);

        mQuestExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment f = new DatePickerFragment();
                f.show(getSupportFragmentManager(),"datePicker");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quest_creator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.slide_from_the_left, R.anim.slide_to_the_right);
            return true;
        }else if(item.getItemId() == R.id.save_quest){

            Quest q = new Quest(mQuestName.getText().toString(),
                    mQuestDescription.getText().toString());

            q.setDifficulty(mDifficultySpinner.getSelectedItemPosition());

            SimpleDateFormat format = new SimpleDateFormat("MMMM dd yyyy", Locale.US);
            try{
                Date date = format.parse(mQuestExpiryDate.getText().toString());
                q.setExpiryDate(date);
            }catch(ParseException e){
                e.printStackTrace();
            }
            
            DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());
            long id = dbh.createQuest(q);
            //TODO retrieve quests from the database with the dates
            q = dbh.getQuest(id);

            showProgress(true);
            mQuestTask = new CreateQuestTask(q);
            mQuestTask.execute((Void) null);

            // TODO: UNABLE TO SAVE HANDLER
            finish();
            overridePendingTransition(R.anim.slide_from_the_left, R.anim.slide_to_the_right);
            return true;
        }

        return false;
    }



    public static class DatePickerFragment extends android.support.v4.app.DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "None", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ((EditText)getActivity().findViewById(R.id.questexpirydate)).setText(
                            "None"
                    );
                    dialog.dismiss();
                }
            });

            return dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            Calendar c = Calendar.getInstance();
            c.set(year, month, day);

            SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd yyyy", Locale.US);

            ((EditText)getActivity().findViewById(R.id.questexpirydate)).setText(
                    sdf.format(c.getTime())
            );
        }
    }

    private class CreateQuestTask extends AsyncTask<Void, Void, Response<JsonObject>>{

        private final Quest q;

        CreateQuestTask(Quest q) { this.q = q;}

        @Override
        protected Response<JsonObject> doInBackground(Void... params){
            TaskeryAPI api = TaskeryAPI.retrofit.create(TaskeryAPI.class);

            try{
                SessionManager s = new SessionManager(getApplicationContext());

                Call<JsonObject> call = api.sendQuest(s.retrieveUserToken(), q);
                Response<JsonObject> response = call.execute();
                return response;
            }catch(IOException e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Response<JsonObject> response){
            mQuestTask = null;
            showProgress(false);
            Log.d("LOG",response.body().toString());
        }
    }

    private void showProgress(final boolean show) {
        // Use these APIs to fade-in the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mCreatorView.setVisibility(show ? View.GONE : View.VISIBLE);
        mCreatorView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCreatorView.setVisibility(show ? View.GONE : View.VISIBLE);
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
