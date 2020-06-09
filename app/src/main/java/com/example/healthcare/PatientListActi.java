package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.LoginActivity;
import com.example.model.Consultation;
import com.example.model.Medecin;
import com.example.model.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientListActi extends AppCompatActivity {
    RecyclerView list_patients;
    PatientAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Patient> patients = new ArrayList<>();
    String mail_med;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "PatientListActi";
    TextView deconnect;
    ImageView home;
    CircleImageView photo_profile;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference stRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        Intent i = getIntent();
        mail_med = i.getStringExtra("mail_med");
        list_patients = findViewById(R.id.list_patients);
        list_patients.setHasFixedSize(true);
        adapter = new PatientAdapter(patients,this);
        adapter.setMail_med(mail_med);
        layoutManager = new LinearLayoutManager(this);
        list_patients.setAdapter(adapter);
        list_patients.setLayoutManager(layoutManager);
        initListPatients();
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PatientListActi.this,EspaceMedecinActivity.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);
            }
        });
        deconnect = findViewById(R.id.deconnexion);
        deconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(PatientListActi.this, LoginActivity.class));

            }
        });
        photo_profile = findViewById(R.id.photo_profile);
        initPhotoProfile();

    }

    private void recupPatienst(String path){
        Log.d(TAG,"RECUPPAT called");
       final ArrayList<Consultation> cons = new ArrayList<>();
        db.document(path).collection("consultationsMed").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG,"SUCC_CONS");
                Log.d(TAG,"CONSEMPTY " + queryDocumentSnapshots.isEmpty());

                for (QueryDocumentSnapshot d:queryDocumentSnapshots){
                    Consultation cs = d.toObject(Consultation.class);
                    cons.add(cs);


                }
                Log.d(TAG,"consEMpty :" + cons.isEmpty());
                for (final Consultation c: cons){
                    Log.d(TAG,"MAILPAT :" + c.getNomPatient());
                    db.collection("patients").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            if(task.isSuccessful()){
                                boolean firts_pat = true;
                                for (QueryDocumentSnapshot d: task.getResult()){
                                    Patient pat =  d.toObject(Patient.class);



                                    if(pat.getEmail().equals(c.getNomPatient())){

                                        if(firts_pat){
                                            patients.add(pat);
                                            adapter.notifyDataSetChanged();
                                            firts_pat = false;
                                        }
                                        else {

                                            for (Patient p : patients){
                                                if(!p.getEmail().equals(pat.getEmail())){
                                                    patients.add(pat);
                                                    adapter.notifyDataSetChanged();
                                                }
                                            }


                                        }


                                    }
                                }
                            }
                            else {
                                if (patients.isEmpty()){
                                    Toast.makeText(PatientListActi.this,"Vous n'avez consulté aucun patient pour l'instant", Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"FAILED_CONS");
            }
        });



    }

    private void initListPatients(){

        db.collection("medecins").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){
                    Log.d(TAG,"MAIL_MED : " + mail_med);
                    Log.d(TAG,"PAS_PB_LIST_MED");
                    Log.d(TAG,"ISEMPTY_: " + task.getResult().isEmpty());

                    for (QueryDocumentSnapshot d: task.getResult()){
                        Medecin med = d.toObject(Medecin.class);

                        if(med.getEmail().equals(mail_med)){
                            Log.d(TAG,"IF_STAT");
                            recupPatienst(d.getReference().getPath());
                            adapter.notifyDataSetChanged();
                        }
                    }
                }else {
                    Log.d(TAG,"PB_LIST_MED");
                }

            }
        });

    }
    private  void  initPhotoProfile(){
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
}
