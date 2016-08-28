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
import java.util.Map;

/**
 * Created by lichaojian on 16-8-28.
 */
public class MainAdapter extends BaseAdapter{
    private Context mContext;
    private List<Map<String,String>> mPathList;

    public MainAdapter(Context context, List<Map<String,String>> pathList) {
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

        NoteApplication.getInstance(mContext).displayImage("file:///" + mPathList.get(position).get("picturePath"), holder.mImageView);
        return convertView;
    }

    static class ViewHolder {
        ImageView mImageView;
    }
}
