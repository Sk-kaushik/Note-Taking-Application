package com.example.notetakingapp;

import android.content.Context;
import android.util.AttributeSet;

public class squareImageView extends androidx.appcompat.widget.AppCompatImageView {
    public squareImageView(Context context) {
        super(context);
    }

    public squareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public squareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width
    }
}
