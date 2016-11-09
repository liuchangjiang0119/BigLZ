package com.starsutdio.biglz.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.FileNameMap;

/**
 * Created by windfall on 16-11-4.
 */

public class BitmapUtil {
    public static Bitmap decodeBitmapFromFile(String path,int reqwidth,int reqheight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
//        读入图片
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);
//        计算采样率
        options.inSampleSize = calculateInSampleSize(options,reqwidth,reqheight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path,options);
    }

//    计算采样率
    public static int calculateInSampleSize(BitmapFactory.Options options,int reqwidth,int reqheight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height>reqheight||width>reqwidth){
            final int halfHeight = height/2;
            final int halfWidth = width/2;
            while ((halfHeight/inSampleSize)>=reqheight&&(halfWidth/inSampleSize)>=reqwidth){
                inSampleSize *=2;
            }
        }
        return inSampleSize;
    }
}
