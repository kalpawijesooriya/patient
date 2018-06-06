package com.project.kalpawijesooriya.patient;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;


public class AppoinmentDetails extends AppCompatActivity {
    private Toolbar mToolbar;
    private String consultationID;
    private FirebaseAuth mAuth;
   private StorageReference ref;
   private Button  confirm;
   String number,timeperpatient;
   String Totalfee;
    FirebaseStorage storage;
    StorageReference storageReference;
    private String consulID;
    private Firebase mdatabase;
    private Firebase doctorref;
    ImageView imageView4,paid;
    private Firebase sheduleref;
   private TextView docName,special,datetext,startTime,apxTime,doc_fee,hos_fee,appointmentNo,roomNo,totalfee;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinment_details);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imageView4=(ImageView)findViewById(R.id.imageView4);
        mToolbar = (Toolbar) findViewById(R.id.consultation_page_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar = (Toolbar) findViewById(R.id.consultation_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Add Appointments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAuth = FirebaseAuth.getInstance();
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
        Bundle b = getIntent().getExtras();
        paid.setVisibility(View.GONE);

        if (b != null) {

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


     confirm.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             paid.setVisibility(View.VISIBLE);
             confirm.setVisibility(View.GONE);
             View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
             Bitmap bitmap=  takeScreenshot();
             imageView4.setImageBitmap(bitmap);
             Intent intent = new Intent(AppoinmentDetails.this, Payments.class);
             Bundle b = new Bundle();
             b.putString("total",Totalfee);

             b.putString("timeperpatient",timeperpatient);
             b.putString("number",number );
             b.putString("consulID",consulID);
             intent.putExtras(b);
             startActivity(intent);
             finish();

           //  store(bitmap, "DCIM");


         }
     });

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
    public static Bitmap getScreenShot(View view) {
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(700, 1000, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(-100, -90, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;

    }

    private Bitmap takeScreenshot() {

        Bitmap bitmap = null;

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
            u.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
           
        } catch (Throwable e) {

            e.printStackTrace();
        }
        return bitmap;
    }


}
