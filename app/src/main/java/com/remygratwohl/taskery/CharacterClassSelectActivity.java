package com.remygratwohl.taskery;

import android.support.constraint.ConstraintLayout;
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

import com.remygratwohl.taskery.models.CharacterClass;
import com.remygratwohl.taskery.models.CharacterClassAdapter;
import com.remygratwohl.taskery.models.CharacterClassData;

import java.util.ArrayList;

public class CharacterClassSelectActivity extends AppCompatActivity {

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

        adapter = new CharacterClassAdapter(data);
        recyclerView.setAdapter(adapter);


    }

    private void populateCharacterData(ArrayList<CharacterClass> data){
        for(int i = 0; i < CharacterClassData.nameArray.length; i ++){
            data.add(new CharacterClass(CharacterClassData.nameArray[i],
                    CharacterClassData.descriptionArray[i],
                    CharacterClassData.imageArray[i]
            ));
        }
    }
}
