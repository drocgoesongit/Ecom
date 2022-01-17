package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.Model.UserComplete;
import com.example.myapplication.Model.Users;
import com.example.myapplication.R;
import com.example.myapplication.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Settings extends AppCompatActivity {
private ActivitySettingsBinding binding;
private Users currentUser;
private static final String TAG = "Settings";
private Uri postUri;
private String postURL;
private ProgressDialog pd;
private ProgressDialog pd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        pd = new ProgressDialog(this);
        pd.setMessage("updating your profile");
        pd2 = new ProgressDialog(this);
        pd2.setMessage("retrieving your profile info");

        getData();
        binding.addPhotoSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        binding.updateButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
            }
        });

    }

    private void addImage() {
        Intent intent = new Intent(Intent. ACTION_GET_CONTENT);
        intent. addCategory(Intent. CATEGORY_OPENABLE);
        intent. setType("image/*");
        //  intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent,12);
    }

    private void updateData() {
        new MaterialAlertDialogBuilder(Settings.this)
                .setTitle("Confirm changes?")
                .setMessage("Selected changes will apply to your profile.")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // uploading to the firebase storage.
                        pd.show();
                        if(postUri != null){
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("User Images")
                                .child(String.valueOf(System.currentTimeMillis()));
                        StorageTask task = storageReference.putFile(postUri);
                        task.continueWithTask(new Continuation() {
                            @Override
                            public Object then(@NonNull Task task) throws Exception {
                                if(!task.isSuccessful()){
                                    throw task.getException();
                                }else{
                                    return storageReference.getDownloadUrl();
                                }
                            }
                        }).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Uri uri = (Uri) task.getResult();
                                postURL = uri.toString();
                                // updating in the firebase realtime.
                                UserComplete userWithImage = new UserComplete( postURL, FirebaseAuth.getInstance().getUid());
                                FirebaseDatabase.getInstance().getReference().child("UserImage")
                                        .child(FirebaseAuth.getInstance().getUid())
                                        .setValue(userWithImage)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                updateName();
                                            }
                                        });
                            }
                        });
                        }else{
                            updateName();
                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    private void updateName(){
        Users updatedUser  = new Users(binding.usernameTxtSettings.getText().toString(), currentUser.getEmail(), currentUser.getId());
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(FirebaseAuth.getInstance().getUid())
                .setValue(updatedUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        pd.dismiss();
                        Toast.makeText(Settings.this, "Profile updated successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void getData() {
        pd2.show();
        FirebaseDatabase.getInstance().getReference()
            .child("Users")
            .child(FirebaseAuth.getInstance().getUid())
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    currentUser = snapshot.getValue(Users.class);
                    binding.usernameTxtSettings.setText(currentUser.getUsername());
                    Log.w(TAG, "user recieved is : "+ currentUser.getUsername());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "error while receiving user. "+ error.getMessage());
                }
            });
        FirebaseDatabase.getInstance().getReference()
                .child("UserImage")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            UserComplete user = snapshot.getValue(UserComplete.class);
                            Picasso.get().load(user.getImage()).placeholder(R.color.light_gray).into(binding.settingImage);
                        }
                        pd2.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 12){
            postUri = data.getData();
            binding.settingImage.setImageURI(postUri);
            Toast.makeText(Settings.this, "Image added successfully..", Toast.LENGTH_SHORT).show();
        }
    }
}