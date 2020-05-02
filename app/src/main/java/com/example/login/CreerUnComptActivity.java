package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcare.MainActivity;
import com.example.healthcare.R;
import com.example.model.Medecin;
import com.example.model.Patient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class CreerUnComptActivity extends AppCompatActivity {
    Spinner situations,specialites;
    EditText mail,passwd,date_naiss,nom,
            prenom,adresse,tel;
    TextView suivant;
    ImageView logo;
    private static final String TAG = "MainActivity";
    RadioButton medecin_radio,patient_radio;
    LinearLayout inscription_layout,info_login,infos_personnelles,
            type_compt_layout,specialites_layout,situations_layout;
    private FirebaseAuth mAuth;
    private boolean mail_exist;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_un_compt);
        situations  = (Spinner) findViewById(R.id.situations);
        specialites  = (Spinner) findViewById(R.id.specialites);
        situations_layout = (LinearLayout) findViewById(R.id.situations_layout);
        specialites_layout = (LinearLayout) findViewById(R.id.specialites_layout);

        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        adresse = (EditText) findViewById(R.id.adresse);
        tel = (EditText) findViewById(R.id.phone);

        suivant = (TextView) findViewById(R.id.suivant);
        logo = (ImageView) findViewById(R.id.logo);
        medecin_radio = (RadioButton) findViewById(R.id.medecin_radio);
        patient_radio = (RadioButton) findViewById(R.id.patient_radio);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.situations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        situations.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter_med = ArrayAdapter.createFromResource(this,R.array.specialite, android.R.layout.simple_spinner_item);
        adapter_med.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialites.setAdapter(adapter_med);
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

                if(mFirebaseUser !=null){
                    Toast.makeText(CreerUnComptActivity.this,"You are logged",Toast.LENGTH_SHORT);
                    startActivity(new Intent(CreerUnComptActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(CreerUnComptActivity.this,"You're not log",Toast.LENGTH_SHORT);
                }
            }
        };


        infos_personnelles = (LinearLayout) findViewById(R.id.infos_personnelles);
        inscription_layout = (LinearLayout) findViewById(R.id.inscription_layout);
        info_login = (LinearLayout) findViewById(R.id.info_login);
        type_compt_layout = (LinearLayout) findViewById(R.id.type_compt_layout);

        mail = (EditText) findViewById(R.id.mail);
        passwd = (EditText) findViewById(R.id.passwd);
        date_naiss = (EditText) findViewById(R.id.date_naiss);

        mask_date(date_naiss);



    }

    private  void mask_date(final EditText date){


           TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    date.setText(current);
                    date.setSelection(sel < current.length() ? sel : current.length());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        };

        date.addTextChangedListener(tw);
    }

    public  void suivant(View view){

        if (medecin_radio.isChecked() || patient_radio.isChecked()){
            type_compt_layout.setVisibility(View.GONE);
            inscription_layout.setVisibility(View.VISIBLE);
            info_login.setVisibility(View.VISIBLE);


        }
        if(!medecin_radio.isChecked() && !patient_radio.isChecked()){
            Toast.makeText(CreerUnComptActivity.this,"Veuillez choisir un profile",Toast.LENGTH_SHORT).show();
        }


    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void continuer(View view){

        String mail_str = mail.getText().toString();
        String passwd_str = passwd.getText().toString();


        if(mail_str.isEmpty()){

            mail.setError("Entrez une adresse mail");
            mail.requestFocus();

        }
        else if(passwd_str.isEmpty()){
            passwd.setError("Entrez un mot de passe");
            passwd.requestFocus();
        }
        else  if(!mail_str.isEmpty() && !passwd_str.isEmpty()){
           if( ! isEmailValid(mail_str)){
               mail.setError("Entrez une adresse mail valide");
               mail.requestFocus();
           }
           else if(passwd_str.length() < 8){

                passwd.setError("Entrer un mot de passe de minimum 8 caractères");
                passwd.requestFocus();
           }
           if(isEmailValid(mail_str) && passwd_str.length() >= 8){
               logo.setVisibility(View.GONE);
               infos_personnelles.setVisibility(View.VISIBLE);
               if(medecin_radio.isChecked() ){
                   info_login.setVisibility(View.GONE);
                   specialites_layout.setVisibility(View.VISIBLE);
               }
               if (patient_radio.isChecked()){
                   info_login.setVisibility(View.GONE);
                   situations_layout.setVisibility(View.VISIBLE);
                   date_naiss.setVisibility(View.VISIBLE);
               }

           }
        }
        else {
            Toast.makeText(CreerUnComptActivity.this,"Veuillez entrer les infos de login",Toast.LENGTH_SHORT);
        }


    }


    public void showSignInForm(View view){
        startActivity(new Intent(CreerUnComptActivity.this, LoginActivity.class));
    }

    public  boolean checkEmailInFirebase(){


        String mail_str = mail.getText().toString();
        mAuth.fetchSignInMethodsForEmail(mail_str).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                 mail_exist = ! task.getResult().toString().isEmpty();
                 Toast.makeText(CreerUnComptActivity.this,task.getResult().toString(),Toast.LENGTH_LONG).show();
            }
        });

        return  mail_exist;

    }

    private boolean signUpFirebase(){

        String mail_str = mail.getText().toString();
        String passwd_str = passwd.getText().toString();
        mAuth.createUserWithEmailAndPassword(mail_str,passwd_str).addOnCompleteListener(CreerUnComptActivity.this,
                new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {

                        if(!task.isSuccessful()){
                            mail_exist = true;
                            Toast.makeText(CreerUnComptActivity.this,"SingUp Failed",Toast.LENGTH_SHORT);

                        }
                        else
                        {mail_exist = false;

                            startActivity(new Intent(CreerUnComptActivity.this,MainActivity.class));

                        }
                    }
                });
        return mail_exist;
    }

   private void addMedecin(){
        String nom_str = nom.getText().toString();
        String prenom_str = prenom.getText().toString();
        String adresse_str = adresse.getText().toString();
        String tel_str = tel.getText().toString();
        String specialite = specialites.getSelectedItem().toString();
        String mail_str = mail.getText().toString();

        final Medecin med = new Medecin( nom_str, prenom_str, specialite, adresse_str, tel_str, mail_str);

        db.collection("medecins").add(med).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG,"DocumentSnapshot added with ID: " + documentReference.getId());
                startActivity(new Intent(CreerUnComptActivity.this,MainActivity.class));
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error adding document", e);
                    }
                });


    }

    private void addPatient(){
        String nom_str = nom.getText().toString();
        String prenom_str = prenom.getText().toString();
        String adresse_str = adresse.getText().toString();
        String tel_str = tel.getText().toString();
        String situation = situations.getSelectedItem().toString();
        String mail_str = mail.getText().toString();
        Date  date_naissance = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date_naissance = sdf.parse(date_naiss.getText().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Patient pat = new Patient(nom_str,prenom_str, date_naissance,situation,mail_str,tel_str,adresse_str);


        db.collection("patients").add(pat).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG,"DocumentSnapshot added with ID: " + documentReference.getId());
                startActivity(new Intent(CreerUnComptActivity.this,MainActivity.class));
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Error adding document", e);
                    }
                });


    }
    public void valider(View view){


           if(!signUpFirebase()){
               if (medecin_radio.isChecked()){
                   addMedecin();
               }
               if(patient_radio.isChecked()){
                   addPatient();
               }
           }
           else {
               Toast.makeText(this, "Le mail a déjà un compte",Toast.LENGTH_LONG).show();
           }





    }





}
