package com.example.healthcare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.Patient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientAdapter  extends  RecyclerView.Adapter<PatientAdapter.PatientHolder>{
    public PatientAdapter(ArrayList<Patient> patients, Context mcontext) {
        this.patients = patients;
        this.mcontext = mcontext;
    }

    private ArrayList<Patient> patients = new ArrayList<>();
    private Context mcontext;
    private String mail_med;
    private static final String TAG = "PatientAdapter";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference stRef;

    public String getMail_med() {
        return mail_med;
    }

    public void setMail_med(String mail_med) {
        this.mail_med = mail_med;
    }

    @NonNull
    @Override
    public PatientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item_view,parent,false);
        PatientHolder holder = new PatientHolder(v);
        return holder;
    }

    private  void  initPhotoProfile(String mail_pat, final CircleImageView photo_profile){
        stRef = storage.getReferenceFromUrl("gs://healthcare-1dab0.appspot.com").child("photos_profile_patient/" + mail_pat +".jpg");
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
    @Override
    public void onBindViewHolder(@NonNull PatientHolder holder, int position) {
        final Patient pat = patients.get(position);
        holder.ville_patient.setText(pat.getAdresse());
        holder.nom_prenom.setText(pat.getNom() + " " + pat.getPrenom());
        initPhotoProfile(pat.getEmail(),holder.image_pat);
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,DetailPatient.class);
                intent.putExtra("patient",pat);
                intent.putExtra("mail_med",mail_med);
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public static  class  PatientHolder extends RecyclerView.ViewHolder{
        CircleImageView image_pat;
        TextView nom_prenom,ville_patient;
        LinearLayout plus;

        public PatientHolder(@NonNull View itemView) {
            super(itemView);
            image_pat = itemView.findViewById(R.id.image_pat);
            nom_prenom = itemView.findViewById(R.id.nom_prenom);
            ville_patient = itemView.findViewById(R.id.ville_pat);
            plus = itemView.findViewById(R.id.plus);
        }
    }
}
