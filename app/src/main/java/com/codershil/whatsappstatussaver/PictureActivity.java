package com.codershil.whatsappstatussaver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

public class PictureActivity extends AppCompatActivity {
    private ImageView particularImage, download, myChatApp, btnShare;
    String destinationPathString , filePathString,uriString,fileName;
    File destinationPath ,filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        // getting data from main activity
        Intent intent = getIntent();
        destinationPathString = intent.getStringExtra("DEST_PATH");
        filePathString = intent.getStringExtra("FILE");
        uriString = intent.getStringExtra("URI");
        fileName = intent.getStringExtra("FILENAME");
        destinationPath = new File(destinationPathString);
        filePath = new File(filePathString);

        initializeViews();
    }


    private void initializeViews(){
        getSupportActionBar().setTitle("Picture");

        particularImage = findViewById(R.id.particularImage);
        download = findViewById(R.id.download);
        myChatApp = findViewById(R.id.myChatApp);
        btnShare = findViewById(R.id.share);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PictureActivity.this, "share is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        Glide.with(this).load(uriString).into(particularImage);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilesDao.downloadStatus(PictureActivity.this,filePath,destinationPath,fileName,destinationPathString);
                Dialog dialog = new Dialog(PictureActivity.this);
                dialog.setContentView(R.layout.ok_dialog);
                dialog.show();
                Button button = dialog.findViewById(R.id.okButton);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}