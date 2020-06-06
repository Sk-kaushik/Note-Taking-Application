package com.example.notetakingapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class bottomSheetDialog extends BottomSheetDialogFragment {
    public BottomSheetListner mListner;
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
                mListner.onButtonClick("delete");
                dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListner.onButtonClick("cancel");
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mListner = (BottomSheetListner) context;
        mContext = context;
        super.onAttach(context);

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        mListner.onButtonClick("cancel");
        super.onCancel(dialog);
    }

    public interface BottomSheetListner {
        void onButtonClick(String text);
    }
}
