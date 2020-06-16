package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ConsultationsPatientAct extends AppCompatActivity {

    ImageView home;
    String mail_pat;
    ConsultPatAdapter adapter;
    RecyclerView list_pat;
    CircleImageView photo_profile;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference stRef;
    ArrayList<Consultation> cons = new ArrayList<>();
    TextView deconnect;
    RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "ConsultationsPatientAct";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultations_patient);
        home = findViewById(R.id.home);
        Intent i = getIntent();
        mail_pat = i.getStringExtra("mail_pat");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConsultationsPatientAct.this,EspacePatientActivity.class);
                intent.putExtra("mail_pat",mail_pat);
                startActivity(intent);
            }
        });
        photo_profile = findViewById(R.id.photo_profile);
        initPhotoProfile();
        deconnect = findViewById(R.id.deconnexion);
        deconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(ConsultationsPatientAct.this, LoginActivity.class));

            }
        });
        initConsList();

        list_pat = findViewById(R.id.pat_consultations);
        list_pat.setHasFixedSize(true);
        adapter = new ConsultPatAdapter(cons,this);
        layoutManager  = new LinearLayoutManager(this);
        list_pat.setAdapter(adapter);
        list_pat.setLayoutManager(layoutManager);

    }

    private  void initConsList(){
        db.collection("patients").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(TAG,"LisT_Med");
                    for (QueryDocumentSnapshot d: task.getResult()){
                        Patient pat = d.toObject(Patient.class);

                        int n = 0;
                        if(pat.getEmail().equals(mail_pat) && n==0)
                        {


                                Log.d(TAG,"LisT_Med : " + mail_pat);
                                db.collection("patients/"+d.getId() + "/consultationsPat").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        Log.d(TAG,"CAN_LIST_CONS");
                                        int n2 = 0;

                                        if(n2==0)
                                        for (QueryDocumentSnapshot d: queryDocumentSnapshots){
                                            Consultation c = d.toObject(Consultation.class);
                                            cons.add(c);
                                            n2++;
                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG,"CANT_LIST_CONS : " + e.getMessage());
                                    }
                                });
                            n++;

                        }
                    }
                }
                else {
                    Log.d(TAG,"PB_LIST_MED");
                }
            }
        });

        if (cons.isEmpty()){
            Toast.makeText(ConsultationsPatientAct.this, "Vous n'avez aucune consultation ",Toast.LENGTH_LONG).show();
        }
    }
    private  void  initPhotoProfile(){
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
}
