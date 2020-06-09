package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.LoginActivity;
import com.example.model.Consultation;
import com.example.model.Medecin;
import com.example.model.RdvMedecin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreerConsultationAct extends AppCompatActivity {

    RdvMedecin rdv;
    String mail_med;
    EditText issue_consult;
    Button valide_consult;
    boolean click_cons =false;
    ImageView home;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "CreerConsultationAct";
    TextView deconnect;
    FirebaseStorage storage= FirebaseStorage.getInstance();
    StorageReference stRef;
    CircleImageView photo_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_consultation);
        Intent intent = getIntent();

        rdv = intent.getParcelableExtra("rdv");
        mail_med = intent.getStringExtra("mail_med");

        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreerConsultationAct.this,EspaceMedecinActivity.class);
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
                startActivity(new Intent(CreerConsultationAct.this, LoginActivity.class));

            }
        });
        photo_profile = findViewById(R.id.photo_profile);
        initPhotoProfile();

        issue_consult = findViewById(R.id.issue_consult);
        valide_consult = findViewById(R.id.valide_consult);

        valide_consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ajouterConsultations();
            }
        });
    }

    private  void  ajouterConsultations(){

      if(!click_cons){
          click_cons = true;
          final Consultation cons = new Consultation();

          if(! issue_consult.getText().toString().isEmpty()){

              cons.setIssue(issue_consult.getText().toString());
              cons.setNomMedecin(mail_med);
              cons.setNomPatient(rdv.getPatient().getEmail());
              cons.setDate(rdv.getDate());

              db.collection("medecins").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                  @Override
                  public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                      Log.d(TAG,"LIST8MED_GETTED");
                      for (QueryDocumentSnapshot d : queryDocumentSnapshots){
                          Medecin med = d.toObject(Medecin.class);

                          if(med.getEmail().equals(mail_med)){
                              db.collection("medecins/" + d.getId() + "/consultationsMed").add(cons).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                  @Override
                                  public void onComplete(@NonNull Task<DocumentReference> task) {
                                      if(task.isSuccessful()){
                                          Log.d(TAG,"CONS_MED_SAVED");
                                          Toast.makeText(CreerConsultationAct.this,"Consultation effectuée avec succès !", Toast.LENGTH_LONG).show();
                                          Intent intent = new Intent(CreerConsultationAct.this,GestionConsultations.class);
                                          intent.putExtra("mail_med",mail_med);
                                          startActivity(intent);
                                      }

                                      else
                                          Log.d(TAG,"CONS_MED_NOT_SAVED");
                                  }
                              });
                          }
                      }
                  }
              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Log.d(TAG,"!LIST8MED_GETTED");
                  }
              });
              db.collection("patients").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                  @Override
                  public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                      Log.d(TAG,"LIST8PAT_GETTED");
                      for (QueryDocumentSnapshot d : queryDocumentSnapshots){
                          Medecin med = d.toObject(Medecin.class);

                          if(med.getEmail().equals(rdv.getPatient().getEmail())){
                              db.collection("patients/" + d.getId() + "/consultationsPat").add(cons).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                  @Override
                                  public void onComplete(@NonNull Task<DocumentReference> task) {
                                      if(task.isSuccessful())
                                          Log.d(TAG,"CONS_PAT_SAVED");
                                      else
                                          Log.d(TAG,"CONS_PAT_NOT_SAVED");
                                  }
                              });
                          }
                      }
                  }
              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Log.d(TAG,"!LIST8PAT_GETTED");
                  }
              });


          }
          else {
              issue_consult.requestFocus();
              Toast.makeText(CreerConsultationAct.this,"Ecrivez une consultation ", Toast.LENGTH_LONG).show();
          }

      }
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
