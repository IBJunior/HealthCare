package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.login.LoginActivity;

public class HomeActivity extends AppCompatActivity {

    TextView list_med, list_pat,deconnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        list_med = (TextView) findViewById(R.id.list_med);
        list_pat = (TextView) findViewById(R.id.list_pat);
        list_med.setClickable(true);
        list_pat.setClickable(true);
        deconnexion = (TextView) findViewById(R.id.deconnexion);
        deconnexion.setClickable(true);
        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            }
        });

        list_pat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(HomeActivity.this,ListPatient.class));
            }
        });
        list_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,ListMedecin.class));
            }
        });
    }
}
