package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.healthcare.EspacePatientActivity;
import com.example.healthcare.MainActivity;
import com.example.healthcare.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    EditText mail,passwd;
    Button connect;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mail = (EditText) findViewById(R.id.email);
        passwd = (EditText) findViewById(R.id.passwd);
        connect = (Button) findViewById(R.id.seConnecter);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mAuth.getCurrentUser();

                if(mFirebaseUser !=null){
                    Toast.makeText(LoginActivity.this,"You are logged",Toast.LENGTH_SHORT);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(LoginActivity.this,"You're not log",Toast.LENGTH_SHORT);
                }
            }
        };

    }

    public void seConnecter(View v){

        String email = mail.getText().toString();
        String pass = passwd.getText().toString();

        if(email.isEmpty())
        {
            mail.setError("Provide an email please");
            mail.requestFocus();
        }
        else if(pass.isEmpty()){
            passwd.setError("Provide a password please");
            passwd.requestFocus();
        }
        else  if(email.isEmpty() && pass.isEmpty())
        {
            Toast.makeText(LoginActivity.this,"Fields are empty",Toast.LENGTH_SHORT);

        }
        else if(!(email.isEmpty() && pass.isEmpty())){

            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){

                        Log.d(TAG, "Login Succeed");
                        startActivity(new Intent(LoginActivity.this, EspacePatientActivity.class));

                    }
                    else {

                        Toast.makeText(LoginActivity.this,"SingUp Failed",Toast.LENGTH_SHORT);
                        Log.d(TAG,"Login Failed");

                    }
                }
            });
        }

    }
    public void signupAct(View v){
       startActivity(new Intent(LoginActivity.this,CreerUnComptActivity.class));

    }

    public void creerCompte(View view) {
        final String mail_str = mail.getText().toString();
        String passwd_str = passwd.getText().toString();

        mAuth.createUserWithEmailAndPassword(mail_str,passwd_str).addOnCompleteListener(LoginActivity.this,
                new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {

                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this,CreerUnComptActivity.class);
                            intent.putExtra("mail",mail_str);
                            Log.d(TAG,"Sign  up successfully !");

                            startActivity(intent);


                        }
                        else
                        {

                            Log.d(TAG,"Sign up failed epicly");

                        }
                    }
                });


    }

}
