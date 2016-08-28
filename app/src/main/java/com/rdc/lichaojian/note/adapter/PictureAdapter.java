package com.rdc.lichaojian.note.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.rdc.lichaojian.note.activity.R;
import com.rdc.lichaojian.note.config.NoteApplication;

import java.util.List;

/**
 * Created by lichaojian on 16-8-28.
 */
public class PictureAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mPathList;

    public PictureAdapter(Context context, List<String> pathList) {
        mContext = context;
        mPathList = pathList;
    }

    @Override
    public int getCount() {
        return mPathList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_item, null);
            holder.mImageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        NoteApplication.getInstance(mContext).displayImage("file:///" + mPathList.get(position), holder.mImageView);
        return convertView;
    }

    static class ViewHolder {
        ImageView mImageView;
    }
}
