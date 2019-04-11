package com.motinsheikh.notice_board;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText sign_inemailid,sign_inpasswodid;
    private Button sign_inbuttonid;
    private TextView sign_upTextviewid;
    private TextView forgetpasswordid;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        sign_inemailid = findViewById(R.id.sign_inemailid);
        sign_inpasswodid = findViewById(R.id.sign_inpasswordid);
        sign_inbuttonid= findViewById(R.id.sign_inbuttonid);
        sign_upTextviewid = findViewById(R.id.sign_upTextviewid);
        progressBar = findViewById(R.id.sign_inprogressbarid);
        forgetpasswordid = findViewById(R.id.forgetpasswordid);

        sign_inbuttonid.setOnClickListener(this);
        sign_upTextviewid.setOnClickListener(this);
        forgetpasswordid.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
           {
               case R.id.sign_inbuttonid:
                   userLogin();
                   break;

               case R.id.sign_upTextviewid:
                   Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                   startActivity(intent);
                   break;
               case R.id.forgetpasswordid:
                   Intent intent1 = new Intent(getApplicationContext(),ForgetPasswordActivity.class);
                   startActivity(intent1);




               }


    }

    private void userLogin() {
        String email = sign_inemailid.getText().toString().trim();
        String password = sign_inpasswodid.getText().toString().trim();
        if(email.isEmpty())
        {
            sign_inemailid.setError("Enter your email address");
            sign_inemailid.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            sign_inemailid.setError("Enter a Valid email address");
            sign_inemailid.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            sign_inpasswodid.setError("Enter your password");
            sign_inpasswodid.requestFocus();
            return;
        }
        if(password.length()<6)
        {
           sign_inpasswodid.setError("Minimum length of password should be 6");
            sign_inpasswodid.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {
                   /* finish();
                    Intent intent = new Intent(getApplicationContext(),PostActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                   checkEmailVerification();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Login is Unsuccessful", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
    private void checkEmailVerification()
    {
        FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();
        if(emailflag)
        {
            finish();
            Intent intent = new Intent(getApplicationContext(),PostActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Verify your Email", Toast.LENGTH_SHORT).show();
           mAuth.signOut();
        }
    }
}
