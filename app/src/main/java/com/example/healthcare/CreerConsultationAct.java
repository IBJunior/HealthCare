package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.model.Consultation;
import com.example.model.Medecin;
import com.example.model.RdvMedecin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CreerConsultationAct extends AppCompatActivity {

    RdvMedecin rdv;
    String mail_med;
    EditText issue_consult;
    Button valide_consult;
    boolean click_cons =false;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "CreerConsultationAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_consultation);

        Intent intent = getIntent();

        rdv = intent.getParcelableExtra("rdv");
        mail_med = intent.getStringExtra("mail_med");
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
                                      if(task.isSuccessful())
                                          Log.d(TAG,"CONS_MED_SAVED");
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
}
