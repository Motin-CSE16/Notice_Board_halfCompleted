package com.motinsheikh.notice_board;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class PostActivity extends AppCompatActivity {
   private FirebaseAuth mAuth;
   String[] NoticeType;
   private Spinner spinnerid;
   private TextView textView;
   private Button uploadid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mAuth = FirebaseAuth.getInstance();

        NoticeType=getResources().getStringArray(R.array.Notice_type);
        spinnerid=findViewById(R.id.spinnerid);
        uploadid = findViewById(R.id.uploadid);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,R.layout.spinner_view,R.id.spinnerviewid,NoticeType);
        spinnerid.setAdapter(adapter);

        uploadid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = spinnerid.getSelectedItem().toString();
                if (value.contains("Result_Notice"))
                {
                    Intent intent = new Intent(getApplicationContext(),UploadActivityResult.class);
                    startActivity(intent);

                }
                else if (value.contains("Official_Notice"))
                {
                    Intent intent = new Intent(getApplicationContext(),UploadActivityOfficial.class);
                    startActivity(intent);

                }
                else if (value.contains("Others_Notice"))
                {
                    Intent intent = new Intent(getApplicationContext(),UploadActivityOthers.class);
                    startActivity(intent);

                }



            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout2,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.Signout_id)
        {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
