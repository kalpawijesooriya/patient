package com.project.kalpawijesooriya.patient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HeathAdvices extends AppCompatActivity {
    private Toolbar mToolbar;
    private DatabaseReference myrefdatabase;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private ShimmerFrameLayout mShimmerViewContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heath_advices);

        mToolbar=(Toolbar)findViewById(R.id.promotion_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Heath Advices");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_viewmem);
        mAuth= FirebaseAuth.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myrefdatabase= FirebaseDatabase.getInstance().getReference().child("/Advice");
        FirebaseRecyclerAdapter< Advices,HeathAdvices.BlogViewHolder> recyclerAdapter=new FirebaseRecyclerAdapter< Advices,HeathAdvices.BlogViewHolder>(
                Advices.class,
                R.layout.promotion_card,
                HeathAdvices.BlogViewHolder.class,
                myrefdatabase
        ) {
            @Override
            protected void populateViewHolder(HeathAdvices.BlogViewHolder viewHolder,  Advices model, int position) {
                viewHolder.settitle(model.gettitle());
                viewHolder.setdiscription(model.getdiscription());
                viewHolder.setImage(model.getImage());
                final String advice_id=getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent detaisIntent=new Intent(HeathAdvices.this,AdviceDetails.class);
                        detaisIntent.putExtra("Advice_ID",advice_id);
                        startActivity(detaisIntent);
                    }
                });
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
        TextView txttitle;
        TextView txtdiscription;

        ImageView imageView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            txttitle = (TextView)itemView.findViewById(R.id.title);
            txtdiscription = (TextView) itemView.findViewById(R.id.discription);

            imageView=(ImageView)itemView.findViewById(R.id.promotion_img);

        }

        public void settitle(String title)
        {
            txttitle.setText(title);
        }
        public void setdiscription(String discription)
        {
            txtdiscription.setText(discription);
        }

        public void setImage(String image)
        {

            Picasso.with(mView.getContext())
                    .load(image)
                    .into(imageView);
        }



    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

}
