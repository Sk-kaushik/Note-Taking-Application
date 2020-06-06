package com.example.notetakingapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notetakingapp.Model.MultipleDeleteBottomSheet;
import com.example.notetakingapp.Model.Note;
import com.example.notetakingapp.Model.NoteDbHelper;
import com.example.notetakingapp.Model.NoteFields;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity implements bottomSheetDialog.BottomSheetListner, adapterInterface, MultipleDeleteBottomSheet.MutlipleDelete {
    private static final String TAG = "MainActivity";
    private static final int PERMISSION_REQUEST_CODE = 200;
    public RecyclerView recyclerView;
    public boolean isInActionMode = false;
    public CollapsingToolbarLayout collapsingToolbarLayout;
    public Toolbar toolbar;
    NoteDbHelper noteDbHelper;
    Note deletedNote;
    List<Note> allNote;
    ArrayList<Note> selectedItem = new ArrayList<>();
    int counter = 0;
    long id;
    int position;
    private NoteAdapter mAdapter;
    private SQLiteDatabase mDataBase;
    private Menu menu;
    private ActionMode mActionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createDir();

        noteDbHelper = new NoteDbHelper(this);
        mDataBase = noteDbHelper.getWritableDatabase();
        allNote = new ArrayList<>();
        deletedNote = new Note();
        recyclerView = findViewById(R.id.recyclerView);
        SettingAdapter();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, addNote.class);
                startActivity(intent);
            }
        });

        collapsingToolbarLayout =
                findViewById(R.id.collapsing_toolbar);

        collapsingToolbarLayout.setTitle("All Notes");

        swipeFeature();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);

                return false;
            }
        });
//        menu.findItem(R.id.action_delete).setVisible(false);

        return true;
    }

    private void SettingAdapter() {
        NoteDbHelper noteDbHelper = new NoteDbHelper(this);
        mDataBase = noteDbHelper.getWritableDatabase();
        allNote.addAll(noteDbHelper.getAllNote());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NoteAdapter(this, allNote, this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    public void swipeFeature() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                position = viewHolder.getAdapterPosition();

                // Take action for the swiped item
                if (ItemTouchHelper.LEFT == direction) {
//                    removeItem((long) viewHolder.itemView.getTag());
                    deletedNote = allNote.get(position);
                    allNote.remove(deletedNote);
                    mAdapter.notifyItemRemoved(position);

                    bottomSheetDialog bottomSheet = new bottomSheetDialog();
                    bottomSheet.show(getSupportFragmentManager(), "Delete Bottom Sheet");


                }
            }

            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftActionIcon(R.drawable.delete)
                        .addSwipeLeftBackgroundColor(getColor(R.color.deleteBackground))
                        .addSwipeLeftLabel("Delete")
                        .setSwipeLeftLabelColor(getColor(R.color.AppName))
                        .setSwipeLeftLabelTypeface(Typeface.SANS_SERIF)
                        .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 24)
                        .setIconHorizontalMargin(TypedValue.COMPLEX_UNIT_DIP, 25)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


    private void removeItem(Note delete) {

        mDataBase.delete(NoteFields.NoteTable.TABLE_NAME, NoteFields.NoteTable._ID + "=" + delete.getmId(), null);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onButtonClick(String text) {
        if (text.equals("delete")) {
            removeItem(deletedNote);

        } else if (text.equals("cancel")) {
            undoChanges();
        }
    }

    public void undoChanges() {
        allNote.add(position, deletedNote);
        mAdapter.notifyItemInserted(position);
    }


    @Override
    public void onLongClick() {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.select_all);
        isInActionMode = true;
        toolbar.setTitle(" 0 item selected");
        collapsingToolbarLayout.setTitle("0 Selected");
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mAdapter.notifyDataSetChanged();

    }

    public void prepareSelection(View view, int position) {
        if (((CheckBox) view).isChecked()) {
            selectedItem.add(allNote.get(position));
            Log.d(TAG, "prepareSelection: " + allNote.get(position).getmTitle());
            Log.d(TAG, "prepareSelection: " + selectedItem);
            counter = counter + 1;
            updateTextView(counter);
        } else {
            selectedItem.remove(allNote.get(position));
            counter = counter - 1;
            updateTextView(counter);
        }

    }

    public void updateTextView(int counter) {
        if (counter == 0) {
            collapsingToolbarLayout.setTitle("0 item Selected");
        } else {
            collapsingToolbarLayout.setTitle(counter + " item Selected");
        }
    }


    public void DeleteNote(ArrayList<Note> noteList) {
        for (Note note : noteList
        ) {
            long removeId = note.getmId();
            allNote.remove(note);
            mDataBase.delete(NoteFields.NoteTable.TABLE_NAME, NoteFields.NoteTable._ID + " =" + removeId, null);
            mAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;

            case R.id.deleteActionMode:
                MultipleDeleteBottomSheet multipleDeleteBottomSheet = new MultipleDeleteBottomSheet();
                multipleDeleteBottomSheet.show(getSupportFragmentManager(), "Multiple Delete ");
                break;
            case android.R.id.home:
                clearActionMode();
                mAdapter.notifyDataSetChanged();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMultipleDelete(String text) {
        if (text.equals("deleteAll")) {
            DeleteNote(selectedItem);
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            clearActionMode();
        }
    }


    @Override
    public void onCardClick(int pos) {
        if (isInActionMode) {
            clearActionMode();
            Intent intent = new Intent(this, updateNote.class);
            intent.putExtra("id", allNote.get(pos).getmId());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, updateNote.class);
            intent.putExtra("id", allNote.get(pos).getmId());
            startActivity(intent);
        }

    }


    public void clearActionMode() {
        isInActionMode = false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_main);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        collapsingToolbarLayout.setTitle("All Notes");
        counter = 0;
        selectedItem.clear();
        mAdapter.notifyDataSetChanged();
    }


    public void checkPermission() {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermission: " + "Granted");
        } else {
            requestStoragePermission();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to store data ")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //    Create File for images ---------------------
    public void createDir() {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "MyDirName");

        if (!mediaStorageDir.exists()) {
            Log.d(TAG, "createDir:  " + "Created Dir");
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory");
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (isInActionMode) {
            clearActionMode();
            mAdapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }
}
