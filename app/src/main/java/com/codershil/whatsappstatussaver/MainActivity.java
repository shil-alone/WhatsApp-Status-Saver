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
        initializeLayout();
        checkPermission();
    }

    // setting up layout
    private void initializeLayout() {
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        recyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        adapter = new StatusAdapter(MainActivity.this, FilesDao.getStatusFilesList(), this);
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
        if (Build.VERSION.SDK_INT > 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                filesList = FilesDao.getStatusFilesList();
                adapter.updateStatusData(filesList);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
            }

        } else {
            Toast.makeText(this, "no need to ask permission", Toast.LENGTH_SHORT).show();
            filesList = FilesDao.getStatusFilesList();
            adapter.updateStatusData(filesList);
        }
    }


    // method for handling clicks on status inside recyclerview
    @Override
    public void onStatusClicked(int position, StatusViewHolder statusViewHolder) {
        StatusModel statusModel = filesList.get(position);
        if (statusModel.getUri().toString().endsWith(".mp4")) {
            statusViewHolder.getPlay().setVisibility(View.VISIBLE);
        } else {
            statusViewHolder.getPlay().setVisibility(View.INVISIBLE);
        }
        Glide.with(getApplicationContext()).load(statusModel.getUri()).into(statusViewHolder.getMainStatus());

        statusViewHolder.getMainStatus().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }
}