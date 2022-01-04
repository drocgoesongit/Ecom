package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Model.Users;
import com.example.myapplication.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        pd = new ProgressDialog(this);
        pd.setMessage("Creating your account");

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(SignUp.this, MainActivity.class);
            startActivity(intent);
        }

        //Login screen click listeners.
        binding.signUpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LogIn.class);
                startActivity(intent);
            }
        });        binding.dontaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, LogIn.class);
                startActivity(intent);
            }
        });

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkForProperInput()){
                    FirebaseAuth.getInstance().getInstance().createUserWithEmailAndPassword(
                            binding.emailTxt.getText().toString(),
                            binding.passTxt.getText().toString()).
                            addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Users user = new Users(binding.usernameTxt.getText().toString(), binding.emailTxt.getText().toString(), FirebaseAuth.getInstance().getUid());
                                FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        pd.dismiss();
                                        Toast.makeText(SignUp.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUp.this, AddAddress.class);
                                        startActivity(intent);
                                    }
                                });
                            }else{
                                Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    // Checking if any text field is left empty.
    private boolean checkForProperInput() {
        if(binding.usernameTxt.getText().toString().equals("") || binding.usernameTxt.getText().toString().equals("null")){
            Toast.makeText(SignUp.this, "Write name properly.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(binding.passTxt.getText().toString().equals("") || binding.passTxt.getText().toString().equals("null")){
            Toast.makeText(SignUp.this, "Write Password properly.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(binding.emailTxt.getText().toString().equals("") || binding.emailTxt.getText().toString().equals("null")){
            Toast.makeText(SignUp.this, "Write Email properly.", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }
}