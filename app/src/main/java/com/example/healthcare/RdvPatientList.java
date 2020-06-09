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
import com.example.model.Patient;
import com.example.model.RdvPatient;
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

public class RdvPatientList extends AppCompatActivity {
    RecyclerView list_pat_rdv;
    RdvPatientAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String mail_pat;
    TextView deconnect;
    CircleImageView photo_profile;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference stRef;
    ImageView home;
    private ArrayList<RdvPatient> rdvPatients = new ArrayList<>();
    private static final String TAG = "RdvPatientList";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdv_patient_list);

        Intent i = getIntent();

        mail_pat = i.getStringExtra("mail_pat");
        initRdvsPatient();
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RdvPatientList.this,EspacePatientActivity.class);
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
                startActivity(new Intent(RdvPatientList.this, LoginActivity.class));

            }
        });

        list_pat_rdv = findViewById(R.id.list_rdv_pat);
        list_pat_rdv.setHasFixedSize(true);
        adapter = new RdvPatientAdapter(rdvPatients,this);
       layoutManager = new LinearLayoutManager(this);
       list_pat_rdv.setLayoutManager(layoutManager);
       list_pat_rdv.setAdapter(adapter);
    }

    private  void  initRdvsPatient(){

        db.collection("patients").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (QueryDocumentSnapshot d: queryDocumentSnapshots){
                    Patient pat = d.toObject(Patient.class);

                    if(pat.getEmail().equals(mail_pat)){
                        db.collection("patients/"+d.getId() + "/rdvsPat").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot d: task.getResult()){
                                        RdvPatient rdv = d.toObject(RdvPatient.class);
                                        rdvPatients.add(rdv);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                                else {
                                    if (rdvPatients.isEmpty()){
                                        Toast.makeText(RdvPatientList.this,"Vous n'avez aucun rdv !", Toast.LENGTH_LONG).show();
                                    }

                                }

                            }
                        });
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


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
