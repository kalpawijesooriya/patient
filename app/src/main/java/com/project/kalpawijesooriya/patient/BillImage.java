package com.project.kalpawijesooriya.patient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class BillImage extends AppCompatActivity {
String imageUrl;
ImageView billImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_image);
        Bundle b = getIntent().getExtras();
        billImage =(ImageView)findViewById(R.id.billImage);

        if(b != null) {
            imageUrl = b.getString("BillImage");


        }

        Picasso.with(BillImage.this).load(imageUrl).into(billImage);
    }
}
