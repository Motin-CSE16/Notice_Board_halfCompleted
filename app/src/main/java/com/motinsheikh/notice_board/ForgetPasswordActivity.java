package com.motinsheikh.notice_board;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    private EditText forgetpasswordemailid;
    private Button resetpassworbuttonid;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        firebaseAuth = FirebaseAuth.getInstance();

        forgetpasswordemailid = (EditText)findViewById(R.id.forgetpasswordemailid);
        resetpassworbuttonid = (Button)findViewById(R.id.resetpassworbuttonid);

        resetpassworbuttonid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = forgetpasswordemailid.getText().toString().trim();
                if(email.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter your registered email ID", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgetPasswordActivity.this, "Password reset email set !", Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(ForgetPasswordActivity.this, "Error in sending Password reset Email !", Toast.LENGTH_SHORT).show();
                            }
                            
                        }
                    });
                }

            }
        });
    }
}
