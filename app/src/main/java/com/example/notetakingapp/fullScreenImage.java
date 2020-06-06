package com.example.notetakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.Objects;

public class fullScreenImage extends AppCompatActivity {
    private static final String TAG = "FullScreen";
    public Toolbar toolbar;
    public int Pos;
    ImageView fullImage;
    String mPath, mName;
    String name;
    boolean stringUriNotNull = false;
    String stringUri;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        toolbar = findViewById(R.id.ImageToolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fullImage = findViewById(R.id.fullScreenImage);
        Intent intent = getIntent();
        name = Objects.requireNonNull(getCallingActivity()).getClassName();

        if (name.equals("com.example.notetakingapp.updateNote")) {
            Context context = getApplicationContext();
            mPath = intent.getStringExtra("PATH");
            mName = intent.getStringExtra("NAME");
            stringUri = intent.getStringExtra("URI");

            Pos = intent.getIntExtra("POS", -1);

            if (mPath != null) {
                Log.d(TAG, "if: ." + mPath);
                Log.d(TAG, "onCreate: " + context);
                uri = Uri.fromFile(new File(mPath, mName));
//                uri = Uri.parse(new File(mPath,mName).toString());
                Glide.with(context).load(uri).into(fullImage);
            } else if (stringUri != null) {
                stringUriNotNull = true;
                uri = Uri.parse(stringUri);
                Log.d(TAG, "Uri: " + uri);
                Log.d(TAG, "onCreate: " + context);
                Glide.with(context).load(uri).into(fullImage);

            } else {
                Log.d(TAG, "onCreate: No DATA");
            }

        } else if (name.equals("com.example.notetakingapp.addNote")) {
            Context context = getApplicationContext();

            uri = Uri.parse(Objects.requireNonNull(intent.getExtras()).getString("Image"));
            Pos = Objects.requireNonNull(intent.getExtras()).getInt("pos", -1);
            Glide.with(context).load(uri).into(fullImage);
            uri = null;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.select_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.deleteActionMode == item.getItemId()) {
            if (name.equals("com.example.notetakingapp.addNote")) {
                Intent deleteIntent = new Intent();
                deleteIntent.putExtra("delete", "true");
                deleteIntent.putExtra("imagePosition", Pos);
                setResult(Activity.RESULT_OK, deleteIntent);
                finish();
                return true;
            } else {
                Intent deleteIntent = new Intent();
                deleteIntent.putExtra("delete", "true");
                deleteIntent.putExtra("isDelete", true);
                deleteIntent.putExtra("imagePosition", Pos);
                deleteIntent.putExtra("stringUriNotNull", stringUriNotNull);

                setResult(Activity.RESULT_OK, deleteIntent);
                finish();
                return true;
            }

        } else if (android.R.id.home == item.getItemId()) {
            Intent back = new Intent();
            setResult(Activity.RESULT_CANCELED, back);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        uri = null;
        mPath = null;
        mName = null;
        super.onBackPressed();
    }
}
