package com.example.notetakingapp.Model;

import android.provider.BaseColumns;

public class NoteFields {

    private NoteFields() {
    }

    public static final class NoteTable implements BaseColumns {
        public static final String TABLE_NAME = "Note";
        static final String Column_Title = "Title";
        static final String Column_Content = "Content";
        static final String Time_Stamp = "TimeStamp";

        static final String Img_Name = "Img";
        static final String Image_Path = "ImagePath";
//        public static final String Date = "Date";


    }
}
