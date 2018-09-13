package com.project.kalpawijesooriya.patient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Payments extends AppCompatActivity {
    TextView amout;
    String con_ID;
    String time;
    private String uid;
    String number,Url;
    Button submit;
    private  String bitmap,key;
    private DatabaseReference mDatabase,mDatabaseAppointments,patientRef;
    final Map<String, Object>  usermap = new HashMap<String,Object>();
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
        uid=currentUser.getUid();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("consultation");
        patientRef= FirebaseDatabase.getInstance().getReference().child("User").child("patient").child(uid);
        mDatabaseAppointments= FirebaseDatabase.getInstance().getReference().child("Appointments");

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {




                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {

                         usermap.put("Patient_ID",uid);

                         final DatabaseReference appointmentRef= mDatabase.child(con_ID).child("Appointments");

                         appointmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                             @Override
                             public void onDataChange(DataSnapshot snapshot) {
                                 if (snapshot.child(uid).exists()) {
                                     Toast.makeText(getApplicationContext(), "Sorry,You alreday put appointment for this consultation.plese check your appointments!", Toast.LENGTH_SHORT).show();
                                 }
                                 else
                                 {
                                     appointmentRef.child(uid).updateChildren(usermap);
                                     add_Appointment();

                                 }
                             }

                             @Override
                             public void onCancelled(DatabaseError databaseError) {

                             }
                         });





                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {}
                 });

            }
        });


    }

  private void  add_Appointment()
    {
        mDatabaseAppointments.addListenerForSingleValueEvent(new ValueEventListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
                String formatted = df.format(new Date());
                usermap.put("patient_ID",uid);
                usermap.put("Time",time);
                usermap.put("Number",number);
                usermap.put("BillImage",Url);
                usermap.put("Date",formatted );
                usermap.put("ConsultationID",con_ID);
                patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String appointmentCount=dataSnapshot.child("AppointmentCount").getValue().toString();
                        int result = Integer.parseInt(appointmentCount);
                        result++;
                        final int finalResult = result;

                        patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                dataSnapshot.getRef().child("AppointmentCount").setValue(finalResult);


                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("User", databaseError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                mDatabaseAppointments.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.child(uid).exists()) {

                        }
                        else
                        {
                            mDatabaseAppointments.push().updateChildren( usermap);
                            String key=mDatabaseAppointments.getKey();
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
        });

    }
}
