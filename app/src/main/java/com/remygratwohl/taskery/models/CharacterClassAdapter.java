package com.remygratwohl.taskery.models;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.remygratwohl.taskery.R;

import java.util.ArrayList;

/**
 * Created by Remy on 3/26/2017.
 */

public class CharacterClassAdapter extends RecyclerView.Adapter<CharacterClassAdapter.MyViewHolder>{

    private ArrayList<CharacterClass> data;
    private AdapterCallback mAdapterCallback;

    public class MyViewHolder extends  RecyclerView.ViewHolder{

        private TextView mCharacterName;
        private TextView mCharacterDescription;
        private ImageView mCoverImage;

        public MyViewHolder(final View itemView) {
            super(itemView);

            this.mCharacterName = (TextView) itemView.findViewById(R.id.titleTextView);
            this.mCharacterDescription = (TextView) itemView.findViewById(R.id.descriptionTextView);
            this.mCoverImage = (ImageView) itemView.findViewById(R.id.coverImageView);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override public void onClick(View v) {

                    int itemPos = getAdapterPosition();
                    Log.d("Test","Clicked item - " + itemPos);

                    //Pass the selected class to the activity
                    mAdapterCallback.onMethodCallback(new CharacterClass(data.get(itemPos).getId(),data.get(itemPos).getName(),
                            data.get(itemPos).getDescription(),data.get(itemPos).getImageID()));
                }
            });
        }
    }

    public CharacterClassAdapter(ArrayList<CharacterClass> data, Context context) {
        this.data = data;

        try {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement AdapterCallback.");
        }
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

    public interface AdapterCallback{
        void onMethodCallback(CharacterClass c);
    }


    @Override
    public int getItemCount(){
        return data.size();
    }
}

