package com.remygratwohl.taskery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.remygratwohl.taskery.models.Character;
import com.remygratwohl.taskery.models.CharacterAdapter;
import com.remygratwohl.taskery.models.CharacterData;

import java.util.ArrayList;

public class CharacterSelectActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Character> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_select);

        setTitle("Choose your Character");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<>();
        populateCharacterData(data);

        adapter = new CharacterAdapter(data);
        recyclerView.setAdapter(adapter);

    }

    private void populateCharacterData(ArrayList<Character> data){
        for(int i = 0; i < CharacterData.nameArray.length; i ++){
            data.add(new Character(CharacterData.nameArray[i],
                                    CharacterData.descriptionArray[i], 0
            ));
        }
    }
}
