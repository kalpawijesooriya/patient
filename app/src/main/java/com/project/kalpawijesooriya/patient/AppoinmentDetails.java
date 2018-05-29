package com.project.kalpawijesooriya.patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Map;


public class AppoinmentDetails extends AppCompatActivity {
    private Toolbar mToolbar;
    private String consultationID;
    private FirebaseAuth mAuth;
   private DatabaseReference ref;
   private Button  confirm;
   String Totalfee;
private String consulID;
    private Firebase mdatabase;
    private Firebase doctorref;
    private Firebase sheduleref;
   private TextView docName,special,datetext,startTime,apxTime,doc_fee,hos_fee,appointmentNo,roomNo,totalfee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinment_details);


        mToolbar = (Toolbar) findViewById(R.id.consultation_page_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar = (Toolbar) findViewById(R.id.consultation_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Appointments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        special = (TextView) findViewById(R.id.special);
        datetext = (TextView) findViewById(R.id.date);
        docName = (TextView) findViewById(R.id.docName);
        doc_fee = (TextView) findViewById(R.id.docFee);
        startTime = (TextView) findViewById(R.id.startTime);
        hos_fee = (TextView) findViewById(R.id.hosFee);
        apxTime = (TextView) findViewById(R.id.aprxTime);
        appointmentNo = (TextView) findViewById(R.id.appointmentNO);
        roomNo = (TextView) findViewById(R.id.roomNO);
        confirm=(Button)findViewById(R.id.confirm);
        totalfee=(TextView)findViewById(R.id.totalFee);
        Bundle b = getIntent().getExtras();


        if (b != null) {

            roomNo.setText(b.getString("room"));
            datetext.setText(b.getString("date"));
            doc_fee.setText("Rs."+b.getString("fee")+".00");
            startTime.setText( b.getString("startTime"));
            docName.setText(b.getString("docName"));
            special.setText(b.getString("spciality"));
            apxTime.setText(b.getString("timeperpatient"));
            appointmentNo.setText(b.getString("number"));
            totalfee.setText(b.getString("fee")+".00");
            consulID =b.getString("consulID");
            Totalfee=b.getString("fee");


        }


     confirm.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(AppoinmentDetails.this, Payments.class);
             Bundle b = new Bundle();
             b.putString("total",Totalfee);

             b.putString("timeperpatient",b.getString("timeperpatient"));
             b.putString("number",b.getString("number") );
             b.putString("consulID",consulID);
             intent.putExtras(b);
             startActivity(intent);
             finish();

         }
     });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();

    }
}
