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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.model.Disponibilite;
import com.example.model.Medecin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PriseRdv extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    Medecin medecin;
    TextView prdv,date,heure,reponse;
    Button checkDispo,confirme;
    Calendar c = Calendar.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "PriseRdv";
    Spinner choix_heure_rdv;
    String path;
    String jour_dispo;
    ArrayList<Disponibilite> dispo_tmp = new ArrayList<>();
    ArrayList<String> nom_dispo = new ArrayList<>();
    int index_nom_choisi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prise_rdv);

        Intent intent = getIntent();
        medecin = intent.getParcelableExtra("medecin");

        prdv = findViewById(R.id.prdv);
        date = findViewById(R.id.date);
        //heure = findViewById(R.id.heure);
        reponse = findViewById(R.id.reponse);
        checkDispo = findViewById(R.id.checkDispo);
        confirme = findViewById(R.id.confirme);

        prdv.setText("Rendez-vous avec  Dr " + medecin.getNom() +" " + medecin.getPrenom());
         choix_heure_rdv = findViewById(R.id.choix_heure_rdv);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment choixDate = new ChoixDateFragment();
                if(reponse.getVisibility()== View.VISIBLE){
                    reponse.setVisibility(View.GONE);
                }
                choixDate.show(getSupportFragmentManager(),"Choix date");
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
       /*
        heure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment choixHuere = new ChoixHeureFragment();
                choixHuere.show(getSupportFragmentManager(),"Choix heure");
            }
        });
        */
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String dateChoisie = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        date.setText(dateChoisie);

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        /*
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        heure.setText(hourOfDay + "h" + minute +"min");
         */


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
    public void disponibilite(View view) {


        String dateChoisi = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        jour_dispo =  dateChoisi.replace(" ","_");
        final ArrayList<Medecin> tmp_list = new ArrayList<>();

        db.collection("medecins")
                .whereEqualTo("email", medecin.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String st ="";
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Medecin medeci = document.toObject(Medecin.class);
                                st = document.getReference().toString();
                                tmp_list.add(medeci);
                                path = document.getReference().getPath();
                                db.document(document.getReference().getPath()).collection("disponibilite").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){

                                            for (QueryDocumentSnapshot dispo : task.getResult()){
                                                Disponibilite disp = dispo.toObject(Disponibilite.class);
                                                dispo_tmp.add(disp);
                                                nom_dispo.add(dispo.getId());
                                            }
                                            Log.d(TAG,"Disponibilité success  ");

                                        }
                                        else {
                                            Log.d(TAG,"Disponibilité failed ");
                                        }
                                    }
                                });

                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


        String h_tmp = "00h00-00h00";
        for (String st: nom_dispo){


            if(st.equals(jour_dispo) && (!dispo_tmp.get(nom_dispo.indexOf(st)).getHeure1().equals(h_tmp) || !dispo_tmp.get(nom_dispo.indexOf(st)).getHeure2().equals(h_tmp) ))
            {
                index_nom_choisi = nom_dispo.indexOf(st);
                Log.d(TAG,"IF statement");
                ArrayList<String> heures = new ArrayList<>();


                if(!dispo_tmp.get(nom_dispo.indexOf(st)).getHeure1().equals(h_tmp)){
                    heures.add(dispo_tmp.get(nom_dispo.indexOf(st)).getHeure1());
                }
                if(!dispo_tmp.get(nom_dispo.indexOf(st)).getHeure2().equals(h_tmp)){
                    heures.add(dispo_tmp.get(nom_dispo.indexOf(st)).getHeure2());

                }
                initSpinerChoix(heures);
            }
            else {
                Toast.makeText(PriseRdv.this,"Veuillez choisir un autre jour !",Toast.LENGTH_SHORT).show();
            }


        }



    }

    public void confirmer_rdv(View view) {
        DocumentReference documentReference = db.document(path).collection("disponibilite").document(jour_dispo);
         if(choix_heure_rdv.getSelectedItem().equals(dispo_tmp.get(index_nom_choisi).getHeure1())){
           documentReference.update(
                   "heure1","00h00-00h00",
                   "heure2", dispo_tmp.get(index_nom_choisi).getHeure2()
           ).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()){
                       reponse.setText("Rendez-vous confirmé ! pour " + date.getText());
                       reponse.setVisibility(View.VISIBLE);


                       Log.d(TAG,"Updated Successfully");

                   }
                   else {
                       Log.d(TAG,"Update failed");
                   }
               }
           });
       }
         if(choix_heure_rdv.getSelectedItem().equals(dispo_tmp.get(index_nom_choisi).getHeure2())){
            documentReference.update(
                    "heure1",dispo_tmp.get(index_nom_choisi).getHeure1(),
                    "heure2","00h00-00h00"

            ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.d(TAG,"Updated Successfully");
                        reponse.setText("Votre rendez-vous est confirmé !");
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
