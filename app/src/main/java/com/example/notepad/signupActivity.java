package com.example.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signupActivity extends AppCompatActivity {

    TextView tvNewUser,tvNiceUHere,tvWantLogin;
    EditText etSignEmail,etSignPassword;
    AppCompatButton btnNewSignUp;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        tvNewUser = findViewById(R.id.tvNewUser);
        tvNiceUHere = findViewById(R.id.tvNiceUHere);
        tvWantLogin = findViewById(R.id.tvWantLogin);
        etSignEmail = findViewById(R.id.etSignEmail);
        etSignPassword = findViewById(R.id.etSignPassword);
        btnNewSignUp = findViewById(R.id.btnNewSignUp);
        firebaseAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();


        tvWantLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signupActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        btnNewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = etSignEmail.getText().toString().trim();
                String password = etSignPassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(signupActivity.this, "All Field are Required", Toast.LENGTH_SHORT).show();
                }

                else if (password.length()<7){
                    Toast.makeText(signupActivity.this, "Password should greater then 7 digit", Toast.LENGTH_SHORT).show();
                }
                else {

                    firebaseAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(signupActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();

                            }
                            else {
                                Toast.makeText(signupActivity.this, "Failed To Register", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
            }
        });
    }

    private void sendEmailVerification(){

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(signupActivity.this, "Verification Email is Sent, Verify and Login Again", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(signupActivity.this,MainActivity.class));
                }
            });
        }
        else {
            Toast.makeText(this, "Failed To Send Verification Email", Toast.LENGTH_SHORT).show();
        }
    }
}