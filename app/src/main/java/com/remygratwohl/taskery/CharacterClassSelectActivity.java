package com.remygratwohl.taskery;

import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.remygratwohl.taskery.models.Character;
import com.remygratwohl.taskery.models.CharacterClass;
import com.remygratwohl.taskery.models.CharacterClassAdapter;
import com.remygratwohl.taskery.models.CharacterClassData;

import java.util.ArrayList;

public class CharacterClassSelectActivity extends AppCompatActivity implements CharacterClassAdapter.AdapterCallback{

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CharacterClass> data;
    private static EditText characterNameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_class_select);

        setTitle("Pick a Class");

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
                                characterNameText.getText().toString(),role.getId());

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

    @Override
    public void onBackPressed(){
        // Do nothing :^)
    }

    private void populateCharacterData(ArrayList<CharacterClass> data){
        for(int i = 0; i < CharacterClassData.getNumClasses() - 1; i ++){
            data.add(CharacterClassData.getCharacterClassAtID(i));
        }
    }
}
