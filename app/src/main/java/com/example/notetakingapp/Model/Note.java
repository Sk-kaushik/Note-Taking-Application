package com.example.notetakingapp.Model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.io.Serializable;

public class Note implements Serializable {
    String[] listImgName;
    String[] listImgPath;
    private String mTitle, mContent, mTime;
    private long mId;
    private Uri mUri;
    private String mImgPath;
    private String mImgName;


    public Note() {
    }

    public Note(String mTitle, String mContent) {
        this.mTitle = mTitle;
        this.mContent = mContent;

    }

    public Note(String mTitle, String mContent, Uri uri) {
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mUri = uri;
    }

    public Note(String mTitle, String mContent, String imgPath, String imgName) {
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.mImgPath = imgPath;
        this.mImgName = imgName;
    }

    Note(long Id, String Title, String Content, String Time) {
        mId = Id;
        mTitle = Title;
        mContent = Content;
        mTime = Time;
    }

    Note(long Id, String Title, String Content, String Time, Uri uri) {
        mId = Id;
        mTitle = Title;
        mContent = Content;
        mTime = Time;
        mUri = uri;

    }

    Note(long Id, String Title, String Content, String Time, String imgPath, String imgName) {
        mId = Id;
        mTitle = Title;
        mContent = Content;
        mTime = Time;
        mImgPath = imgPath;
        mImgName = imgName;

    }

    public String getmImgName() {
        return mImgName;
    }

    public void setmImgName(String mImgName) {
        this.mImgName = mImgName;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmTime() {
        return mTime;
    }

    public long getmId() {
        return mId;
    }

    public Uri getmUri() {
        return mUri;
    }

    public void setmUri(Uri mUri) {
        this.mUri = mUri;
    }

    public Bitmap getImg() {
        return null;
    }

    public String[] getListImgName() {
        return listImgName;
    }

    public void setListImgName(String[] listImgName) {
        this.listImgName = listImgName;
    }

    public String[] getListImgPath() {
        return listImgPath;
    }

    public void setListImgPath(String[] listImgPath) {
        this.listImgPath = listImgPath;
    }

    public String getImgPath() {
        return mImgPath;
    }

    public void setImgPath(String imgPath) {
        this.mImgPath = imgPath;
    }

}
