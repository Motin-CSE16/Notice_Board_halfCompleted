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

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText sign_upemailid,sign_uppassword;
    private Button sign_upbuttonid;
    private TextView sign_inTextviewid;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.sign_upprogressbarid);

        sign_upemailid = findViewById(R.id.sign_upemailid);
        sign_uppassword = findViewById(R.id.sign_uppasswordid);
        sign_upbuttonid = findViewById(R.id.sign_upbuttonid);
        sign_inTextviewid = findViewById(R.id.sign_inTextviewid);

        sign_upbuttonid.setOnClickListener(this);
        sign_inTextviewid.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.sign_upbuttonid:
                userRegister();
                break;

            case R.id.sign_inTextviewid:
                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void userRegister() {
        String email = sign_upemailid.getText().toString().trim();
        String password = sign_uppassword.getText().toString().trim();
        if(email.isEmpty())
        {
            sign_upemailid.setError("Enter your email address");
            sign_upemailid.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            sign_upemailid.setError("Enter a Valid email address");
            sign_upemailid.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            sign_uppassword.setError("Enter your password");
            sign_uppassword.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            sign_uppassword.setError("Minimum length of password should be 6");
            sign_uppassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            //Toast.makeText(getApplicationContext(), "Register is successful", Toast.LENGTH_SHORT).show();
                            sendEmailVerification();

                        }  /*else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                            {
                                finish();
                                Intent intent = new Intent(getApplicationContext(),PostActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }*/ else {
                            Toast.makeText(getApplicationContext(), "Error : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }


                    }

                //}
                });

    }
    private void sendEmailVerification()
    {
        FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
        if(firebaseUser != null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getApplicationContext(), "Successfully Registered,Verification mail has been sent !", Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        finish();
                        Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Verification mail hasn't been sent !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}
