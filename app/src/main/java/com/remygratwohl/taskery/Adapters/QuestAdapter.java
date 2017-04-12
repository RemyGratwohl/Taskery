package com.remygratwohl.taskery.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.remygratwohl.taskery.R;
import com.remygratwohl.taskery.database.DatabaseHelper;
import com.remygratwohl.taskery.models.Quest;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Remy on 3/30/2017.
 */

public class QuestAdapter extends RecyclerView.Adapter<QuestAdapter.QuestViewHolder> {

    private ArrayList<Quest> items;
    private Context mContext;

    public QuestAdapter(Context c,ArrayList<Quest> items){
        this.items = items;
        this.mContext = c;
    }

    public static class QuestViewHolder extends  RecyclerView.ViewHolder{

        private TextView mQuestName;
        private TextView mQuestDescription;
        private CheckBox mCompletedBox;
        private TextView mQuestExpiryDate;

        public QuestViewHolder(final View itemView) {
            super(itemView);
            this.mQuestName = (TextView) itemView.findViewById(R.id.quest_name);
            this.mQuestDescription = (TextView) itemView.findViewById(R.id.quest_description);
            this.mCompletedBox = (CheckBox) itemView.findViewById(R.id.action_finish_quest);
            this.mQuestExpiryDate = (TextView) itemView.findViewById(R.id.quest_log_tv_expiry);

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
        TextView textViewExpiry = holder.mQuestExpiryDate;

        textViewName.setText(items.get(listPosition).getName());
        textViewDescription.setText(items.get(listPosition).getDescription());

        if (items.get(listPosition).getExpiryDate() != null){
            Date current = Calendar.getInstance().getTime();
            Date expiry = items.get(listPosition).getExpiryDate();
            long difference = TimeUnit.DAYS.convert((expiry.getTime() - current.getTime()),
                    TimeUnit.MILLISECONDS);

            if (difference == 0){
                textViewExpiry.setText("Today!");
            }else{
                textViewExpiry.setText(difference + "\nDays");
            }

        }else{

            textViewExpiry.setText("");
        }

        holder.mCompletedBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                DatabaseHelper db = new DatabaseHelper(mContext);
                Quest q = items.get(listPosition);
                q.setHasBeenCompleted(true);
                Log.d("QUESTADAPTER", q.toString());
                db.updateQuest(items.get(listPosition));

                items.remove(listPosition);
                notifyItemRemoved(listPosition);
                notifyItemRangeChanged(listPosition,items.size());
                Toast.makeText(mContext,"Success",Toast.LENGTH_SHORT).show();
                holder.mCompletedBox.setChecked(false);
            }
        });
    }

    @Override
    public int getItemCount(){
        return items.size();
    }
}
