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

public class EspacePatientActivity extends AppCompatActivity {

    ImageView search_med,consult_pat,mes_rdv,nv_rdc;
    String mail_pat;
    TextView deconnect;
    CircleImageView photo_profile;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference stRef;
    private static final String TAG = "EspacePatientActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_patient);

        Intent intent = getIntent();
        mail_pat = intent.getStringExtra("mail_pat");
        initPhotoProfile();
        photo_profile = findViewById(R.id.photo_profile);
        mes_rdv = findViewById(R.id.mes_rdv);
        nv_rdc = findViewById(R.id.nv_rdc);
        nv_rdc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(EspacePatientActivity.this,ListMedecin.class);
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
                startActivity(new Intent(EspacePatientActivity.this, LoginActivity.class));

            }
        });
        mes_rdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EspacePatientActivity.this,RdvPatientList.class);
                i.putExtra("mail_pat",mail_pat);
                startActivity(i);
            }
        });
        consult_pat = findViewById(R.id.consult_patient);
        consult_pat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EspacePatientActivity.this, ConsultationsPatientAct.class);
                i.putExtra("mail_pat",mail_pat);
                startActivity(i);
            }
        });
        search_med = (ImageView) findViewById(R.id.search_med);
        search_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(EspacePatientActivity.this,ListMedecin.class);
                intent1.putExtra("mail_pat",mail_pat);
                startActivity(intent1);
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
