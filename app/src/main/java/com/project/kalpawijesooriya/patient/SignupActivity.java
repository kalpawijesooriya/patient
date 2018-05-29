package com.project.kalpawijesooriya.patient;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword,fname,lname,address,mobNo,poNo;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String homeAddress,BD,TITLE,Province,Gender,Marry;
    private String poneNo;
    private String mobileNo;
    private EditText birthday;
    private Calendar myCalendar;
    private DatabaseReference mDatabase;
    private DatePickerDialog.OnDateSetListener date;
    private Spinner title,pro;
    private RadioGroup radioGroupgender;
    private RadioButton radioButtongender;
    private RadioGroup radioGroupmarry;
    private RadioButton radioButtonmarry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        address = (EditText) findViewById(R.id.address);
        mobNo = (EditText) findViewById(R.id.mobile);
        poNo = (EditText) findViewById(R.id.phone);
        birthday= (EditText) findViewById(R.id.Birthday);
        myCalendar = Calendar.getInstance();
        title=(Spinner)findViewById(R.id.title);
        pro=(Spinner)findViewById(R.id.province);
        radioGroupgender = (RadioGroup) findViewById(R.id.gender);
        radioGroupmarry = (RadioGroup) findViewById(R.id.marry);


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        birthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(SignupActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroupgender.getCheckedRadioButtonId();
                int selectedIdmarry = radioGroupmarry.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                 radioButtongender = (RadioButton) findViewById(selectedId);
                 radioButtonmarry = (RadioButton) findViewById(selectedIdmarry);
                 Marry=radioButtonmarry.getText().toString().trim();
                 Gender= radioButtongender.getText().toString().trim();
                 email = inputEmail.getText().toString().trim();
                 password = inputPassword.getText().toString().trim();
                 firstName = fname.getText().toString().trim();
                 lastName = lname.getText().toString().trim();
                 homeAddress = address.getText().toString().trim();
                 poneNo= poNo.getText().toString().trim();
                 mobileNo=  mobNo.getText().toString().trim();
                  BD=birthday.getText().toString().trim();
                 TITLE =  title.getSelectedItem().toString().trim();
                 Province=  pro.getSelectedItem().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
                                    String uid=currentUser.getUid();
                                    mDatabase=FirebaseDatabase.getInstance().getReference().child("User").child("patient").child(uid);
                                    HashMap<String,String> usermap=new HashMap<>();
                                    usermap.put("Email",email);
                                    usermap.put("ID",uid);
                                    usermap.put("Password",password);
                                    usermap.put("LastName",lastName);
                                    usermap.put("FirstName",firstName);
                                    usermap.put("Address",homeAddress);
                                    usermap.put("PhoneNo",poneNo);
                                    usermap.put("MobileNo",mobileNo);
                                    usermap.put("BirthDay",BD);
                                    usermap.put("Title",TITLE );
                                    usermap.put("Province",Province);
                                    usermap.put("Gender",Gender);
                                    usermap.put("image","https://image.flaticon.com/icons/png/128/149/149071.png");
                                    usermap.put("Marry",Marry);mDatabase.setValue(usermap);

                                    mDatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override

                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                startActivity(new Intent(SignupActivity.this,ProfileImage.class));
                                                finish();
                                            }
                                        }
                                    });

                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        birthday.setText(sdf.format(myCalendar.getTime()));
    }


}