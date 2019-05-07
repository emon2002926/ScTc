package com.pluto.application;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Transaction;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class ImageI extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Button buttonChoose;
    private Button buttonUpload;
    private TextView mTextVIewshowUploads;
    private EditText mEditTExt;
    private ImageView imageView;
    private ProgressBar progressBar;

    private Uri mImageUri;

    private StorageReference mStorageReference;
    private DatabaseReference mDatabaseReference;
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_i);

        buttonChoose = findViewById(R.id.button_choose_Image);

        buttonUpload = findViewById(R.id.upload);

        mTextVIewshowUploads = findViewById(R.id.text_show_uploads);

        mEditTExt = findViewById(R.id.edit_text);

        imageView = findViewById(R.id.image);

        progressBar =  findViewById(R.id.progressBar);



        mStorageReference = FirebaseStorage.getInstance().getReference("upload");

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("uploads");




        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilechooser();

            }

        });
        mTextVIewshowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                openImagesActivity();

            }
        });
        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask !=null && mUploadTask.isInProgress()){
                    Toast.makeText(ImageI.this,"Upload in progress",Toast.LENGTH_SHORT).show();

                }else {
                    uploadFile();
                }



            }
        });

    }
    private void openFilechooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData() != null )
        {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(imageView);
        }

    }

    private String getFileExtension(Uri uri){


        ContentResolver cR  = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));



    }
    private  void uploadFile(){
        if (mImageUri != null){
            StorageReference fileReferance = mStorageReference.child( "uploads/" + System.currentTimeMillis()
                    +"." +getFileExtension(mImageUri));


            mUploadTask = fileReferance.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override



                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);

                        }
                    },500);

                    Toast.makeText(ImageI.this,"Upload successful",Toast.LENGTH_LONG).show();
                    upload upload = new upload(mEditTExt.getText().toString().trim(),
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().toString());


                    String  uploadId = mDatabaseReference.push().getKey();

                    mDatabaseReference.child(uploadId).setValue(upload);
                }

            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ImageI.this,e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    })



                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressBar.setProgress((int)progress);
                        }
                    });




        }else {
            Toast.makeText(this,"No file selected", Toast.LENGTH_SHORT).show();
        }


    }
    private  void openImagesActivity(){
        Intent intent = new Intent(this,ImageRetrew.class);
        startActivity(intent);
    }
}
