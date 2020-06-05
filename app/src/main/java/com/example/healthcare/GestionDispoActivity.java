package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GestionDispoActivity extends AppCompatActivity {

    TextView creerDispo,mesDispo;
    String mail_med;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_dispo);
        mesDispo = findViewById(R.id.mesDispo);
        creerDispo = findViewById(R.id.creerDispo);
        final Intent i = getIntent();
        mail_med = i.getStringExtra("mail_med");

        mesDispo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionDispoActivity.this,ListDispoActi.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);
            }
        });

        creerDispo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GestionDispoActivity.this,CreerDispoAct.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);
            }
        });

    }
}
