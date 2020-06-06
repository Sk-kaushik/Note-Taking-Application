package com.example.notetakingapp;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.notetakingapp.Model.Attachment;

import java.util.List;

public class gridViewAdapter extends BaseAdapter {
    AttachmentHolder holder = new AttachmentHolder();
    private Activity mActivity;
    private List<Attachment> attachmentList;
    private LayoutInflater inflater;

    gridViewAdapter(Activity mActivity, List<Attachment> attachmentList) {
        this.mActivity = mActivity;
        this.attachmentList = attachmentList;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return attachmentList.size();
    }

    @Override
    public Attachment getItem(int position) {
        return attachmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Attachment mAttachment = attachmentList.get(position);
        AttachmentHolder holder = new AttachmentHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_view_item, parent, false);
            holder.imgView = convertView.findViewById(R.id.picture);
//            imgView = new ImageView(mActivity);
//            imgView.setLayoutParams(new GridView.LayoutParams(parent.getMeasuredHeight(), parent.getMeasuredWidth()));
//            imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            imgView.setPadding(6, 6, 6, 6);
        } else {
            holder.imgView = (ImageView) convertView;
        }
        if (mActivity instanceof addNote) {
            Uri uri = attachmentList.get(position).getUri();

            Glide.with(convertView).load(uri).into(holder.imgView);
            holder.imgView.setPadding(5, 5, 5, 5);

        } else if (mActivity instanceof updateNote) {
            Uri uri = attachmentList.get(position).getUri();
            if (uri != null) {
                Glide.with(convertView).load(uri).into(holder.imgView);
            } else {
                holder.imgView.setImageBitmap(attachmentList.get(position).getBitmap());
                holder.imgView.setPadding(5, 5, 5, 5);


            }
        }

        return holder.imgView;
    }


    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }


    static class AttachmentHolder {
        ImageView imgView;
    }


}
