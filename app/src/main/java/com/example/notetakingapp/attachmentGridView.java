package com.example.notetakingapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

public class attachmentGridView extends GridView {

    private int itemHeight;

    public attachmentGridView(Context context) {
        super(context);
    }

    public attachmentGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public attachmentGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public attachmentGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasuExpandableHeightGridViewreSpec) {
//		if (isExpanded()) {
        // Calculate entire height by providing a very large height hint.
        // View.MEASURED_SIZE_MASK represents the largest height possible.
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
//		} else {
//			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		}
    }

//	public void setExpanded(boolean expanded) {
//		this.expanded = expanded;
//	}


    public void autoresize() {
        // Set gridview height
//	    ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int items = getAdapter().getCount();
        int columns = items == 1 ? 1 : 2;

        setNumColumns(columns);
//    	itemHeight = Constants.THUMBNAIL_SIZE * 2 / columns;
//    	layoutParams.height = ( (items / columns) + (items % columns) ) * itemHeight; //this is in pixels
//
//	    setLayoutParams(layoutParams);
    }


    public int getItemHeight() {
        return itemHeight;
    }
}
