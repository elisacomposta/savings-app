package com.example.savings;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    final static String RECORD_ID_KEY = "recordId";
    private List<Record> recordList = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.amountTxt.setText(MainActivity.formatCurrencyNumber(recordList.get(position).getAmount() / 100.0));
        holder.subjectTxt.setText(recordList.get(position).getSubject());
        holder.dateTxt.setText(recordList.get(position).getDate());
        final int pos = position;
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!MainActivity.creating) {
                    /*DataBaseHelper dbH = new DataBaseHelper(mContext);
                    dbH.deleteOne(recordList.get(pos));
                    notifyDataSetChanged();
                    if (mContext instanceof MainActivity) {
                        ((MainActivity) mContext).showAllRecords();
                    }*/
                    Intent intent = new Intent(mContext, RecordActivity.class);
                    intent.putExtra(RECORD_ID_KEY, recordList.get(pos).getId());
                    mContext.startActivity(intent);
                }
            }
        });

        //check color
        if (recordList.get(position).getEarned()) {
            holder.parent.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGreen));
        }
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView parent;
        TextView amountTxt;
        TextView subjectTxt;
        TextView dateTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.cardParent);
            amountTxt = itemView.findViewById(R.id.amountLine);
            subjectTxt = itemView.findViewById(R.id.subjectLine);
            dateTxt = itemView.findViewById(R.id.dateLine);
        }
    }

}
