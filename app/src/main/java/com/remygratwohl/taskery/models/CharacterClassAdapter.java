package com.remygratwohl.taskery.models;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.remygratwohl.taskery.R;

import java.util.ArrayList;

/**
 * Created by Remy on 3/26/2017.
 */

public class CharacterClassAdapter extends RecyclerView.Adapter<CharacterClassAdapter.MyViewHolder>{

    private ArrayList<CharacterClass> data;

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView mCharacterName;
        TextView mCharacterDescription;
        ImageView mCoverImage;

        public MyViewHolder(final View itemView) {
            super(itemView);

            this.mCharacterName = (TextView) itemView.findViewById(R.id.titleTextView);
            this.mCharacterDescription = (TextView) itemView.findViewById(R.id.descriptionTextView);
            this.mCoverImage = (ImageView) itemView.findViewById(R.id.coverImageView);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override public void onClick(View v) {

                    int itemPos = getAdapterPosition();

                    Log.d("Test","Clicked item - " + itemPos);

                    //TODO: Popup alert confirmation
                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    alert.setTitle("Confirmation");
                    alert.setMessage("Are you sure you want to choose " +
                            data.get(itemPos).getName() +
                            " as your class? This can not be changed later");

                    alert.setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Create new Character object with chosen class
                                    // writes it to the database
                                    // transition to main view.
                                    Character playerCharacter = new Character();
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
            });
        }
    }

    public CharacterClassAdapter(ArrayList<CharacterClass> data) {
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.character_class_select_card, parent, false);


        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.mCharacterName;
        TextView textViewDescription = holder.mCharacterDescription;
        ImageView imageView = holder.mCoverImage;

        textViewName.setText(data.get(listPosition).getName());
        textViewDescription.setText(data.get(listPosition).getDescription());
        imageView.setImageResource(data.get(listPosition).getImageID());
    }

    @Override
    public int getItemCount(){
        return data.size();
    }
}

