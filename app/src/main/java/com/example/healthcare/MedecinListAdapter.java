package com.example.healthcare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Medecin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import de.hdodenhof.circleimageview.CircleImageView;

public class MedecinListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MedecinListAdapter";
    private ArrayList<Medecin> medecins = new ArrayList<>();
    private Context mcontext;
    private  String nom_pat,mail_pat;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference stRef;

    public String getNom_pat() {
        return nom_pat;
    }

    public void setNom_pat(String nom_pat) {
        this.nom_pat = nom_pat;
    }

    public String getMail_pat() {
        return mail_pat;
    }

    public void setMail_pat(String mail_pat) {
        this.mail_pat = mail_pat;
    }

    public MedecinListAdapter(Context context, ArrayList<Medecin> medecins){
        this.mcontext = context;
        this.medecins = medecins;
        this.mail_pat ="";
        this.nom_pat = "";

    }

    private  void  initPhotoProfile(String mail_med,final CircleImageView photo_profile){
        stRef = storage.getReferenceFromUrl("gs://healthcare-1dab0.appspot.com").child("photos_profile_medecin/" + mail_med +".jpg");
        Log.d(TAG,"IMAGE_REF : " + stRef.toString());
        try {

            final File localeFile = File.createTempFile("images","jpg");
            stRef.getFile(localeFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localeFile.getAbsolutePath());
                    photo_profile.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"IMAGE_PP_FAILED");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_item_view,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    public Uri getMedUri(String nom_profile){
        return  null;
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        holder = (ViewHolder) holder;
        Medecin med = medecins.get(position);
        Log.d(TAG,"OnBindViewHolder called");
        CircleImageView view= ((ViewHolder) holder).image_med;

        ((ViewHolder) holder).image_med.setImageURI(getMedUri(medecins.get(position).getEmail()));
        initPhotoProfile(med.getEmail(),view);
        ((ViewHolder) holder).specialite_ville.setText(medecins.get(position).getSpecialite() +"-" + medecins.get(position).getAdresse());
        ((ViewHolder) holder).nom_prenom.setText("Dr "+medecins.get(position).getNom() + " " + medecins.get(position).getPrenom());
        ((ViewHolder) holder).plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext, DetailMedecin.class);
                Log.d(TAG, ""+ medecins.get(position).toString());
                intent.putExtra("medecin", medecins.get(position));
                intent.putExtra("mail_pat",mail_pat);
                mcontext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return medecins.size();
    }

    public void filterList(ArrayList<Medecin> medecins_filter) {

        medecins = medecins_filter;
        notifyDataSetChanged();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image_med;
        TextView nom_prenom, specialite_ville;
        LinearLayout plus;

        public ViewHolder(View itemView){
            super(itemView);

            image_med = itemView.findViewById(R.id.image_med);
            nom_prenom = itemView.findViewById(R.id.nom_prenom);
            specialite_ville = itemView.findViewById(R.id.specialite_ville);
            plus = itemView.findViewById(R.id.plus);
        }
    }
}
