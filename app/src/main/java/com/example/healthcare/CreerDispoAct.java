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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.login.LoginActivity;
import com.example.model.Disponibilite;
import com.example.model.Medecin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreerDispoAct extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    String mail_med;
    TextView chjourDispo,jourDispo,heure_dbt_1,heure_dbt_2,heure_fin_1,heure_fin_2,
            choix_heure_dbt_1,choix_heure_dbt_2,choix_heure_fin_1,choix_heure_fin_2;
    Button creer;
    ImageView home;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "CreerDispoAct";
    ArrayList<Medecin> tmp_list = new ArrayList<>();
    Calendar c = Calendar.getInstance();
    String path;
    TextView deconnect;
    Disponibilite dispo;
    boolean heure_dbt_1_bool,heure_dbt_2_bool,heure_fin_1_bool,heure_fin_2_bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_dispo);
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CreerDispoAct.this,EspaceMedecinActivity.class);
                intent1.putExtra("mail_med",mail_med);
                startActivity(intent1);
            }
        });
        deconnect = findViewById(R.id.deconnexion);
        deconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(CreerDispoAct.this, LoginActivity.class));

            }
        });

        chjourDispo = findViewById(R.id.chjourDispo);
        jourDispo = findViewById(R.id.jourDispo);
        choix_heure_dbt_1 = findViewById(R.id.choix_heure_dbt_1);
        choix_heure_dbt_2 = findViewById(R.id.choix_heure_dbt_2);
        choix_heure_fin_1 = findViewById(R.id.choix_heure_fin_1);
        choix_heure_fin_2 = findViewById(R.id.choix_heure_fin_2);

        heure_dbt_1 = findViewById(R.id.heure_dbt_1);
        heure_dbt_2 = findViewById(R.id.heure_dbt_2);
        heure_fin_1 = findViewById(R.id.heure_fin_1);
        heure_fin_2 = findViewById(R.id.heure_fin_2);

        creer = findViewById(R.id.creerDispo);
        heure_dbt_1_bool=false;
        heure_dbt_2_bool=false;
        heure_fin_1_bool=false;
        heure_fin_2_bool=false;
        Intent i = getIntent();
        mail_med = i.getStringExtra("mail_med");

        creer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creerDisponibilite();
            }
        });

        chjourDispo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment choixDate = new ChoixDateFragment();
                choixDate.show(getSupportFragmentManager(),"Choix date");
            }
        });
        choix_heure_dbt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment choixHuere = new ChoixHeureFragment();
                choixHuere.show(getSupportFragmentManager(),"Choix heure");
                heure_dbt_1_bool = true;
            }
        });
        choix_heure_dbt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment choixHuere = new ChoixHeureFragment();
                choixHuere.show(getSupportFragmentManager(),"Choix heure");
                heure_dbt_2_bool = true;
            }
        });
        choix_heure_fin_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment choixHuere = new ChoixHeureFragment();
                choixHuere.show(getSupportFragmentManager(),"Choix heure");
                heure_fin_1_bool = true;
            }
        });
        choix_heure_fin_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment choixHuere = new ChoixHeureFragment();
                choixHuere.show(getSupportFragmentManager(),"Choix heure");
                heure_fin_2_bool = true;
            }
        });



    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String dateChoisie = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        chjourDispo.setText(dateChoisie);

    }
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        if(heure_dbt_1_bool){
            choix_heure_dbt_1.setText(hourOfDay + "h" + minute);

            heure_dbt_1_bool = false;
        }
        if(heure_dbt_2_bool){
            choix_heure_dbt_2.setText(hourOfDay + "h" + minute);
            heure_dbt_2_bool = false;
        }
        if(heure_fin_1_bool){
            choix_heure_fin_1.setText(hourOfDay + "h" + minute);
            heure_fin_1_bool = false;
        }
        if(heure_fin_2_bool){
            choix_heure_fin_2.setText(hourOfDay + "h" + minute);
            heure_fin_2_bool = false;
        }





    }
    private void creerDisponibilite(){
        //préparation des infos de dispo
        String dateChoisi = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        String heure1 =  choix_heure_dbt_1.getText().toString() + "-" + choix_heure_fin_1.getText().toString();
        String heure2 =  choix_heure_dbt_2.getText().toString() + "-" + choix_heure_fin_2.getText().toString();
        dispo = new Disponibilite();
        dispo.setHeure1(heure1);
        dispo.setHeure2(heure2);
        dispo.setJour(dateChoisi);



        db.collection("medecins")
                .whereEqualTo("email",mail_med)
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

                                db.document(path).collection("disponibilite").document().set(dispo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Log.d(TAG,"Ajout de dispo réussi");
                                            Toast.makeText(CreerDispoAct.this,"Disponibilité créée avec succès", Toast.LENGTH_LONG).show();

                                            startActivity(new Intent(CreerDispoAct.this,GestionDispoActivity.class));
                                        }
                                        else {
                                            Log.d(TAG,"Ajout de dispo échoué");
                                        }
                                    }
                                });

                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }



}
