package com.project.kalpawijesooriya.patient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileImage extends AppCompatActivity {
    private final int PICK_IMAGE_REQUEST = 71;
    private StorageReference storageReference,ref;
    private Uri imageuri;
    private FirebaseStorage storage;
    private ImageView profilrpic;
    private Button upload,skip,chose;
    private FirebaseAuth auth;
    private String uid;
    private Uri filePath;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);

        auth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        skip=(Button) findViewById(R.id.skip);
        upload=(Button) findViewById(R.id.uploadImage);
        chose=(Button) findViewById(R.id.choseImage);





        profilrpic = (ImageView) findViewById(R.id.profilepic);

        chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileImage.this,Login.class));
                finish();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(Intent.createChooser(intent, "Select Picture1"), PICK_IMAGE_REQUEST);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profilrpic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage()
    {




        if(filePath!= null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(ProfileImage.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
            uid=currentUser.getUid();
            ref = storageReference.child("User").child("patient").child(uid);

            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    //imageuri=ref.getDownloadUrl().getResult().toString();
                    imageuri = taskSnapshot.getDownloadUrl();

                    mDatabase= FirebaseDatabase.getInstance().getReference().child("User").child("patient").child(uid);
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                       @Override
                                                       public void onDataChange(DataSnapshot dataSnapshot) {
                                                           Map<String, Object> postValues = new HashMap<String,Object>();
                                                           for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                               postValues.put(snapshot.getKey(),snapshot.getValue());
                                                           }
                                                           postValues.put("image", imageuri.toString().trim());
                                                           mDatabase.updateChildren(postValues);
                                                       }

                                                       @Override
                                                       public void onCancelled(DatabaseError databaseError) {}
                                                   }
                    );
                    startActivity(new Intent(ProfileImage.this,Login.class));
                    finish();
                    Toast.makeText(ProfileImage.this, "Image Uploaded" , Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileImage.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");

                        }
                    });

        }
    }
}
