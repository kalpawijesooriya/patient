package com.project.kalpawijesooriya.patient;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;


public class AppoinmentDetails extends AppCompatActivity {
    private Toolbar mToolbar;
   private Button  confirm;
   String number,timeperpatient;
   String Totalfee;
    Bundle b;
    private ProgressBar progressBar;
    private String consulID;
    ImageView imageView4,paid;
    private boolean reloadNedeed = true;
    Bitmap bitmap = null;
   private TextView docName,special,datetext,startTime,apxTime,doc_fee,hos_fee,appointmentNo,roomNo,totalfee;
    private final int FIVE_SECONDS = 0;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinment_details);
        imageView4=(ImageView)findViewById(R.id.imageView4);
        mToolbar = (Toolbar) findViewById(R.id.consultation_page_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar = (Toolbar) findViewById(R.id.consultation_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Appointments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        special = (TextView) findViewById(R.id.special);
        datetext = (TextView) findViewById(R.id.date);
        docName = (TextView) findViewById(R.id.docName);
        doc_fee = (TextView) findViewById(R.id.docFee);
        startTime = (TextView) findViewById(R.id.startTime);
        hos_fee = (TextView) findViewById(R.id.hosFee);
        apxTime = (TextView) findViewById(R.id.aprxTime);
        appointmentNo = (TextView) findViewById(R.id.appointmentNO);
        roomNo = (TextView) findViewById(R.id.roomNO);
        confirm=(Button)findViewById(R.id.confirm);
        totalfee=(TextView)findViewById(R.id.totalFee);
        paid=(ImageView)findViewById(R.id.paid);
         b = getIntent().getExtras();
        paid.setVisibility(View.GONE);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (b != null) {

            load(  b);


        }


     confirm.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             changevisibility();

         }
     });

    }
    private void load( Bundle b)
    {
        roomNo.setText(b.getString("room"));
        datetext.setText(b.getString("date"));
        doc_fee.setText("Rs."+b.getString("fee")+".00");
        startTime.setText( b.getString("startTime"));
        docName.setText(b.getString("docName"));
        special.setText(b.getString("spciality"));
        apxTime.setText(b.getString("timeperpatient"));
        appointmentNo.setText(b.getString("number"));
        totalfee.setText(b.getString("fee")+".00");
        consulID =b.getString("consulID");
        Totalfee=b.getString("fee");
        timeperpatient= b.getString("timeperpatient");
        number= b.getString("number");
    }
    @Override
    public void onResume() {
        super.onResume();

        if (this.reloadNedeed)
            this.load( b);

        this.reloadNedeed = false; // do not reload anymore, unless I tell you so...
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


    private void takeScreenshot() {



        try {

            String mPath = Environment.getExternalStorageDirectory().toString() + "/"  + ".jpg";

            View u = findViewById(R.id.rel);
            u.setDrawingCacheEnabled(true);
            RelativeLayout z = (RelativeLayout) findViewById(R.id.rel);
            //z.setBackgroundColor(getResources().getColor(R.color.white));
            int totalHeight = z.getChildAt(0).getHeight();
            int totalWidth = z.getChildAt(0).getWidth();
            u.layout(0, 0, totalWidth, totalHeight);
            u.buildDrawingCache(true);
            bitmap = Bitmap.createBitmap(u.getDrawingCache());


        } catch (Throwable e) {

            e.printStackTrace();
        }

        uploadImage(bitmap);
    }
    public void uploadImage(final Bitmap bitmap) {
        progressBar.setVisibility(View.VISIBLE);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
        final String uid=currentUser.getUid();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://patient-management-syste-9758b.appspot.com");
        StorageReference imagesRef = storageRef.child("appointments/"+consulID+"/"+uid+".jpg");

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Intent intent = new Intent(AppoinmentDetails.this, Payments.class);
                Bundle b = new Bundle();
                b.putString("total",Totalfee);
                b.putString("timeperpatient",timeperpatient);
                b.putString("number",number );
                b.putString("consulID",consulID);
                b.putString("Bill",downloadUrl.toString());

                ByteArrayOutputStream baos=new  ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] arr=baos.toByteArray();
                String result= Base64.encodeToString(arr, Base64.DEFAULT);

                b.putString("bitmap",result);
                intent.putExtras(b);
                progressBar.setVisibility(View.GONE);
                startActivity(intent);
                finish();
            }
        });
    }
    private void changevisibility()
    {
        confirm.setVisibility(View.GONE);
        paid.setVisibility(View.VISIBLE);
        handler.postDelayed(new Runnable() {
            public void run() {

                takeScreenshot();
                confirm.setVisibility(View.VISIBLE);
                paid.setVisibility(View.GONE);

            }
        }, FIVE_SECONDS);

    }

}
