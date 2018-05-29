package com.project.kalpawijesooriya.patient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class MedAlarm extends AppCompatActivity {
    private Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_alarm);

        mToolbar=(Toolbar)findViewById(R.id.MedAlarm_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Med Alarm");
    }
}
