package com.remygratwohl.taskery.models;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.remygratwohl.taskery.R;

import java.util.ArrayList;

/**
 * Created by Remy on 3/26/2017.
 */

public class CharacterAdapter  extends RecyclerView.Adapter<CharacterAdapter.MyViewHolder>{

    private ArrayList<Character> data;

    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView mCharacterName;
        TextView mCharacterDescription;

        public MyViewHolder(View itemView) {
            super(itemView);

            this.mCharacterName = (TextView) itemView.findViewById(R.id.titleTextView);
            this.mCharacterDescription = (TextView) itemView.findViewById(R.id.descriptionTextView);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override public void onClick(View v) {
                    Log.d("Test","Clicked!" + getAdapterPosition());

                }
            });
        }
    }

    public CharacterAdapter(ArrayList<Character> data) {
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.character_select_card, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.mCharacterName;
        TextView textViewDescription = holder.mCharacterDescription;

        textViewName.setText(data.get(listPosition).getName());
        textViewDescription.setText(data.get(listPosition).getDescription());
    }


    @Override
    public int getItemCount(){
        return data.size();
    }
}

