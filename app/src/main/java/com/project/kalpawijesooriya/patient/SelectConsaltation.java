package com.project.kalpawijesooriya.patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SelectConsaltation extends AppCompatActivity {
    private Toolbar mToolbar;
    private DatabaseReference con_ref;
    private TextView text_name,text_special;
    private DatabaseReference doctor_ref;
    private FirebaseAuth mAuth;
    private String specilitys = "";
    private Firebase mdatabase,mdatabasedoctor;
    private Firebase mdatabaseshedule;
    private String  name_day;
    private ProgressBar progressBar;
    private String doctor_id_from_doctor;
    private DatabaseReference Shedule_ref;
    private FirebaseAuth.AuthStateListener mAuthListner;
    RecyclerView recyclerView;
    String doctor;
    private  String consul_id;
    String doctorID,appointmentno;
    String shedulID,fee;
    String title,date;
    String today,room;
    private RelativeLayout con_rel;

    public SelectConsaltation() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_consaltation);

        mToolbar=(Toolbar)findViewById(R.id.consultation_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Appointments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        con_rel=(RelativeLayout)findViewById(R.id.con_rel);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        Shedule_ref=   FirebaseDatabase.getInstance().getReference().child("schedule");
        doctor_ref = FirebaseDatabase.getInstance().getReference().child("User").child("doctor");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_viewconsultation);
        mAuth= FirebaseAuth.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        text_name=(TextView)findViewById(R.id.doctor_name);
        text_special=(TextView)findViewById(R.id.doctor_special);



        Bundle b = getIntent().getExtras();


        if(b != null) {
            specilitys = b.getString("specility");
            doctor = b.getString("doctor");
            fee = b.getString("fee");
    }
        text_special.setText(specilitys);

        String[] parts = doctor.split(" ");
        String fname = parts[0];
        String lname = parts[1];


        doctor_ref.orderByChild("Firstname").equalTo(fname).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot datas : dataSnapshot.getChildren()) {
                    title = datas.child("Title").getValue().toString();
                    doctor_id_from_doctor = datas.child("DoctorID").getValue().toString();

                    text_name.setText("Dr.("+title+")"+" "+doctor);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       con_ref= FirebaseDatabase.getInstance().getReference().child("/consultation");
        FirebaseRecyclerAdapter< Consultation,SelectConsaltation.BlogViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter< Consultation,SelectConsaltation.BlogViewHolder>(
                Consultation.class,
                R.layout.consultation_card,
                SelectConsaltation.BlogViewHolder.class,
                con_ref
        ) {
            @Override
            protected void populateViewHolder(final SelectConsaltation.BlogViewHolder viewHolder, final Consultation model, final int Consultation) {

                Shedule_ref.child(model.getScheduleID()).child("Day").addValueEventListener(new ValueEventListener() {
                   ViewGroup.LayoutParams params = viewHolder.con_rel.getLayoutParams();
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        name_day = dataSnapshot.getValue(String.class);

                        if (doctor_id_from_doctor.equals( model.getDoctorID() )){

                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                            Date strDate = null;

                            try {
                                strDate = sdf.parse(model.getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if(System.currentTimeMillis()<=strDate.getTime() ) {
                                params.height = 300;
                                params.width =800;
                                viewHolder.con_rel.setLayoutParams(params);
                                viewHolder.setDate(model.getDate(),name_day);
                                viewHolder.setlastno(model.getLastAppoimentNo());
                                consul_id=getRef(Consultation).getKey();
                                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        getvariables(consul_id);
                                       // b.putString("consultatiomID",consul_id);
                                       // intent.putExtras(b); //Put your id to your next Intent
                                        //startActivity(intent);
                                    }
                                });
                            }
                            else {

                                params.height = 0;
                                params.width = 0;
                                viewHolder.con_rel.setLayoutParams(params);
                            }

                           }
                        else {
                            params.height = 0;
                            params.width = 0;
                            viewHolder.con_rel.setLayoutParams(params);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        };
        recyclerView.setAdapter(recyclerAdapter);


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

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView txtdate,appointNo;
        RelativeLayout con_rel;
        String name_day = "no name";
        private String appointment_date;


        public BlogViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            con_rel=(RelativeLayout)itemView.findViewById(R.id.con_rel);
            txtdate = (TextView)itemView.findViewById(R.id.day);
            appointNo = (TextView)itemView.findViewById(R.id.nextnumber);
        }

        public void setDate(String date, String name_day)
        {

            String[] parts = date.split("/");
            String month = parts[0];
            String day = parts[1];
            String year = parts[2];
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] months = dfs.getMonths();
            int month_no = Integer.parseInt(month)-1;
            if ( month_no >= 0 &&  month_no <= 11 ) {
                month = months[ month_no];
            }
            appointment_date= day+"th of "+ month+ " "+name_day+" "+ year;
            txtdate.setText( appointment_date);
        }
        public void setlastno(String number)
        {
            appointNo.setText("Next avealable number is "+Integer.toString((Integer.parseInt(number)+1)) );
        }





    }

    public void  getvariables(final String consul_id)
    {
        progressBar.setVisibility(View.VISIBLE);
        mdatabase = new Firebase("https://patient-management-syste-9758b.firebaseio.com/consultation/" + consul_id);
        mdatabase.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                date = map.get("Date");
                doctorID = map.get("DoctorID");
                 appointmentno=Integer.toString((Integer.parseInt(map.get("LastAppoimentNo"))+1));

                String nurse = map.get("Nurse");
                room = map.get("Room");
                shedulID = map.get("ScheduleID");
                Shedule_ref=   FirebaseDatabase.getInstance().getReference().child("schedule");
                Shedule_ref.child(shedulID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String starttime=  dataSnapshot.child("StartTime").getValue().toString();
                        String endtime=  dataSnapshot.child("EndTime").getValue().toString();
                        String patientNumber=  dataSnapshot.child("patientNumber").getValue().toString();
                        int patientnumberInt=Integer.parseInt(patientNumber);

                        String[] endparts = endtime.split(":");
                        int hourend = Integer.parseInt(endparts[0]);
                        int minsend = Integer.parseInt(endparts[1]);

                        String[] startparts = starttime.split(":");
                        int hoursatart = Integer.parseInt(startparts[0]);
                        int minstart = Integer.parseInt(startparts[1]);

                        int timestart=minstart+(hoursatart*60);
                        int timeend=minsend+(hourend*60);
                        int timeperpatient=(timeend-timestart)/patientnumberInt;

                        Intent intent=new Intent(SelectConsaltation.this,AppoinmentDetails.class);
                        Bundle b = new Bundle();
                        b.putString("date",date);
                        b.putString("room",room);
                        b.putString("fee",fee);
                        b.putString("docName", doctor);
                        b.putString("startTime",starttime);
                        b.putString("spciality",specilitys);
                        b.putString("timeperpatient", String.valueOf(timeperpatient));
                        b.putString("number",appointmentno );
                        b.putString("consulID",consul_id );

                        intent.putExtras(b);
                        progressBar.setVisibility(View.GONE);  //Put your id to your next Intent
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });



    }

}
