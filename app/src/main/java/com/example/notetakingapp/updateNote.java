package com.example.notetakingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.example.notetakingapp.Model.Attachment;
import com.example.notetakingapp.Model.Note;
import com.example.notetakingapp.Model.NoteDbHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class updateNote extends AppCompatActivity {
    Utils utils;
    EditText Title, Content;
    long mId = 0;
    Note note;
    String TAG = "updateNote";
    AppCompatButton btnSave;
    boolean isDelete = false, stringUriNotNull = false;
    ImageButton addImg;
    ViewStub stub;
    Bitmap loadedImg;
    String FILE_NAME;
    Uri uri;
    ImageView img;
    String imgPath, imgName;
    gridViewAdapter mAdapter;
    NoteDbHelper db;
    Bitmap bitmap;
    Uri muri;
    String ImgPath;
    View inflated;
    List<Attachment> attachmentList = new ArrayList<>();
    Attachment attachment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);
        mId = Objects.requireNonNull(getIntent().getExtras()).getLong("id");
        Title = findViewById(R.id.Title);
        Content = findViewById(R.id.Content);
        btnSave = findViewById(R.id.saveButton);
        addImg = findViewById(R.id.add_img);

        attachment = new Attachment();
        utils = new Utils();

        db = new NoteDbHelper(this);
        if (mId != 0 && mId > 0) {
            note = db.getNote(mId);
            Title.setText(note.getmTitle());
            Content.setText(note.getmContent());
            if (note.getImgPath() != null) {
                imgPath = note.getImgPath();
                imgName = note.getmImgName();
                addImageInEditText(imgPath, imgName, null);
            } else {
                attachmentList.clear();
            }
        }

//      --------------------  Setting Toolbar---------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Note");

//   ---------------------     Calling Functions -------------------------------------------
        btnSave.setOnClickListener(v -> {
            try {
                saveButton();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        addImg.setOnClickListener(v -> addImage());


    }

    //---------------------Creating Menu -------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
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
                note.setmTitle(title);
                note.setmContent(content);
                db = new NoteDbHelper(this);

                if (isDelete) {
                    note.setImgPath(null);
                    note.setmImgName(null);
                    if (stringUriNotNull) {
                        id = db.updateNote(note);
                    } else {
                        id = db.updateNote(note);
                        deleteImageFromInternalStorage();

                    }
                    isDelete = false;
                } else {
                    Log.d(TAG, "saveButton: " + "Not Deleted");
                    Log.d(TAG, "Empty: " + note.getmTitle() + " : " + note.getmContent());
                    id = db.updateNote(note);


                }
            } else {
                String title = Title.getText().toString();
                String content = Content.getText().toString();
                note.setmTitle(title);
                note.setmContent(content);
                for (Attachment attachment :
                        attachmentList) {
                    if (attachment.getUri() != null) {
                        Uri localUri = attachment.getUri();
                        String fName = attachment.getImgName();

                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), localUri);
                        imgPath = utils.saveToInternalStorage(bitmap, getApplicationContext(), fName);
                        if (attachmentList.size() > 1) {
                            Log.d(TAG, "attachment List: " + attachment.getImgName());
                            note.setImgPath(attachment.getImgPath());
                            note.setmImgName(attachment.getImgName());
                            id = db.updateNote(note);
                        } else {
                            Log.d(TAG, "saveButton: " + "size is 1");

                            note.setImgPath(imgPath);
                            note.setmImgName(fName);
                            id = db.updateNote(note);
                        }


                    } else {
                        Log.d(TAG, "forech: " + attachment.getImgName());
                        note.setmTitle(title);
                        note.setmContent(content);
                        id = db.updateNote(note);

                    }


                }
            }

            if (id != -1) {
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
            Log.d(TAG, "onActivityResult: " + uri);
            FILE_NAME = utils.getImageName(uri, getApplicationContext());
            addImageInEditText(null, FILE_NAME, uri);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            String delete = data.getStringExtra("delete");
            isDelete = data.getBooleanExtra("isDelete", true);
            int pos = data.getIntExtra("imagePosition", 0);
            stringUriNotNull = data.getBooleanExtra("stringUriNotNull", false);

            assert delete != null;
            if (delete.equals("true")) {
                Log.d(TAG, "onActivityResult: " + "True");
                if (pos != -1) {
                    attachmentList.remove(attachmentList.get(pos));
                    Log.d(TAG, "onActivityResult: " + pos);
                    Log.d(TAG, "onActivityResult: " + attachmentList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        } else {
            Log.d(TAG, "onActivityResult: " + "cancelled");
            muri = null;
            ImgPath = null;
        }
    }


    private void addImageInEditText(String path, String name, Uri uri) {
        if (stub == null) {
            stub = findViewById(R.id.viewStub);
            inflated = stub.inflate();
        }
        Bitmap img;

        if (uri == null && imgPath != null) {
            img = utils.loadImageFromStorage(path, name);
            attachment.setBitmap(img);
            attachment = new Attachment(img, null);
            attachment.setImgPath(path, name);

            attachmentList.add(attachment);
        } else if (uri != null) {
            attachment = new Attachment(uri, name);
            attachmentList.add(attachment);
            Log.d(TAG, "addImageInEditText: " + uri + " :" + name);
            Log.d(TAG, "addImageInEditText: " + attachmentList);
        }


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
            private String ImgName;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(updateNote.this, fullScreenImage.class);

                muri = attachmentList.get(position).getUri();
                ImgPath = attachmentList.get(position).getImgPath();
                ImgName = attachmentList.get(position).getImgName();

                if (muri != null) {
                    Log.d(TAG, "onItemClick: " + muri);
                    intent.putExtra("URI", muri.toString());
                    intent.putExtra("POS", position);
                    Log.d(TAG, "onItemClick: " + position);

                } else if (ImgPath != null) {
                    Log.d(TAG, "onItemClick: " + ImgPath);
                    intent.putExtra("PATH", ImgPath);
                    intent.putExtra("NAME", ImgName);
                    intent.putExtra("POS", position);
                    Log.d(TAG, "onItemClick: " + position);

                } else {
                    Log.d(TAG, "onItemClick: " + ImgPath);
                    Log.d(TAG, "onItemClick: " + muri);

                    Log.d(TAG, "onItemClick: null");
                }

//                intent.putExtra("POS", position);
//                intent.putExtra("PATH", ImgPath);
//                intent.putExtra("NAME", name);
//                intent.putExtra("URI", muri.toString());
//
                startActivityForResult(intent, 2);
            }
        });

    }


    public void deleteImageFromInternalStorage() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                boolean success = false;
                File file = new File(imgPath, imgName);
                if (file.exists()) {
                    success = file.delete();
                }
                if (success) {
                } else Log.d(TAG, "run:  Failed");
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}


