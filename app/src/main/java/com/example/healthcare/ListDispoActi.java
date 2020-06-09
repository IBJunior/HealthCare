package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.LoginActivity;
import com.example.model.Disponibilite;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListDispoActi extends AppCompatActivity {
    RecyclerView list_dispo_recycle;
    DispoAdapter adapter;
    TextView deconnect;
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Disponibilite> dispos = new ArrayList<>();
    private static final String TAG = "ListDispoActi";
    String mail_med;
    String path=" ";
    ImageView home;
    ArrayList<String> ref_dispo_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_dispo);
        Intent i = getIntent();
        mail_med = i.getStringExtra("mail_med");
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListDispoActi.this,EspaceMedecinActivity.class);
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
                startActivity(new Intent(ListDispoActi.this, LoginActivity.class));

            }
        });

        initDispos();

        list_dispo_recycle = findViewById(R.id.list_dispo_recycle);
        list_dispo_recycle.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new DispoAdapter(dispos);
        adapter.setMcontext(this);

        list_dispo_recycle.setLayoutManager(layoutManager);
        list_dispo_recycle.setAdapter(adapter);

    }

    private  void  initDispos(){

        Log.d(TAG,"Ini List dispo callded MAIL_MED :"+ mail_med);
        db.collection("medecins")
                .whereEqualTo("email",mail_med)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Log.d(TAG,"On récupère le medecin  et essaye ses dispos");
                Boolean b = queryDocumentSnapshots.getDocuments().isEmpty();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {

                    path = document.getReference().getPath() +"/disponibilite";
                    if(path !=null){
                        db.document(document.getReference().getPath()).collection("disponibilite").get().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG,"echec de recup des dispos");
                            }
                        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                Log.d(TAG,"List des dispo en mains");

                                for (QueryDocumentSnapshot dis : queryDocumentSnapshots){
                                    Disponibilite dispo = dis.toObject(Disponibilite.class);
                                    dispos.add(dispo);
                                    ref_dispo_list.add(dis.getReference().getPath());

                                }
                                adapter.setPath(path);
                                adapter.setRef_dispo_list(ref_dispo_list);
                                adapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (dispos.isEmpty()){
                                    Toast.makeText(ListDispoActi.this, "Vous n'avez aucune disponibilité veuillez en créer", Toast.LENGTH_LONG).show();

                                }
                            }
                        });

                    }
                    else{
                       Toast.makeText(ListDispoActi.this,"votre n'avez  aucune  disponibilité ",Toast.LENGTH_LONG).show();
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Echec des dispo list");

            }
        });




    }
}
