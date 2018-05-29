package com.project.kalpawijesooriya.patient;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PromotionDetails extends AppCompatActivity {
private TextView topic,details;
private ImageView image_dis;
private DatabaseReference mref;
private ProgressDialog progess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_details);
        String id=getIntent().getStringExtra("Promotion_ID");
        mref= FirebaseDatabase.getInstance().getReference().child("Promotion").child(id);



        topic=(TextView)findViewById(R.id.pro_deta_title);
        details=(TextView)findViewById(R.id.promodetais);
        image_dis=(ImageView)findViewById(R.id.promodetails_img);
        topic.setText(id);
        progess=new ProgressDialog(this);
        progess.setTitle("Lodding..");
        progess.setMessage("Lodding Data Plase wait..");
        progess.setCanceledOnTouchOutside(false);
        progess.show();

        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String title=dataSnapshot.child("title").getValue().toString();
                String detals=dataSnapshot.child("details").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();
                topic.setText(title);
                details.setText(detals);
                Picasso.with(PromotionDetails.this).load(image).into( image_dis);
                progess.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
