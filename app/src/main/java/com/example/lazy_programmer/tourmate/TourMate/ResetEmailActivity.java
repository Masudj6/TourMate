package com.example.lazy_programmer.tourmate.TourMate;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lazy_programmer.tourmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetEmailActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText resetEt;
    private Button btnResetEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tourmate_activity_reset_email);

        //popup window for adding event
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width=dm.widthPixels;
        int height=dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int) (height*.5));

        resetEt= (EditText) findViewById(R.id.resetEmailEt );

    }

    public void onResetEmail(View view) {


        firebaseAuth=FirebaseAuth.getInstance();
    final FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
        @Override
        public void onComplete(@NonNull Task task) {

            if (task.isSuccessful()) {
                Toast.makeText(ResetEmailActivity.this,
                        "Verification email sent to " + user.getEmail(),
                        Toast.LENGTH_SHORT).show();
                ResetEmailActivity.this.finish();
            } else {

                Toast.makeText(ResetEmailActivity.this,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    });


    }

}
