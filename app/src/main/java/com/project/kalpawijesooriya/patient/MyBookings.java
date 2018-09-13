package com.project.kalpawijesooriya.patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MyBookings extends AppCompatActivity {
    private DatabaseReference myrefdatabase;
    RecyclerView recyclerView;
    Toolbar toolbar = null;
    private FirebaseAuth mAuth;
    String userId;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private Toolbar mToolbar;
    private ShimmerFrameLayout mShimmerViewContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        mToolbar=(Toolbar)findViewById(R.id.promotion_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Bookings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_appoitments);
        mAuth= FirebaseAuth.getInstance();
        userId=mAuth.getCurrentUser().getUid();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myrefdatabase= FirebaseDatabase.getInstance().getReference().child("/Appointments");

        FirebaseRecyclerAdapter< Bookings,MyBookings.BlogViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter< Bookings,MyBookings.BlogViewHolder>(
                Bookings.class,
                R.layout.booking_card,
                MyBookings.BlogViewHolder.class,
                myrefdatabase
        ) {
            @Override
            protected void populateViewHolder(final MyBookings.BlogViewHolder viewHolder, final Bookings model, int position) {

                String patientId = model.getPatient_ID();
                final ViewGroup.LayoutParams params = viewHolder.book_rel.getLayoutParams();
                DatabaseReference consulRef = FirebaseDatabase.getInstance().getReference().child("consultation").child(model.getConsultationID());
                consulRef.child("DoctorID").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference().child("User").child("doctor").child((String) snapshot.getValue());

                        doctorRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String fname = dataSnapshot.child("Firstname").getValue(String.class);
                                String lname = dataSnapshot.child("Lastname").getValue(String.class);
                                String doctorname = fname + " " + lname;

                                viewHolder.setDoctor("DOCTOR : Dr." + doctorname);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                if (userId.equals(patientId)) {
                    params.height = 200;
                    params.width =700;
                    viewHolder.book_rel.setLayoutParams(params);
                    viewHolder.setDate("DATE : " + model.getDate());
                    viewHolder.setNumber("NUMBER : " + model.getNumber());
                    viewHolder.setTime("TIME : " + model.getTime());
                    final String appointment_id = getRef(position).getKey();
                    viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        Intent MyBookings=new Intent(MyBookings.this,BillImage.class);
                        MyBookings.putExtra("BillImage",model.getBillImage());
                        startActivity(MyBookings);
                        }
                    });
                }
                else {
                    params.height = 0;
                    params.width = 0;
                    viewHolder.book_rel.setLayoutParams(params);
                }
            }

        };
        recyclerView.setAdapter(recyclerAdapter);



    }


    @Override
    protected void onStart() {
        super.onStart();
        // mAuth.addAuthStateListener(mAuthListner);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mView;
        RelativeLayout book_rel;
        TextView txtdate;
        TextView txttime;
        TextView txtnumber;
        TextView txtdoctor;

        ImageView imageView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            book_rel=(RelativeLayout)itemView.findViewById(R.id.book_rel);
            txtdate = (TextView)itemView.findViewById(R.id.app_date);
            txttime = (TextView) itemView.findViewById(R.id.app_time);
            txtnumber = (TextView)itemView.findViewById(R.id.app_number);
            txtdoctor = (TextView) itemView.findViewById(R.id.app_doctor);

            imageView=(ImageView)itemView.findViewById(R.id.promotion_img);

        }

        public void setDate(String date)
        {
            txtdate.setText(date);
        }
        public void setNumber(String number)
        {
            txtnumber.setText(number);
        }
        public void setDoctor(String doctor)
        {
            txtdoctor.setText(doctor);
        }
        public void setTime(String time)
        {
            txttime.setText( time);
        }

//        public void setImage(String image)
//        {
//
//            Picasso.with(mView.getContext())
//                    .load(image)
//                    .into(imageView);
//        }



    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mShimmerViewContainer.startShimmerAnimation();
//    }
//
//    @Override
//    public void onPause() {
//        mShimmerViewContainer.stopShimmerAnimation();
//        super.onPause();
//    }


}
