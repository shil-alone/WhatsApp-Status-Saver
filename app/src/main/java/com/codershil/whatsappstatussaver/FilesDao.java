package com.codershil.whatsappstatussaver;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FilesDao {

    // method for getting in the form of statusModel
    public static ArrayList<StatusModel> getStatusFilesList() {
        ArrayList<StatusModel> filesList = new ArrayList<>();
        StatusModel statusModel;
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constants.FOLDER_NAME + "Media/.Statuses";
        File targetDirectory = new File(targetPath);
        File[] files = targetDirectory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            statusModel = new StatusModel();
            statusModel.setUri(Uri.fromFile(file));
            statusModel.setPath(file.getAbsolutePath());
            statusModel.setFilename(file.getName());

            if (!statusModel.getUri().toString().endsWith(".nomedia")) {
                filesList.add(statusModel);
            }
        }
        return filesList;
    }

    // method for downloading status
    public static void downloadStatus(Context context, File filePath, File destinationPath, String fileName, String destinationPathString) {

        try {
            org.apache.commons.io.FileUtils.copyFileToDirectory(filePath, destinationPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(context, new String[]{destinationPathString + fileName},
                new String[]{"*/*"}
                , new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {

                    }

                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });

    }

}
