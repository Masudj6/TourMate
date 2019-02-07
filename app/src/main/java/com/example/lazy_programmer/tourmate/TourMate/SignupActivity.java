package com.example.lazy_programmer.tourmate.TourMate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lazy_programmer.tourmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private DatabaseReference dbReference;
    private EditText fullNameEt,emailEt,phoneEt,passEt,confirmPassEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourmate_activity_signup);
        firebaseAuth=FirebaseAuth.getInstance();

        fullNameEt= (EditText) findViewById(R.id.fullNameEt);
        emailEt= (EditText) findViewById(R.id.regEmailEt);
        phoneEt= (EditText) findViewById(R.id.phoneEt);
        passEt= (EditText) findViewById(R.id.regPassworEt);
        confirmPassEt= (EditText) findViewById(R.id.confirmPasswordEt);


        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));




    }

    public void submitRegInfo(View view) {
        final String fullName=fullNameEt.getText().toString().trim();
        final String email=emailEt.getText().toString().trim();
        final String phone=phoneEt.getText().toString().trim();
        final String pass=passEt.getText().toString().trim();

        if(!fullName.isEmpty() && !email.isEmpty() && !phone.isEmpty() && !pass.isEmpty()){
            if(!pass.equals(confirmPassEt.getText().toString().trim()))
                confirmPassEt.setError("Password is not matched");
            else{
                final ProgressDialog loading=new ProgressDialog(this);
                loading.setMessage("Creating profile...");
                loading.show();
                firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String uid=firebaseAuth.getCurrentUser().getUid().toString();

                            sendVerificationEmail();

                            dbReference= FirebaseDatabase.getInstance().getReference();

                            HashMap<String,String> dataMap=new HashMap<String, String>();
                            dataMap.put("Name",fullName);
                            dataMap.put("Email",email);
                            dataMap.put("Phone",phone);
                            dataMap.put("Pass",pass);
                            dbReference.child("users").child(uid).child("Profile").setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        loading.dismiss();
                                        SignupActivity.this.finish();
                                        //Toast.makeText(RegistrationActivity.this, "Profile created", Toast.LENGTH_SHORT).show();
                                    } else{
                                        loading.dismiss();
                                        Toast.makeText(SignupActivity.this, ""+task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }else
                            Toast.makeText(SignupActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });

            }

        }else{
            if(fullName.isEmpty()){
                fullNameEt.setError("Enter your full name");
            }
            if(email.isEmpty()){
                emailEt.setError("Enter your email");
            }
            if(phone.isEmpty()){
                phoneEt.setError("Enter your phone number");
            }
            if(pass.isEmpty()){
                passEt.setError("Enter your pass");
            }
            if(confirmPassEt.getText().toString().isEmpty())
                confirmPassEt.setError("Enter your pass again");
        }

    }

    private void sendVerificationEmail() {
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    Toast.makeText(SignupActivity.this, "Check your Email for verification", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        startActivity(new Intent(this,LoginActivity.class));
    }
}
