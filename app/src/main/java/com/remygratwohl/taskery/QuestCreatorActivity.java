package com.remygratwohl.taskery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.remygratwohl.taskery.database.DatabaseHelper;
import com.remygratwohl.taskery.models.Quest;

public class QuestCreatorActivity extends AppCompatActivity {

    TextView mQuestName;
    TextView mQuestDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest_creator);

        setTitle("Create a New Quest");

        mQuestName = (TextView) findViewById(R.id.questname);
        mQuestDescription = (TextView) findViewById(R.id.questdescription);
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


            DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());

            dbh.createQuest(q);

            // TODO: UNABLE TO SAVE HANDLER
            finish();
            overridePendingTransition(R.anim.slide_from_the_left, R.anim.slide_to_the_right);
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_the_left, R.anim.slide_to_the_right);
    }
}
