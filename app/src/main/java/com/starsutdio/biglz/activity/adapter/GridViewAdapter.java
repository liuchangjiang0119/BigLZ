package com.starsutdio.biglz.activity.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.starsutdio.biglz.R;

import java.util.List;

/**
 * Created by windfall on 16-11-6.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<Bitmap> mBitmapList;
    public GridViewAdapter(Context context,List<Bitmap> list){
        mContext = context;
        mBitmapList = list;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mBitmapList.size()+1;
    }

    @Override
    public Object getItem(int i) {
        return mBitmapList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup group) {
        if (view ==null){
            view = inflater.inflate(R.layout.gridview_item,null);
        }
        ImageView imageView = (ImageView)view.findViewById(R.id.grid_image);
        if (i == 0) imageView.setImageResource(R.drawable.ic_add_24dp);


        return view;
    }
}
