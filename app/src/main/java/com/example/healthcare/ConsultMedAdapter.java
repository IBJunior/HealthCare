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

public class ConsultMedAdapter extends  RecyclerView.Adapter<ConsultMedAdapter.ConsultMedHolder> {

    private ArrayList<Consultation> cons = new ArrayList<>();
    private Context mcontext;

    public ConsultMedAdapter(ArrayList<Consultation> cons, Context mcontext) {
        this.cons = cons;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public ConsultMedHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cons_med_item,parent,false);
        ConsultMedHolder holder =  new ConsultMedHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultMedHolder holder, int position) {

        Consultation c = cons.get(position);

        holder.issue_consult.setText(c.getIssue());
        holder.mail_pat.setText(c.getNomMedecin());

    }

    @Override
    public int getItemCount() {
        return cons.size();
    }

    public  static  class  ConsultMedHolder extends RecyclerView.ViewHolder{

        TextView mail_pat,date_consult,issue_consult;
        public ConsultMedHolder(@NonNull View itemView) {
            super(itemView);
            mail_pat = itemView.findViewById(R.id.mail_pat);
            date_consult = itemView.findViewById(R.id.date_cons);
            issue_consult = itemView.findViewById(R.id.issue_consult);
        }
    }
}
