package com.starsutdio.biglz.activity.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.starsutdio.biglz.R;
import com.starsutdio.biglz.utils.BitmapUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by windfall on 16-11-4.
 */

public class PopupWindowListAdapter extends BaseAdapter {
    private List<String> dirName ;
    private List<String> image_url;
    private Context mContext;
    private LayoutInflater mInflater;
    private Bitmap mBitmap;

    public PopupWindowListAdapter(Context context,List<String> dirName,List<String> image_url) {
        mContext = context;
        this.dirName = dirName;
        this.image_url = image_url;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dirName.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup group) {
        if (view == null){
            view = mInflater.inflate(R.layout.item_photo_list,null);

        }
        TextView textView = (TextView)view.findViewById(R.id.dirName);
        ImageView imageView = (ImageView)view.findViewById(R.id.first_photo);
        textView.setText(dirName.get(i));
        mBitmap = BitmapUtil.decodeBitmapFromFile(image_url.get(i),100,100);
        imageView.setImageBitmap(mBitmap);
        return view;
    }
}
