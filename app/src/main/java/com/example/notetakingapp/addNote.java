package com.example.notetakingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.example.notetakingapp.Model.Attachment;
import com.example.notetakingapp.Model.Note;
import com.example.notetakingapp.Model.NoteDbHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class addNote extends AppCompatActivity {
    EditText Title, Content;
    AppCompatButton btnSave;
    AppCompatImageButton addImg;
    String TAG = "addNote";
    ViewStub stub;
    String imgPath;
    File imgDir;
    gridViewAdapter mAdapter;
    Note note;
    View inflated;
    List<Attachment> attachmentList = new ArrayList<>();
    Attachment attachment;
    String FILE_NAME = "";
    Utils utils;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

//        Setting Toolbar---------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Note");
        utils = new Utils();


//        Getting Ids of View;-----------------------------------------
        Title = findViewById(R.id.Title);
        Content = findViewById(R.id.Content);
        btnSave = findViewById(R.id.saveButton);
        addImg = findViewById(R.id.add_img);
        btnSave.setOnClickListener(v -> {
                    try {
                        saveButton();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );

        addImg.setOnClickListener(v -> addImage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveButton() throws IOException {
        if (Title.getText().toString().trim().length() == 0 || Content.getText().toString().trim().length() == 0) {
            if (Title.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Please enter Title", Toast.LENGTH_SHORT).show();
            } else if (Content.getText().toString().trim().length() == 0) {
                Toast.makeText(this, "Please enter Note", Toast.LENGTH_SHORT).show();

            }
        } else {
            long id = -1;
            if (attachmentList.isEmpty()) {
                String title = Title.getText().toString();
                String content = Content.getText().toString();
                Note note = new Note(title, content);
                NoteDbHelper db = new NoteDbHelper(this);
                id = db.addNote(note);
            } else {
                String title = Title.getText().toString();
                String content = Content.getText().toString();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgPath = utils.saveToInternalStorage(bitmap, getApplicationContext(), FILE_NAME);
                Note note = new Note(title, content, imgPath, FILE_NAME);
                NoteDbHelper db = new NoteDbHelper(this);
                id = db.addNote(note);
            }


            if (id != -1) {
                Log.d(TAG, "Add Note id: " + id);
                Toast.makeText(this, "Note Saved Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error in Saving", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addImage() {
        Intent imageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(imageIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        assert data != null;

        if (requestCode == 1 && resultCode == RESULT_OK) {
            uri = data.getData();
            FILE_NAME = utils.getImageName(uri, getApplicationContext());
            addImageInEditText(uri);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            String isDelete = data.getStringExtra("delete");
            int pos = data.getIntExtra("imagePosition", -1);

            assert isDelete != null;

            if (isDelete.equals("true")) {

                if (pos != -1) {
                    attachmentList.remove(attachmentList.get(pos));
                    mAdapter.notifyDataSetChanged();
                }

            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }


    private void addImageInEditText(Uri uri) {

        if (stub == null) {
            stub = findViewById(R.id.viewStub);
            inflated = stub.inflate();
        }

        attachment = new Attachment();
        attachment.setUri(uri);
        attachmentList.add(attachment);

        attachmentGridView mGridView;
        FrameLayout layout = inflated.findViewById(R.id.inflatedLayout);

        mAdapter = new gridViewAdapter(this, attachmentList);
        mGridView = layout.findViewById(R.id.gridView);
        mGridView.setAdapter(mAdapter);
        mGridView.autoresize();
        mAdapter.notifyDataSetChanged();

        assert stub != null;
        stub.setVisibility(View.VISIBLE);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = attachmentList.get(position).getUri();
                Intent intent = new Intent(addNote.this, fullScreenImage.class);

                intent.putExtra("Image", uri.toString());
                intent.putExtra("pos", position);
                startActivityForResult(intent, 2);
            }
        });

    }


}

