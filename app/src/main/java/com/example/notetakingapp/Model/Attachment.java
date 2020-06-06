package com.example.notetakingapp.Model;

import android.graphics.Bitmap;
import android.net.Uri;

public class Attachment {
    private Uri uri;
    private Bitmap bitmap;
    private String imgPath, imgName;


    public Attachment() {
    }

    public Attachment(Bitmap bitmap, Uri uri) {
        this.bitmap = bitmap;
        this.uri = uri;
    }

    public Attachment(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Attachment(Uri uri) {
        this.uri = uri;
    }

    public Attachment(Uri uri, String imgName) {
        this.uri = uri;
        this.imgName = imgName;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setUri(Uri uri, String imgName) {
        this.uri = uri;
        this.imgName = imgName;
    }

    public void setImgPath(String imgPath, String imgName) {
        this.imgPath = imgPath;
        this.imgName = imgName;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getImgName() {
        return imgName;
    }


}
