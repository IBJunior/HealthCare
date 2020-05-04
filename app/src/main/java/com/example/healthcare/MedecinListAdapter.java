package com.example.healthcare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Medecin;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MedecinListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MedecinListAdapter";
    private ArrayList<Medecin> medecins = new ArrayList<>();
    private Context mcontext;

    public MedecinListAdapter(Context context, ArrayList<Medecin> medecins){
        this.mcontext = context;
        this.medecins = medecins;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_item_view,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        holder = (ViewHolder) holder;
        Log.d(TAG,"OnBindViewHolder called");

        ((ViewHolder) holder).specialite_ville.setText(medecins.get(position).getSpecialite() +"-" + medecins.get(position).getAdresse());
        ((ViewHolder) holder).nom_prenom.setText("Dr " +medecins.get(position).getNom() + " " + medecins.get(position).getPrenom());

        ((ViewHolder) holder).plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext,DetailMedecin.class);
               Log.d(TAG, ""+ medecins.get(position).toString());
                intent.putExtra("Medcin", medecins.get(position));
                mcontext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return medecins.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image_med;
        TextView nom_prenom,specialite_ville,plus;

        public ViewHolder(View itemView){
            super(itemView);

            image_med = itemView.findViewById(R.id.image_med);
            nom_prenom = itemView.findViewById(R.id.nom_prenom);
            specialite_ville = itemView.findViewById(R.id.specialite_ville);
            plus = itemView.findViewById(R.id.plus);
        }
    }
}
