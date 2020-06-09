package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class EspaceMedecinActivity extends AppCompatActivity {

    String mail_med;
    ImageView dispo,consults,mes_rdv,patients;
    private static final String TAG = "EspaceMedecinActivity";
    TextView  deconnect;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference stRef;
    CircleImageView photo_profile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_medecin);
        consults = findViewById(R.id.consults);

        Intent i = getIntent();
        patients = findViewById(R.id.patients);
        patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EspaceMedecinActivity.this,PatientListActi.class);
                intent.putExtra("mail_med",mail_med);
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
                startActivity(new Intent(EspaceMedecinActivity.this, LoginActivity.class));

            }
        });

        mail_med = i.getStringExtra("mail_med");
        Log.d(TAG,"MAIL8MED :" + mail_med);
        dispo = findViewById(R.id.dispo);

        mes_rdv = findViewById(R.id.mes_rdv);
        mes_rdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EspaceMedecinActivity.this,RdvMedList.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);
            }
        });
        dispo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EspaceMedecinActivity.this,GestionDispoActivity.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);
            }
        });
        consults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EspaceMedecinActivity.this,GestionConsultations.class);
                intent.putExtra("mail_med", mail_med);
                startActivity(intent);
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
