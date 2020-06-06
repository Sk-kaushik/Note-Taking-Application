package com.example.notetakingapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Utils {

    private static final String TAG = " Utils";
    private File imgDir;

    String saveToInternalStorage(Bitmap bitmapImage, Context context, String FILE_NAME) {

        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        imgDir = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath = new File(imgDir, FILE_NAME);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return imgDir.getAbsolutePath();
    }


    Bitmap loadImageFromStorage(String path, String Name) {
        Bitmap loadedImg = null;
        try {
            File f = new File(path, Name);
            loadedImg = BitmapFactory.decodeStream(new FileInputStream(f));

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
        return loadedImg;

    }

    String getImageName(Uri uri, Context context) {
        String FILE_NAME;
        Cursor imageData = context.getContentResolver().query(uri, null, null, null, null);
        assert imageData != null;
        int nameIndex = imageData.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        imageData.moveToFirst();
        FILE_NAME = imageData.getString(nameIndex);
        imageData.close();
        return FILE_NAME;

    }

}
