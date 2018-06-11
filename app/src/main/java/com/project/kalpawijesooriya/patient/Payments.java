package com.project.kalpawijesooriya.patient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Payments extends AppCompatActivity {
    TextView amout;
    String con_ID;
    String time;
    String number,Url;
    Button submit;
     String bitmap;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        Bundle b = getIntent().getExtras();
        amout = (TextView) findViewById(R.id.amount);
        submit=(Button)findViewById(R.id.submit);

        if (b != null) {

            amout.setText(b.getString("total")+".00");
            con_ID=b.getString("consulID");
            number =b.getString("number");
            time = b.getString("timeperpatient");
            Url = b.getString("Bill");
            bitmap=b.getString("bitmap");


        }
        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        final String uid=currentUser.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("consultation");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                             @Override
                                                             public void onDataChange(DataSnapshot dataSnapshot) {
                                                                 final Map<String, Object>  usermap = new HashMap<String,Object>();
                                                                 usermap.put("patient_ID",uid);
                                                                 usermap.put("Time",time);
                                                                 usermap.put("Number",number);
                                                                 usermap.put("BillImage",Url);
                                                                 final DatabaseReference appointmentRef= mDatabase.child(con_ID).child("Appointment");

                                                                 appointmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                     @Override
                                                                     public void onDataChange(DataSnapshot snapshot) {
                                                                         if (snapshot.child(uid).exists()) {
                                                                             Toast.makeText(getApplicationContext(), "Sorry,You alreday put appointment for this consultation.plese check your appointments!", Toast.LENGTH_SHORT).show();
                                                                         }
                                                                         else
                                                                         {
                                                                             appointmentRef.child(uid).updateChildren( usermap);
                                                                             mDatabase.child(con_ID).child("LastAppoimentNo").setValue(number);
                                                                             Intent intent = new Intent(Payments.this, DisplayPastAppointments.class);
                                                                             Bundle b = new Bundle();
                                                                             b.putString("bitmap",bitmap);
                                                                             intent.putExtras(b);
                                                                             startActivity(intent);
                                                                             finish();

                                                                         }
                                                                     }

                                                                     @Override
                                                                     public void onCancelled(DatabaseError databaseError) {

                                                                     }
                                                                 });





                                                             }

                                                             @Override
                                                             public void onCancelled(DatabaseError databaseError) {}
                                                         }
                );
            }
        });


    }


}
