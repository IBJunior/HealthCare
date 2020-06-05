package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class EspaceMedecinActivity extends AppCompatActivity {

    String mail_med;
    ImageView dispo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_medecin);

        final Intent i = getIntent();

        mail_med = i.getStringExtra("mail_med");
        dispo = findViewById(R.id.dispo);

        dispo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EspaceMedecinActivity.this,GestionDispoActivity.class);
                intent.putExtra("mail_med",mail_med);
                startActivity(intent);
            }
        });
    }
}
