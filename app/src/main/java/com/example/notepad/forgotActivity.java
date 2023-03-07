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
import com.google.firebase.auth.FirebaseAuth;

public class forgotActivity extends AppCompatActivity {


    TextView tvFPassword,tvDontWorry,tvGoToLoginScreen;
    EditText etVerifyEmail;
    AppCompatButton btnRecover;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        tvFPassword = findViewById(R.id.tvFPassword);
        tvDontWorry = findViewById(R.id.tvDontWorry);
        tvGoToLoginScreen = findViewById(R.id.tvGoToLoginScreen);
        etVerifyEmail = findViewById(R.id.etVerifyEmail);
        btnRecover = findViewById(R.id.btnRecover);

        firebaseAuth = FirebaseAuth.getInstance();


        getSupportActionBar().hide();

        tvGoToLoginScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(forgotActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btnRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = etVerifyEmail.getText().toString().trim();
                if (mail.isEmpty()){
                    Toast.makeText(forgotActivity.this, "Enter mail first", Toast.LENGTH_SHORT).show();
                }else {

                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(forgotActivity.this, "Mail Sent, You Can Recover Your password using mail", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(forgotActivity.this,MainActivity.class));
                            }
                            else {
                                Toast.makeText(forgotActivity.this, "Email is Wrong or Account Not Exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                }
            }
        });

    }
}