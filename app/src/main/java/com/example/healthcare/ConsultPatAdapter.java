package com.example.healthcare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Consultation;

import java.util.ArrayList;

public class ConsultPatAdapter extends  RecyclerView.Adapter<ConsultPatAdapter.ConsultPatHolder> {
    private ArrayList<Consultation> cons = new ArrayList<>();
    private Context mcontext;

    public ConsultPatAdapter(ArrayList<Consultation> cons, Context mcontext) {
        this.cons = cons;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ConsultPatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cons_pat_item,parent,false);
        ConsultPatHolder holder = new ConsultPatHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultPatHolder holder, int position) {

        Consultation c = cons.get(position);
        holder.issue_consult.setText(c.getIssue());
        holder.mail_med.setText(c.getNomMedecin());
    }

    @Override
    public int getItemCount() {
        return cons.size();
    }

    public  static class ConsultPatHolder extends RecyclerView.ViewHolder{
        TextView date_consult,mail_med,issue_consult;

        public ConsultPatHolder(@NonNull View itemView) {
            super(itemView);
            date_consult = itemView.findViewById(R.id.date_cons);
            mail_med = itemView.findViewById(R.id.mail_med);
            issue_consult = itemView.findViewById(R.id.issue_consult);
        }
    }
}
