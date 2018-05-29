package com.project.kalpawijesooriya.patient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Payments extends AppCompatActivity {
    TextView amout;
    String con_ID;
    String time;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        Bundle b = getIntent().getExtras();
        amout = (TextView) findViewById(R.id.amount);


        if (b != null) {

            amout.setText(b.getString("total")+".00");
            con_ID=b.getString("consulID");
            time=b.getString("number");
            number= b.getString("timeperpatient");
        }
    }
}
