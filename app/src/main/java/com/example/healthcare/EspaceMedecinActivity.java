package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class EspaceMedecinActivity extends AppCompatActivity {

    TextView textView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_medecin);
        textView = findViewById(R.id.mail_med);

        Intent i = getIntent();

        textView.setText(i.getStringExtra("mail_med"));
    }
}
