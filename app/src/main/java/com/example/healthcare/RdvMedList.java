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
import com.example.model.Medecin;
import com.example.model.RdvMedecin;
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

public class RdvMedList extends AppCompatActivity {

    ArrayList<RdvMedecin> rdvMedecins = new ArrayList<>();
    String mail_med;
    RecyclerView listRdvsMed;
    RdvMedAdapter rdvMedAdapter;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "RdvMedList";
    ImageView home;
    CircleImageView photo_profile;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference stRef;
    TextView deconnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rdv_med_list);

        initListRdv();
        Intent i = getIntent();
        mail_med = i.getStringExtra("mail_med");
        Log.d(TAG,"MAIL_MED_ :" + mail_med);
        listRdvsMed = findViewById(R.id.list_rdv_med);
        listRdvsMed.setHasFixedSize(true);
        rdvMedAdapter = new RdvMedAdapter(rdvMedecins,this);
        layoutManager =  new LinearLayoutManager(this);
        listRdvsMed.setLayoutManager(layoutManager);
        listRdvsMed.setAdapter(rdvMedAdapter);
        rdvMedAdapter.setMail_med(mail_med);

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RdvMedList.this,EspaceMedecinActivity.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);
            }
        });
        photo_profile = findViewById(R.id.photo_profile);
        //initPhotoProfile();
        deconnect = findViewById(R.id.deconnexion);
        deconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(RdvMedList.this, LoginActivity.class));

            }
        });


    }

    private  void  initListRdv(){

        db.collection("medecins").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG,"SingleMedFound" );
                Log.d(TAG,"ISEMPTY + " + queryDocumentSnapshots.isEmpty());

                for (QueryDocumentSnapshot document : queryDocumentSnapshots){
                    Log.d(TAG,"IDD : " +document.getId());
                    Medecin med = document.toObject(Medecin.class);

                    if(med.getEmail().equals(mail_med)){
                        db.collection("medecins/"+document.getId() + "/rdvsMed").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()){
                                    Log.d(TAG,"ListRdvGet");

                                    for (QueryDocumentSnapshot d: task.getResult()){
                                        RdvMedecin rdv = d.toObject(RdvMedecin.class);
                                        rdvMedecins.add(rdv);
                                        rdvMedAdapter.notifyDataSetChanged();
                                    }
                                }
                                else {
                                    Log.d(TAG,"ListRdvNotGet");
                                    if (rdvMedecins.isEmpty()){
                                        Toast.makeText(RdvMedList.this,"Vous n'avez aucun rdv actuellement !", Toast.LENGTH_LONG).show();
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
                Log.d(TAG,"SingleMedNotFound");
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
