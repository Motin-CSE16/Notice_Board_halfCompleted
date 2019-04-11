package com.motinsheikh.notice_board;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class UploadActivityResult extends AppCompatActivity {
    private Button selectfileid,uploadid,seeuploadednoticeid;
  //  private EditText enternameid;
    private TextView nofileselectid;
    Uri pdfUri;
    FirebaseStorage storage;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    ArrayList<String> urls=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_result);
        //enternameid=findViewById(R.id.enternameid);

        seeuploadednoticeid=findViewById(R.id.seeuploadednoticeid);
        seeuploadednoticeid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadActivityResult.this,My_Recycler_View_Activity.class));

            }
        });

        storage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();
        selectfileid=findViewById(R.id.selectfileid);
        uploadid=findViewById(R.id.uploadid);
        nofileselectid=findViewById(R.id.nofileselectedid);

        selectfileid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UploadActivityResult.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED)
                {
                    selectPdf();

                }
                else
                {
                    ActivityCompat.requestPermissions(UploadActivityResult.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
                }

            }
        });
        uploadid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pdfUri!=null)
                uploadfile(pdfUri);
                else
                    Toast.makeText(getApplicationContext(), "Please selsct a file", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadfile(Uri pdfUri) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading File....");
        progressDialog.setProgress(0);
        progressDialog.show();
        final String filename=System.currentTimeMillis()+".pdf";
        final String filename1=System.currentTimeMillis()+"";
        final StorageReference storageReference=storage.getReference();
        storageReference.child("UploadRnotice").child(filename).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               String url=taskSnapshot.getStorage().getDownloadUrl().toString();
                DatabaseReference reference=database.getReference();
                reference.child(filename1).setValue(url).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "File succesfully uploaded", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "File not uploaded successfully", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "File not uploaded successfully", Toast.LENGTH_SHORT).show();

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentprogress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentprogress);

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
          selectPdf();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please provide permission", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectPdf() {
        Intent intent=new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,86);

     /*  String filename=enternameid.getText().toString().trim();
        if (filename.isEmpty())
        {
              enternameid.setError("Please enter file name");
              enternameid.requestFocus();
              return;
        }*/



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==86 && resultCode==RESULT_OK && data!=null)
        {
         pdfUri=data.getData();
         nofileselectid.setText("A file is selected: "+data.getData().getLastPathSegment());
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }
}
