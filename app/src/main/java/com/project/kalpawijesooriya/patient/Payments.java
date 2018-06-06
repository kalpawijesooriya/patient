package com.project.kalpawijesooriya.patient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Payments extends AppCompatActivity {
    TextView amout;
    String con_ID;
    String time;
    String number;
    Button submit;
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
                                                                 Map<String, Object>  usermap = new HashMap<String,Object>();
                                                                 usermap.put("patient_ID",uid);
                                                                 usermap.put("Time",time);
                                                                 usermap.put("Number",number);
                                                                 mDatabase.child(con_ID).child("Appointment").child(uid).updateChildren( usermap);

                                                             }

                                                             @Override
                                                             public void onCancelled(DatabaseError databaseError) {}
                                                         }
                );
            }
        });


    }
}
