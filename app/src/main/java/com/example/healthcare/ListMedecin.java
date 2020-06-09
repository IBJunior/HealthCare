package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.LoginActivity;
import com.example.model.Medecin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListMedecin extends AppCompatActivity {

    private ArrayList<Medecin> medecins = new ArrayList<>();
    private static final String TAG = "ListPatient";
    MedecinListAdapter medecinListAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText search;
    String mail_pat;
    ImageView home;
    TextView deconnect;
    CircleImageView photo_profile;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference stRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"On Create called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_medecin);
        Log.d(TAG,"Oncreate Started");
        search = (EditText) findViewById(R.id.search);
        home = findViewById(R.id.home);
        Intent i = getIntent();
        mail_pat = i.getStringExtra("mail_pat");

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListMedecin.this,EspacePatientActivity.class);
                intent.putExtra("mail_pat",mail_pat);
                startActivity(intent);
            }
        });
        photo_profile = findViewById(R.id.photo_profile);
        initPhotoProfile();

        deconnect = findViewById(R.id.deconnexion);
        deconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(ListMedecin.this, LoginActivity.class));

            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        iniMedecinList();

    }

    private void filter(String s){
        ArrayList<Medecin> medecins_filter = new ArrayList<>();

        for (Medecin med : medecins){
            if (med.getSpecialite().toLowerCase().trim().contains(s) || med.getAdresse().toLowerCase().trim().contains(s)){
                medecins_filter.add(med);
            }
        }
        medecinListAdapter.filterList(medecins_filter);
    }
    private void iniMedecinList(){
        db.collection("medecins").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d(TAG,"Souley");
                if(task.isSuccessful()){
                    Toast.makeText(ListMedecin.this,"Success",Toast.LENGTH_SHORT);

                    for (QueryDocumentSnapshot d : task.getResult()) {

                        Medecin med =  d.toObject(Medecin.class);

                        medecins.add(med);
                        Log.d(TAG," "+ med.getNom());


                    }
                    medecinListAdapter.notifyDataSetChanged();

                }
                else {
                    Toast.makeText(ListMedecin.this," not Success",Toast.LENGTH_SHORT);
                    Log.d(TAG,"No items in your collections");
                }

            }
        });

        initMedecinRecyclerView();
    }
    private void initMedecinRecyclerView(){
        Log.d(TAG,"Initiation of RecyclerView started");


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.medecin_recycler);
        medecinListAdapter = new MedecinListAdapter(this,medecins);
        medecinListAdapter.setMail_pat(mail_pat);
        recyclerView.setAdapter(medecinListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


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
