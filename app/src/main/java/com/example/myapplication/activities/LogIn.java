package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.databinding.ActivityLogInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {
private ActivityLogInBinding binding;
private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        onClickListeners();

        pd = new ProgressDialog(this);

        binding.logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkForProperInput()) {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(
                            binding.emailTxtlg.getText().toString(),
                            binding.passTxtlg.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent = new Intent(LogIn.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LogIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void onClickListeners() {
        binding.dontaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
            }
        });binding.SignupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    // Checking if any text field is left empty.
    private boolean checkForProperInput() {
        if(binding.passTxtlg.getText().toString().equals("") || binding.passTxtlg.getText().toString().equals("null")){
            Toast.makeText(LogIn.this, "Write Password properly.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(binding.emailTxtlg.getText().toString().equals("") || binding.emailTxtlg.getText().toString().equals("null")){
            Toast.makeText(LogIn.this, "Write Email properly.", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }
}