package com.example.notetakingapp.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.notetakingapp.Model.NoteFields.NoteTable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Note.db";
    private static final int DATABASE_VERSION = 13;
    private static final String TAG = "Db helper";
    Context context;

    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private Date date = new Date();

    public NoteDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_NOTELIST_TABLE = "CREATE TABLE " +
                NoteTable.TABLE_NAME + " (" +
                NoteTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NoteTable.Column_Title + " TEXT NOT NULL, " +
                NoteTable.Column_Content + " TEXT NOT NULL, " +
                NoteTable.Time_Stamp + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                NoteTable.Img_Name + " TEXT , " +
                NoteTable.Image_Path + " TEXT" +
                ");";
        db.execSQL(SQL_CREATE_NOTELIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NoteTable.TABLE_NAME);
        onCreate(db);
    }

    public long addNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NoteTable.Column_Title, note.getmTitle());
        cv.put(NoteTable.Column_Content, note.getmContent());
        cv.put(NoteTable.Time_Stamp, formatter.format(date));

        if (note.getImgPath() == null) {
            cv.putNull(NoteTable.Img_Name);
            cv.putNull(NoteTable.Image_Path);
        } else {
            cv.put(NoteTable.Image_Path, note.getImgPath());
            cv.put(NoteTable.Img_Name, note.getmImgName());
        }
        return db.insert(NoteTable.TABLE_NAME, null, cv);

    }

    public Note getNote(long id) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(NoteTable.TABLE_NAME, new String[]{NoteTable._ID,
                        NoteTable.Column_Title, NoteTable.Column_Content,
                        NoteTable.Time_Stamp, NoteTable.Image_Path, NoteTable.Img_Name},
                NoteTable._ID + "=" + id, null,
                null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        assert cursor != null;
        int titleIndex = cursor.getColumnIndex(NoteTable.Column_Title);
        String title = cursor.getString(titleIndex);
        int contentIndex = cursor.getColumnIndex(NoteTable.Column_Content);
        String content = cursor.getString(contentIndex);
        int timeIndex = cursor.getColumnIndex(NoteTable.Time_Stamp);
        String time = cursor.getString(timeIndex);
        int imageIndex = cursor.getColumnIndex(NoteTable.Image_Path);
        String imagePath = cursor.getString(imageIndex);
        int nameIndex = cursor.getColumnIndex(NoteTable.Img_Name);
        Log.d(TAG, "getNote: " + nameIndex);
        String imgName = cursor.getString(nameIndex);
        Log.d(TAG, "getNote: " + imgName);
        if (imagePath == null) {
            Note note = new Note(id, title, content, time);
            cursor.close();
            return note;
        } else {
            Note note = new Note(id, title, content, time, imagePath, imgName);
            cursor.close();
            return note;
        }

    }

    public List<Note> getAllNote() {
        SQLiteDatabase db = getWritableDatabase();
        List<Note> allNotes = new ArrayList<>();
        Cursor cursor;
        cursor = db.query(NoteTable.TABLE_NAME, null, null, null, null, null, NoteTable.Time_Stamp + " DESC");

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                int titleIndex = cursor.getColumnIndex(NoteTable.Column_Title);
                String title = cursor.getString(titleIndex);
                int contentIndex = cursor.getColumnIndex(NoteTable.Column_Content);
                String content = cursor.getString(contentIndex);
                int timeIndex = cursor.getColumnIndex(NoteTable.Time_Stamp);
                String time = cursor.getString(timeIndex);
                int imageIndex = cursor.getColumnIndex(NoteTable.Image_Path);
                String imagePath = cursor.getString(imageIndex);
                int nameIndex = cursor.getColumnIndex(NoteTable.Img_Name);
                String imgName = cursor.getString(nameIndex);

                if (imagePath == null) {
                    Note note = new Note(id, title, content, time);
                    allNotes.add(note);
                } else {
                    Note note = new Note(id, title, content, time, imagePath, imgName);
                    allNotes.add(note);

                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        return allNotes;

    }

    public long updateNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NoteTable.Column_Title, note.getmTitle());
        cv.put(NoteTable.Column_Content, note.getmContent());
        cv.put(NoteTable.Time_Stamp, formatter.format(date));
        if (note.getImgPath() == null) {
            cv.putNull(NoteTable.Image_Path);
            cv.putNull(NoteTable.Img_Name);

        } else {
            cv.put(NoteTable.Image_Path, note.getImgPath());
            cv.put(NoteTable.Img_Name, note.getmImgName());
        }
        long id = db.update(NoteTable.TABLE_NAME, cv, NoteTable._ID + "=" + note.getmId(), null);
        Log.d(TAG, "editNote: " + id);
        return id;
    }
//
//    public static byte[] convertBitmapIntoByte(Bitmap bitmap) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        return stream.toByteArray();
//    }
//
//    // convert from byte array to bitmap
//    public static Bitmap getImage(byte[] image) {
//        return BitmapFactory.decodeByteArray(image, 0, image.length);
//    }

}
