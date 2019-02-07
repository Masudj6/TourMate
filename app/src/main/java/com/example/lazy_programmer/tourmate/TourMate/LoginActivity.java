package com.example.lazy_programmer.tourmate.TourMate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lazy_programmer.tourmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText emailEt,passEt;
    private TextView resetEmailtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourmate_activity_login);
        emailEt= (EditText) findViewById(R.id.usernameEt);
        passEt= (EditText) findViewById(R.id.passworEt);
        resetEmailtv= (TextView) findViewById(R.id.forgetPassTv);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));


    }

    public void loginPressed(View view) {
        String email=emailEt.getText().toString().trim();
        String pass=passEt.getText().toString().trim();

        if(!email.isEmpty() && !pass.isEmpty()){
            firebaseAuth=FirebaseAuth.getInstance();
            final ProgressDialog loadingBar=new ProgressDialog(this);
            loadingBar.setMessage("please wait...");
            loadingBar.show();
            firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    loadingBar.dismiss();
                    if (task.isSuccessful()){
                        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                        if(user.isEmailVerified()){
                            startActivity(new Intent(LoginActivity.this,TourMateHomePage.class));
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "Email is not varified", Toast.LENGTH_SHORT).show();
                        }

                            

                    }else{
                        Toast.makeText(LoginActivity.this, ""+task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else{
            if(email.isEmpty())
                emailEt.setError("Enter your email");
            if(pass.isEmpty())
                passEt.setError("wrong password");
        }



    }
    @Override
    public void onBackPressed() {
            this.finish();

    }

    public void signupPressed(View view) {
        this.finish();
        startActivity(new Intent(this,SignupActivity.class));
    }

    public void resetUserPass(View view) {
       startActivity(new Intent(this,ResetEmailActivity.class));
    }
}
