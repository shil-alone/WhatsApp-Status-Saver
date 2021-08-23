package com.codershil.whatsappstatussaver;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements StatusClickListener {

    private final int requestCode = 1;
    private StatusAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<StatusModel> filesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
    }

    // setting up layout
    private void initializeLayout() {
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        // checking if statuses directory exists or not
        String statusDirectory = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME + "Media/.Statuses";
        File file = new File(statusDirectory);
        if (!file.isDirectory()) {
            Toast.makeText(MainActivity.this, "statuses not found", Toast.LENGTH_LONG).show();
            return;
        }

        // setting up recyclerview
        filesList = FilesDao.getStatusFilesList();
        adapter = new StatusAdapter(MainActivity.this, filesList, this);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        // adding listener to refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                filesList = FilesDao.getStatusFilesList();
                adapter.updateStatusData(filesList);
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 700);
                }
            }
        });

    }

    // method for checking storage permissions
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                initializeLayout();
            } else {
                Toast.makeText(MainActivity.this, "please allow given permission", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            }

        } else {
            // no need to ask permission in case of api level less than 23
            initializeLayout();
        }
    }


    // method for handling clicks on status inside recyclerview
    @Override
    public void onStatusClicked(StatusViewHolder statusViewHolder) {
        StatusModel statusModel = filesList.get(statusViewHolder.getAdapterPosition());
        if (statusModel.getUri().toString().endsWith(".mp4")) {
            String destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SAVE_FOLDER_NAME;
            Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
            intent.putExtra("DEST_PATH_VIDEO", destPath);
            intent.putExtra("FILE_VIDEO", statusModel.getPath());
            intent.putExtra("FILENAME_VIDEO", statusModel.getFilename());
            intent.putExtra("URI_VIDEO", statusModel.getUri().toString());
            startActivity(intent);
        } else {
            String destPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.SAVE_FOLDER_NAME;
            Intent intent = new Intent(getApplicationContext(), PictureActivity.class);
            intent.putExtra("DEST_PATH", destPath);
            intent.putExtra("FILE", statusModel.getPath());
            intent.putExtra("FILENAME", statusModel.getFilename());
            intent.putExtra("URI", statusModel.getUri().toString());
            startActivity(intent);
        }

    }
}