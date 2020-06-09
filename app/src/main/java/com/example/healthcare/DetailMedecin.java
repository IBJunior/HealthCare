package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login.LoginActivity;
import com.example.model.Medecin;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailMedecin extends AppCompatActivity {

    Medecin medecin;
    private static final String TAG = "DetailPatient";
    ImageView home;
    TextView nom_prenom, specialite, adresse, tel, email, deconnect;
    Button rdv;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    StorageReference stRef;
    CircleImageView photo_profile,imageView;
    String mail_pat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_medecin);
        Intent intent = getIntent();
        medecin = intent.getParcelableExtra("medecin");

        mail_pat = intent.getStringExtra("mail_pat");
        home = findViewById(R.id.home);
        photo_profile = findViewById(R.id.photo_profile);
        initPhotoProfile(mail_pat,photo_profile,"photos_profile_patient");
        initPhotoProfile(medecin.getEmail(),imageView,"photos_profile_medecin");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DetailMedecin.this,EspacePatientActivity.class);
                intent1.putExtra("mail_pat",mail_pat);
                startActivity(intent1);
            }
        });
        deconnect = findViewById(R.id.deconnexion);
        deconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(DetailMedecin.this, LoginActivity.class));

            }
        });



        imageView = findViewById(R.id.image_med);
        nom_prenom = findViewById(R.id.nom_prenom);
        tel = findViewById(R.id.tel);
        email =  findViewById(R.id.email);
        adresse = findViewById(R.id.adresse);
        specialite = findViewById(R.id.specialite);

        rdv = findViewById(R.id.rdv);

        rdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailMedecin.this,PriseRdv.class);
                i.putExtra("medecin",medecin);
                i.putExtra("mail_pat",mail_pat);
                startActivity(i);
            }
        });
        
        nom_prenom.setText("Dr "+medecin.getNom() + " " + medecin.getPrenom());
        adresse.setText(medecin.getAdresse());
        specialite.setText(medecin.getSpecialite());
        tel.setText(medecin.getTel());
        email.setText(medecin.getEmail());

    }
    private  void  initPhotoProfile(String mail, final  CircleImageView photo_profile,String path){
        stRef = storage.getReferenceFromUrl("gs://healthcare-1dab0.appspot.com").child(path+"/" + mail +".jpg");
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
