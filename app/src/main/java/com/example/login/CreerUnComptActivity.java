package com.example.login;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.MainActivity;
import com.example.healthcare.R;
import com.example.model.Medecin;
import com.example.model.Patient;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreerUnComptActivity extends AppCompatActivity {
    Spinner situations,specialites;
    EditText passwd,naissance,nom,
            prenom,ville,tel;
    String mail_str;
    String image_url;
    RadioGroup profile;
    private static final String TAG = "MainActivity";
    RadioButton med_radio,pat_radio;
    LinearLayout pat_only, med_only;
    private FirebaseAuth mAuth;
    Button valide,photo_profile;
    ImageView img_mini;
    private Uri imageUri;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    StorageReference storageReference;
    private final static int PICK_IMAGE_REQUEST = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_un_compt);
        situations  = (Spinner) findViewById(R.id.situation);
        specialites  = (Spinner) findViewById(R.id.specialite);
        pat_only = (LinearLayout) findViewById(R.id.pat_only);
        med_only = (LinearLayout) findViewById(R.id.med_only);

        photo_profile = findViewById(R.id.photo_profile);
        img_mini = findViewById(R.id.img_mini);

        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        ville = (EditText) findViewById(R.id.ville);
        tel = (EditText) findViewById(R.id.tel);

        med_radio = (RadioButton) findViewById(R.id.med_radio);
        pat_radio = (RadioButton) findViewById(R.id.pat_radio);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.situations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        situations.setAdapter(adapter);
        ArrayAdapter<CharSequence> adapter_med = ArrayAdapter.createFromResource(this,R.array.specialite, android.R.layout.simple_spinner_item);
        adapter_med.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialites.setAdapter(adapter_med);
        Intent i = getIntent();

        mail_str  = i.getStringExtra("mail");
        mAuth = FirebaseAuth.getInstance();

        profile = (RadioGroup) findViewById(R.id.profile);
        valide = findViewById(R.id.vald);
        naissance = (EditText) findViewById(R.id.naissance);
        mask_date(naissance);

        profile.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId== R.id.med_radio){
                    pat_only.setVisibility(View.GONE);
                    valide.setVisibility(View.GONE);
                    med_only.setVisibility(View.VISIBLE);
                    valide.setVisibility(View.VISIBLE);

                }
                if(checkedId== R.id.pat_radio){
                    med_only.setVisibility(View.GONE);
                    valide.setVisibility(View.GONE);
                    pat_only.setVisibility(View.VISIBLE);
                    valide.setVisibility(View.VISIBLE);

                }
            }
        });



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

    private String  getExtension(Uri uri){

        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return  mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private String addPhotoProfile(String nom_image,String profile){
        storageReference = FirebaseStorage.getInstance().getReference(profile);
        image_url = "";
        final StorageReference fileRef = storageReference.child(nom_image + "." +getExtension(imageUri));

        final  UploadTask uploadTask =  fileRef.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"échec d'upload");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG,"Image uploaded !");

                Task<Uri> urlstuff = uploadTask.continueWith(new Continuation<UploadTask.TaskSnapshot, Uri>() {
                    @Override
                    public Uri then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()){
                            Log.d(TAG,"sauvegarde de l'image échouée");
                        }

                        Log.d(TAG,"Recup URL : "+ fileRef.getDownloadUrl().toString());
                        image_url = fileRef.getDownloadUrl().toString();
                        return null;
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG,"Recuperation de url success");
                        }
                    }
                });
            }
        });

        /*if (imageUri != null){
            StorageReference fileRef = storageReference.child(nom_image + "." +getExtension(imageUri));
            fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Log.d(TAG,"Image saved successfully");
                    image_url = taskSnapshot.getStorage().getDownloadUrl().toString();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"Image not saved ");
                }
            });

        }else {
            Toast.makeText(CreerUnComptActivity.this,"Veuillez choisir une image",Toast.LENGTH_LONG).show();
        }*/

        return image_url;

    }


    private void addMedecin(){
        String imgUrl;
        String nom_str = nom.getText().toString();
        String prenom_str = prenom.getText().toString();
        String adresse_str = ville.getText().toString();
        String tel_str = tel.getText().toString();
        String specialite = specialites.getSelectedItem().toString();
        imgUrl = addPhotoProfile(mail_str,"photos_profile_medecin");


        final Medecin med = new Medecin( nom_str, prenom_str, specialite, adresse_str, tel_str, mail_str);
        med.setPhotoUrl(imgUrl);

        db.collection("medecins").add(med).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG,"DocumentSnapshot added with ID: " + documentReference.getId());
                Toast.makeText(CreerUnComptActivity.this,"Compte créé avec Succès veuillez vous connectez!",Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CreerUnComptActivity.this,LoginActivity.class);
                startActivity(intent);
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
        String imgUrl;
        String nom_str = nom.getText().toString();
        String prenom_str = prenom.getText().toString();
        String adresse_str = ville.getText().toString();
        String tel_str = tel.getText().toString();
        String situation = situations.getSelectedItem().toString();
        String date_naisse = naissance.getText().toString();
        imgUrl = addPhotoProfile(mail_str,"photos_profile_patient");


        Patient pat = new Patient(nom_str,prenom_str, date_naisse,situation,mail_str,tel_str,adresse_str);
        pat.setPhotoUrl(imgUrl);

        db.collection("patients").add(pat).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG,"DocumentSnapshot added with ID: " + documentReference.getId());
                Toast.makeText(CreerUnComptActivity.this,"Compte créé avec Succès veuillez vous connectez!",Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(CreerUnComptActivity.this,LoginActivity.class));
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
        Log.d(TAG,"Valider : called !");
        if (med_radio.isChecked()){
            addMedecin();
        }
        if(pat_radio.isChecked()){
                   addPatient();
        }

    }

    public void choisirImage(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode== RESULT_OK && data != null
                && data.getData() !=null){
            imageUri = data.getData();
            img_mini.setImageURI(imageUri);

        }
    }
}
