package com.remygratwohl.taskery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.remygratwohl.taskery.database.DatabaseHelper;
import com.remygratwohl.taskery.models.Character;
import com.remygratwohl.taskery.models.SessionManager;

import org.w3c.dom.Text;

public class StatsActivity extends AppCompatActivity {

    private Character character;

    private TextView mCharacterName;
    private ProgressBar mCharacterHealth;
    private TextView mCharacterHealthPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);


        DatabaseHelper dbh = new DatabaseHelper(getApplicationContext());
        SessionManager sm = new SessionManager(getApplicationContext());

        character = dbh.getCharacter(sm.retrieveSessionsUser().getEmail());

        mCharacterName = (TextView) findViewById(R.id.character_name);
        mCharacterHealth = (ProgressBar) findViewById(R.id.health_bar);
        mCharacterHealthPercentage = (TextView) findViewById(R.id.health_bar_percentage);

        mCharacterHealthPercentage.setText(character.getCurrentHP() + "/" + character.getMaxHP());

        mCharacterName.setText(character.getName());
        mCharacterHealth.setMax(character.getMaxHP());
        mCharacterHealth.setProgress(character.getCurrentHP());


    }
}
