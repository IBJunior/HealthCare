package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.login.LoginActivity;
import com.example.model.Disponibilite;
import com.example.model.Medecin;
import com.example.model.Patient;
import com.example.model.RdvMedecin;
import com.example.model.RdvPatient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PriseRdv extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    Medecin medecin;
    String mail_pat;
    TextView prdv,date,reponse,deconnect;
    Button checkDispo,confirme;
    Calendar c = Calendar.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "PriseRdv";
    Spinner choix_heure_rdv;
    String path;
    ImageView home;
    LinearLayout date_ll;
    String ref_doc_update;
    TextView text_prdv;
    boolean confirme_rdv = false;
    Disponibilite dispo = new Disponibilite();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prise_rdv);

        Intent intent = getIntent();
        medecin = intent.getParcelableExtra("medecin");

        home = findViewById(R.id.home);
        Intent i = getIntent();
        date_ll = findViewById(R.id.date_llayout);
        mail_pat = i.getStringExtra("mail_pat");
        text_prdv = findViewById(R.id.text_prdv);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PriseRdv.this,EspacePatientActivity.class);
                intent.putExtra("mail_pat",mail_pat);
                startActivity(intent);
            }
        });
        deconnect = findViewById(R.id.deconnexion);
        deconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(PriseRdv.this, LoginActivity.class));

            }
        });


        prdv = findViewById(R.id.prdv);
        date = findViewById(R.id.date);
        //heure = findViewById(R.id.heure);
        reponse = findViewById(R.id.reponse);
        checkDispo = findViewById(R.id.checkDispo);
        confirme = findViewById(R.id.confirme);

        prdv.setText("Dr  " + medecin.getNom() +" " + medecin.getPrenom());
         choix_heure_rdv = findViewById(R.id.choix_heure_rdv);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment choixDate = new ChoixDateFragment();
                choixDate.show(getSupportFragmentManager(),"Choix date");
                if(reponse.getVisibility()== View.VISIBLE){
                    reponse.setVisibility(View.GONE);
                }
                if(choix_heure_rdv.getVisibility() == View.VISIBLE){
                    choix_heure_rdv.setVisibility(View.GONE);
                }
                if(checkDispo.getVisibility() == View.GONE){
                    checkDispo.setVisibility(View.VISIBLE);
                }
                if(confirme.getVisibility()== View.VISIBLE){
                    confirme.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String dateChoisie = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        date.setText(dateChoisie);

    }


    private void initSpinerChoix(ArrayList<String> heures){

        // Défibition des éléments du spiner de choix de date
        choix_heure_rdv = (Spinner) findViewById(R.id.choix_heure_rdv);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, heures);
       choix_heure_rdv.setAdapter(arrayAdapter);
       choix_heure_rdv.setVisibility(View.VISIBLE);
       checkDispo.setVisibility(View.GONE);
       confirme.setVisibility(View.VISIBLE);
    }


    private  ArrayList<String>  getDispoHeures(Disponibilite dspo){
        ArrayList<String> heures = new ArrayList<>();
        String h_tmp = "00h00-00h00";

        if(!dspo.getHeure1().equals(h_tmp)){
            heures.add(dspo.getHeure1());
        }
        if(!dspo.getHeure2().equals(h_tmp)){
            heures.add(dspo.getHeure2());
        }

        return heures;

    }
    public void disponibilite(View view) {


        final String dateChoisi = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());



        db.collection("medecins")
                .whereEqualTo("email", medecin.getEmail())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG,"MedMail found");

                for (QueryDocumentSnapshot document : queryDocumentSnapshots){

                    Log.d(TAG,"PATH : "+ document.getReference().getPath());
                    path = document.getReference().getPath();
                    db.document(document.getReference().getPath()).collection("disponibilite").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            boolean dispo_found = false;

                            Log.d(TAG,"Dispo en mains");
                            for (QueryDocumentSnapshot d: queryDocumentSnapshots){
                                Disponibilite dsp = d.toObject(Disponibilite.class);

                                if (dsp.getJour().equals(dateChoisi)){

                                    if(!(dsp.getHeure1().equals("00h00-00h00") && dsp.getHeure2().equals("00h00-00h00")))
                                    {
                                        dispo_found = true;
                                        dispo = dsp;
                                        ref_doc_update = d.getId();
                                    }
                                }
                            }

                            if (dispo_found){
                                // task
                                Log.d(TAG,"DayFound");
                                initSpinerChoix(getDispoHeures(dispo));

                            }
                            else {
                                Log.d(TAG,"DayNotFound");
                                Toast.makeText(PriseRdv.this,"Veuillez choisir un autre jour !",Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG,"DispoNotMains");

                        }
                    });
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"MedMail Not found");
            }
        });






    }
    private  void  saveRdvMed(String path, RdvMedecin rdvMedecin){

        db.collection(path + "/rdvsMed").document().set(rdvMedecin).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,"Rdvs_Med saved");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Rdvs_Med Not saved");

            }
        });

    }

    private  void saveRdvpat(final RdvPatient rdvPatient){
        db.collection("patients").whereEqualTo("email",mail_pat).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot d: queryDocumentSnapshots){
                    Log.d(TAG,"PATHPAT: " + d.getId());


                    db.document("patients/" + d.getId()).collection("rdvsPat").document().set(rdvPatient).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG,"RDV_PAT_SAVED");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG,"RDV_PAT_NOT_SAVED");
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"PASSAVEDPAT");
            }
        });

    }

    public void confirmer_rdv(View view) {
       if (!confirme_rdv){
           Toast.makeText(this,"Confirmed",Toast.LENGTH_LONG).show();
           Log.d(TAG,"ID: " + ref_doc_update);
           DocumentReference documentReference = db.document(path).collection("disponibilite").document(ref_doc_update);
           if(choix_heure_rdv.getSelectedItem().equals(dispo.getHeure1())){
               final String heure = dispo.getHeure1();
               final String jour = dispo.getJour();
               documentReference.update(
                       "heure1","00h00-00h00",
                       "heure2", dispo.getHeure2()
               ).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           text_prdv.setVisibility(View.GONE);
                           prdv.setVisibility(View.GONE);
                           reponse.setText("Rendez-vous confirmé  pour " + date.getText() + " à " + dispo.getHeure1() + " avec " + prdv.getText() + ".");
                           reponse.setVisibility(View.VISIBLE);
                           confirme.setVisibility(View.GONE);
                           choix_heure_rdv.setVisibility(View.GONE);
                           date_ll.setVisibility(View.GONE);
                           RdvMedecin  rdvMedecin = new RdvMedecin();
                           RdvPatient rdvPatient = new RdvPatient();

                           rdvMedecin.setHeure(heure);
                           rdvMedecin.setDate(jour);
                           rdvMedecin.getPatient().setEmail(mail_pat);
                           saveRdvMed(path,rdvMedecin);

                           rdvPatient.setDate(jour);
                           rdvPatient.setHeure(heure);
                           rdvPatient.setMedecin(medecin);
                           saveRdvpat(rdvPatient);
                           confirme_rdv =true;


                           Log.d(TAG,"Updated Successfully");

                       }
                       else {
                           Log.d(TAG,"Update failed");
                       }
                   }
               });
           }
           if(choix_heure_rdv.getSelectedItem().equals(dispo.getHeure2())){
               final String heure = dispo.getHeure2();
               final String jour = dispo.getJour();
               documentReference.update(
                       "heure1",dispo.getHeure1(),
                       "heure2","00h00-00h00"

               ).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Log.d(TAG,"Updated Successfully");
                           text_prdv.setVisibility(View.GONE);
                           prdv.setVisibility(View.GONE);
                           reponse.setText("Rendez-vous confirmé  pour " + date.getText() + " à " + dispo.getHeure2() + " avec " + prdv.getText() + ".");
                           reponse.setVisibility(View.VISIBLE);
                           confirme.setVisibility(View.GONE);
                           choix_heure_rdv.setVisibility(View.GONE);
                           date_ll.setVisibility(View.GONE);
                           RdvMedecin  rdvMedecin = new RdvMedecin();
                           RdvPatient rdvPatient = new RdvPatient();

                           rdvMedecin.setHeure(heure);
                           rdvMedecin.setDate(jour);
                           rdvMedecin.getPatient().setEmail(mail_pat);
                           saveRdvMed(path,rdvMedecin);

                           rdvPatient.setDate(jour);
                           rdvPatient.setHeure(heure);
                           rdvPatient.setMedecin(medecin);
                           saveRdvpat(rdvPatient);
                           confirme_rdv =true;
                           reponse.setVisibility(View.VISIBLE);
                       }
                       else {
                           Log.d(TAG,"Update failed");
                       }
                   }
               });
           }


       }
    }
}
