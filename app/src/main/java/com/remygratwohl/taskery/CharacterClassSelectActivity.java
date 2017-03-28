package com.remygratwohl.taskery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.remygratwohl.taskery.models.CharacterClass;
import com.remygratwohl.taskery.models.CharacterClassAdapter;
import com.remygratwohl.taskery.models.CharacterClassData;

import java.util.ArrayList;

public class CharacterClassSelectActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<CharacterClass> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_class_select);

        setTitle("Choose your Class");

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
                                    CharacterClassData.descriptionArray[i], 0
            ));
        }
    }
}
