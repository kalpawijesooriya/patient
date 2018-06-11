package com.project.kalpawijesooriya.patient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.Toast;

public class DisplayPastAppointments extends AppCompatActivity {
String map;
Bitmap bitmap;
ImageView bill;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_past_appointments);

        bill =(ImageView)findViewById(R.id.bill);
        Bundle b = getIntent().getExtras();
        if (b != null) {

            map=b.getString("bitmap");

            try{
                byte [] encodeByte= Base64.decode(map, Base64.DEFAULT);
                bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                bill.setImageBitmap(bitmap);

            }catch(Exception e){
                e.getMessage();

            }


        }


    }
}
