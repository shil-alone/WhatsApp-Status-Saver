package com.codershil.whatsappstatussaver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class VideoActivity extends AppCompatActivity {
    private ImageView download, myChatApp, btnShare;
    private VideoView particularVideo;
    String destinationPathString, filePathString, uriString, fileName;
    File destinationPath, filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent = getIntent();
        destinationPathString = intent.getStringExtra("DEST_PATH_VIDEO");
        filePathString = intent.getStringExtra("FILE_VIDEO");
        uriString = intent.getStringExtra("URI_VIDEO");
        fileName = intent.getStringExtra("FILENAME_VIDEO");
        destinationPath = new File(destinationPathString);
        filePath = new File(filePathString);
        initializeView();

    }

    private void initializeView() {
        getSupportActionBar().setTitle("Video");

        particularVideo = findViewById(R.id.particularVideo);
        download = findViewById(R.id.download);
        myChatApp = findViewById(R.id.myChatApp);
        btnShare = findViewById(R.id.share);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VideoActivity.this, "share is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(particularVideo);
        Uri uri1 = Uri.parse(uriString);
        particularVideo.setMediaController(mediaController);
        particularVideo.setVideoURI(uri1);
        particularVideo.requestFocus();
        particularVideo.start();

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilesDao.downloadStatus(VideoActivity.this, filePath, destinationPath, fileName, destinationPathString);
                Dialog dialog = new Dialog(VideoActivity.this);
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