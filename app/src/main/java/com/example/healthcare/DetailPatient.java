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
import com.example.model.Patient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailPatient extends AppCompatActivity {

    Patient patient;
    CircleImageView image_pat;
    ImageView home;
    TextView nom_prenom,ville_pat,email,tel,situation,deconnect;
    String mail_med;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference stRef;
    CircleImageView photo_profile;
    private static final String TAG = "DetailPatient";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_patient);
         Intent i = getIntent();

        patient = i.getParcelableExtra("patient");
        mail_med = i.getStringExtra("mail_med");



        nom_prenom = findViewById(R.id.nom_prenom);
        ville_pat = findViewById(R.id.ville_pat);
        email = findViewById(R.id.email);
        tel = findViewById(R.id.tel);
        situation = findViewById(R.id.situation);
        image_pat = findViewById(R.id.image_pat);


        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailPatient.this,EspaceMedecinActivity.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);
            }
        });
        photo_profile= findViewById(R.id.photo_profile);
        initPhotoProfile(mail_med,photo_profile,"photos_profile_medecin");
        initPhotoProfile(patient.getEmail(),image_pat,"photos_profile_patient");

        deconnect = findViewById(R.id.deconnexion);
        deconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(DetailPatient.this, LoginActivity.class));

            }
        });


        nom_prenom.setText(patient.getNom() + " " + patient.getPrenom());
        ville_pat.setText(patient.getAdresse());
        email.setText(patient.getEmail());
        tel.setText(patient.getTel());
        situation.setText(patient.getSituation_familiale());
    }
    private  void  initPhotoProfile(String mail, final  CircleImageView photo_profile,String path){
        stRef = storage.getReferenceFromUrl("gs://healthcare-1dab0.appspot.com").child(path +"/" + mail +".jpg");
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
