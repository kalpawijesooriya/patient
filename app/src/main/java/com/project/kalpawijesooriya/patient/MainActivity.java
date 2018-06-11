package com.project.kalpawijesooriya.patient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
private RelativeLayout promotion,alarm,tracker,services,advices;
    private Toolbar mToolbar;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private String userId;
    private DatabaseReference myrefdatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
              mToolbar=(Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Viweka Hospital");

        String tocan= FirebaseInstanceId.getInstance().getToken();
        System.out.print(tocan);
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                }
            }
        };


        promotion=(RelativeLayout)findViewById(R.id.relativeLayout_promotion);//promotion button
        advices=(RelativeLayout)findViewById(R.id.relativeLayout_heathadvice);//heathadvice button
        services=(RelativeLayout)findViewById(R.id.relativeLayout_heathcenter);//heathcenter button
        alarm=(RelativeLayout)findViewById(R.id.relativeLayout_medalarm);//medalarm button
        tracker=(RelativeLayout)findViewById(R.id.relativeLayout_heathtracker);//heathtracker button
        if(auth.getCurrentUser()!=null) {
            myrefdatabase = FirebaseDatabase.getInstance().getReference("User").child("patient").child(auth.getCurrentUser().getUid());
         }
        //goto promotion page
        promotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(MainActivity.this,Promotion.class));
            }
        });

        //goto heath advices class
        advices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(MainActivity.this,HeathAdvices.class));
                userId=auth.getCurrentUser().getUid();

//                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//                DatabaseReference userIdRef = rootRef.child("User").child(userId);
//                Toast.makeText(getApplicationContext(), userIdRef.getRef().getParent().getKey().toString(), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this,HeathAdvices.class));

            }
        });

        //goto heath advices class
        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,HeathServices.class));
            }
        });

        //goto HeathTracker class
        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MedAlarm.class));
            }
        });

        //goto MedAlarm class
        tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,HeathTracker.class));
            }
        });



    }

    public void signOut() {
        auth.signOut();
    }




    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
        FirebaseUser currentuser=auth.getCurrentUser();
        if(currentuser==null)
        {


        }
        else{
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
          //  myrefdatabase.child("online").setValue("true");
            }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
        FirebaseUser currentuser=auth.getCurrentUser();
        if(currentuser!=null)
        {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
           // myrefdatabase.child("online").setValue(ServerValue.TIMESTAMP);


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
          if (item.getItemId()==R.id.main_logout_btn)
           {
                  signOut();
            }
        return true;
    }


}
