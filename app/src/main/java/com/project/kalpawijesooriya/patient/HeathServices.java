package com.project.kalpawijesooriya.patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class HeathServices extends AppCompatActivity {
   private RelativeLayout btn_chat;
    private RelativeLayout btn_appointment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heath_services);

        btn_chat=(RelativeLayout)findViewById(R.id.chat);
        btn_appointment=(RelativeLayout)findViewById(R.id.appointment_layout);

        btn_chat.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(HeathServices.this,Chat.class));
             }
         });

        btn_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HeathServices.this,SelectDoctor.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.heath_center_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()==R.id.old_bills_btn)
        {
            startActivity(new Intent(HeathServices.this,MyBookings.class));
        }
        return true;
    }
}
