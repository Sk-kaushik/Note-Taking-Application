package com.example.notetakingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notetakingapp.Model.Note;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.noteViewHolder> implements Filterable {
    String TAG = "noteAdapter";
    private Context mContext;
    private List<Note> notes;
    private List<Note> noteListAll = new ArrayList<>();
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Note> filteredList = new ArrayList<>();

            if (constraint.toString().isEmpty()) {
                filteredList.addAll(noteListAll);

                Log.d(TAG, "is Empty: " + filteredList);

            } else {
                for (Note note : noteListAll
                ) {
                    if (note.getmTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {

                        filteredList.add(note);
                        Log.d(TAG, "Contain Data: " + filteredList);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            notes.clear();
            notes.addAll((Collection<? extends Note>) results.values);
            Log.d(TAG, "publishResults: " + results.values);
            notifyDataSetChanged();
        }
    };
    private adapterInterface mAdapterInterface;
    private MainActivity mainActivity;


    public NoteAdapter(Context context, List<Note> noteList, adapterInterface adapterInterface) {
        this.mContext = context;
        this.notes = noteList;
        this.mAdapterInterface = adapterInterface;
        this.mainActivity = (MainActivity) context;
        this.noteListAll = noteList;
    }

    @NonNull
    @Override
    public noteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.note_item, parent, false);
        return new noteViewHolder(v, mainActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull noteViewHolder holder, int position) {

        String mTitle = notes.get(position).getmTitle();
        String mContent = notes.get(position).getmContent();
        String mTime = notes.get(position).getmTime();
        Log.d(TAG, "Time: " + notes.get(position).getmTime());
        long mId = notes.get(position).getmId();

        holder.itemView.setTag(mId);
        holder.title.setText(mTitle);
        holder.content.setText(mContent);
        holder.time.setText(timeConvertor(mTime));

        if (!mainActivity.isInActionMode) {
            holder.checkbox.setVisibility(View.GONE);
        } else {
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.checkbox.setChecked(false);
        }

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mAdapterInterface.onLongClick();
                return false;
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterInterface.onCardClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();

    }

    @Override
    public Filter getFilter() {

        return filter;
    }

    private String timeConvertor(String time) {
        String str = time;
//        Splitting Time Stamp in date part[0] and Time part[1]
        String[] parts = str.split(" ");

//        Splitting Time and removing seconds
        String[] TimeParts = parts[1].split(":");
        return (TimeParts[0] + ":" + TimeParts[1]);

    }

    static class noteViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        TextView time;
        CardView cardView;
        CheckBox checkbox;
        MainActivity mainActivity;

        noteViewHolder(@NonNull View itemView, MainActivity mainActivity) {
            super(itemView);
            title = itemView.findViewById(R.id.Title);
            content = itemView.findViewById(R.id.Content);
            time = itemView.findViewById(R.id.Time);
            cardView = itemView.findViewById(R.id.cardView);
            checkbox = itemView.findViewById(R.id.checkbox);
            this.mainActivity = mainActivity;

//            cardView.setOnLongClickListener(mainActivity);
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.prepareSelection(v, getAdapterPosition());
                }
            });

        }
    }


}
