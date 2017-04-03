package com.remygratwohl.taskery.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.remygratwohl.taskery.R;
import com.remygratwohl.taskery.models.CharacterClassAdapter;
import com.remygratwohl.taskery.models.Quest;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Remy on 3/30/2017.
 */

public class QuestAdapter extends RecyclerView.Adapter<QuestAdapter.QuestViewHolder> {
    private ArrayList<Quest> items;

    public QuestAdapter(ArrayList<Quest> items){
        this.items = items;
    }

    public static class QuestViewHolder extends  RecyclerView.ViewHolder{

        private TextView mQuestName;
        private TextView mQuestDescription;

        public QuestViewHolder(final View itemView) {
            super(itemView);
                this.mQuestName = (TextView) itemView.findViewById(R.id.quest_name);
                this.mQuestDescription = (TextView) itemView.findViewById(R.id.quest_description);
        }

    }

    @Override
    public QuestAdapter.QuestViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.quest_recyclerview_item_row, parent, false);

        return new QuestAdapter.QuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final QuestAdapter.QuestViewHolder holder, final int listPosition) {

        TextView textViewName = holder.mQuestName;
        TextView textViewDescription = holder.mQuestDescription;

        textViewName.setText(items.get(listPosition).getName());
        textViewDescription.setText(items.get(listPosition).getDescription());
    }

    @Override
    public int getItemCount(){
        return items.size();
    }
}
