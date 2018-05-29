package com.project.kalpawijesooriya.patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SelectDoctor extends AppCompatActivity {
    private Toolbar mToolbar;
    DatabaseReference ref;
    private Spinner spinner_specility;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private Spinner spinner_doctor;
    private String selected_specility,selected_doctor;
    private Button search;
    private  String doc_name,fee;
    DatabaseReference doctor_ref;
    String TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);
        auth = FirebaseAuth.getInstance();
        spinner_specility= (Spinner) findViewById(R.id.specility);
        spinner_doctor= (Spinner) findViewById(R.id.doctor);

        mToolbar=(Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Appointments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ref = FirebaseDatabase.getInstance().getReference();
        doctor_ref = ref.child("User").child("doctor");

        search=(Button)findViewById(R.id.search_doctor_btn);

       // add all the specilities to specility snipper
        doctor_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<String>();
                for (DataSnapshot zoneSnapshot: dataSnapshot.getChildren()) {

                  String specility=  zoneSnapshot.child("Spciality").getValue(String.class);
                    if (list.contains(specility)) {
                        System.out.println("Spciality found :"+specility);
                    } else {
                        list.add(specility);
                    }

                }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SelectDoctor.this, android.R.layout.simple_spinner_item, list);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner_specility.setAdapter(dataAdapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

        spinner_specility.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selected_specility = parent.getItemAtPosition(position).toString();
                add_doctors( selected_specility);
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        spinner_doctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                selected_doctor = parent.getItemAtPosition(position).toString();

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectDoctor.this, SelectConsaltation.class);
                Bundle b = new Bundle();
                b.putString("specility",selected_specility);
                b.putString("doctor",selected_doctor);
                b.putString("fee",fee);
               //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);

            }
        });

    }

    public void  add_doctors(String selected_specility)
    {



        doctor_ref.orderByChild("Spciality").equalTo(selected_specility).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> list = new ArrayList<String>();
                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    String firstName = datas.child("Firstname").getValue().toString();
                    String lastName = datas.child("Lastname").getValue().toString();
                     fee = datas.child("Doctor_fee").getValue().toString();
                    String doc_name=firstName+" "+lastName;
                    //Toast.makeText(getApplicationContext(), fee, Toast.LENGTH_SHORT).show();
                    list.add(doc_name);
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(SelectDoctor.this, android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_doctor.setAdapter(dataAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
