package com.example.healthcare;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Disponibilite;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DispoAdapter extends RecyclerView.Adapter<DispoAdapter.DipsoViewHolder> {
    private static final String TAG = "DispoAdapter";

    private ArrayList<Disponibilite> dispos = new ArrayList<>();
    private Context mcontext;
    private Dialog dialog;
    private  ArrayList<Disponibilite> dispos_fire_base = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  String path;
    String del_ref_doc;
    ArrayList<String> ref_dispo_list = new ArrayList<>();
    TextView jour,plage1,plage2,annul_suppr,conf_suppr,jour_modif,annul_modif,conf_modif;
    EditText plage1_edit,plage2_edit;


    public static class DipsoViewHolder extends RecyclerView.ViewHolder{
        public TextView jour_dispo,heure_dispo;
        public Button modifier,supprimer;

        public DipsoViewHolder(@NonNull View itemView) {
            super(itemView);
            jour_dispo = itemView.findViewById(R.id.jour_dispo);
            heure_dispo = itemView.findViewById(R.id.heure_dispo);
            modifier =  itemView.findViewById(R.id.modifier);
            supprimer = itemView.findViewById(R.id.supprimer);
        }
    }

    public DispoAdapter(ArrayList<Disponibilite> dispos){

        this.dispos = dispos;
    }

    public ArrayList<String> getRef_dispo_list() {
        return ref_dispo_list;
    }

    public void setRef_dispo_list(ArrayList<String> ref_dispo_list) {
        this.ref_dispo_list = ref_dispo_list;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Context getMcontext() {
        return mcontext;
    }

    public void setMcontext(Context mcontext) {
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public DipsoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dialog = new Dialog(mcontext);
        Log.d(TAG,"OnCreate Call");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dispo_item_view,parent,false);
        DipsoViewHolder dspVHolder = new  DispoAdapter.DipsoViewHolder(v);
        return dspVHolder;
    }


    private void del_dispo(final Disponibilite dispo){
        Log.d(TAG,"del_dispo_called");
        db.collection(path).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG,"get list of dispos_del");

                for (QueryDocumentSnapshot d: queryDocumentSnapshots){
                    Disponibilite disp = d.toObject(Disponibilite.class);


                    if (dispo.getJour().equals(disp.getJour())){
                        del_ref_doc = d.getReference().getId();
                        db.collection(path).document(del_ref_doc).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG,"DELETED !");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG,"NOTDELETED !");
                                notifyDataSetChanged();
                            }
                        });

                    }

                }
                Log.d(TAG,"REFF : " + del_ref_doc);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }
    private  void update_dispo(Disponibilite dispo){
        DocumentReference reference =  db.collection(path).document(del_ref_doc);

       reference.update(
               "jour",dispo.getJour(),
               "heure1", dispo.getHeure1(),
               "heure2", dispo.getHeure2()
       ).addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
               Log.d(TAG,"Updated_dispo");
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Log.d(TAG,"failed_updating_dispo");
           }
       });
    }
    @Override
    public void onBindViewHolder(@NonNull DipsoViewHolder holder, int position) {
        final Disponibilite dispo =  dispos.get(position);



        holder.heure_dispo.setText(dispo.getHeure1() + "\n" + dispo.getHeure2());
        holder.jour_dispo.setText(dispo.getJour());
        holder.supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setContentView(R.layout.suppr_dispo_dialog);
                jour = (TextView) dialog.findViewById(R.id.jour);
                String jour_str = jour.getText().toString();
                jour.setText(jour_str + " : " + dispo.getJour());
                plage1 = dialog.findViewById(R.id.plage_1);
                String plg1_str = plage1.getText().toString();
                plage1.setText(plg1_str + " : " + dispo.getHeure1());
                plage2 = dialog.findViewById(R.id.plage_2);
                String plg2_str = plage2.getText().toString();
                plage2.setText(plg1_str + " : " + dispo.getHeure1());
                annul_suppr = dialog.findViewById(R.id.annul_suppr);
                conf_suppr = dialog.findViewById(R.id.conf_suppr);
                annul_suppr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                conf_suppr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(dialog.getContext(),"Dispo suppr", Toast.LENGTH_LONG).show();
                        Log.d(TAG,"REF : " +path);
                        del_dispo(dispo);
                    }
                });
                dialog.show();
                Log.d(TAG,"Suppr called");

            }
        });
        holder.modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.setContentView(R.layout.modif_dispo_dialog);
                plage1_edit = dialog.findViewById(R.id.plage_1_edit);
                plage1_edit.setText(dispo.getHeure1());
                plage2_edit = dialog.findViewById(R.id.plage_2_edit);
                plage2_edit.setText(dispo.getHeure2());
                jour_modif = dialog.findViewById(R.id.jour_modif);

                annul_modif = dialog.findViewById(R.id.annul_modif);
                conf_modif = dialog.findViewById(R.id.conf_modif);
                annul_modif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                conf_modif.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(dialog.getContext(),"Mofié", Toast.LENGTH_LONG).show();
                        Disponibilite dsp = new Disponibilite();
                        dsp.setJour(jour_modif.getText().toString());
                        dsp.setHeure1(plage1_edit.getText().toString());
                        dsp.setHeure2(plage2_edit.getText().toString());
                        update_dispo(dsp);
                    }
                });
                dialog.show();
                Log.d(TAG,"modifer called");
            }
        });

    }

    @Override
    public int getItemCount() {
        return dispos.size();
    }
}
