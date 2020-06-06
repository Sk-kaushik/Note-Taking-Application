package com.example.notetakingapp.Model;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.notetakingapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MultipleDeleteBottomSheet extends BottomSheetDialogFragment {
    public MultipleDeleteBottomSheet.MutlipleDelete mListner;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.delete_bottomsheet, container, false);
        Button delete = v.findViewById(R.id.delete);
        Button cancel = v.findViewById(R.id.cancel);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListner.onMultipleDelete("deleteAll");
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListner.onMultipleDelete("cancel");
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mListner = (MultipleDeleteBottomSheet.MutlipleDelete) context;
        mContext = context;
        super.onAttach(context);

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mListner.onMultipleDelete("cancel");
        super.onCancel(dialog);
    }

    public interface MutlipleDelete {
        void onMultipleDelete(String text);
    }
}
