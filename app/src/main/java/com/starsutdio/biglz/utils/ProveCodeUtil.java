package com.starsutdio.biglz.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by windfall on 16-11-1.
 */

public class ProveCodeUtil {
    //    验证码字符
    private static final char[] CHARS={'1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l',
            'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'

    };
    private static final int CodeLength = 4;    //字符长度
    private static final int WIDTH = 150,HIGHT=80;  //宽高
    private static final int FontSize = 40;         //字体大小
    private static int base_padding_left ;
    private static final int random_padding_left = 23,
            base_padding_top = 45,random_padding_top = 10;
    private static Random mRandom = new Random();

    private static String createRandomText(){
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<CodeLength;i++){
            builder.append(CHARS[mRandom.nextInt(CHARS.length)]);
        }
        return builder.toString();
    }
    public static Bitmap createRandomBitmap(){
        String proveCode = createRandomText();

        Bitmap bitmap = Bitmap.createBitmap(WIDTH,HIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setTextSize(FontSize);

        base_padding_left = 20;
        for (int i=0;i<CodeLength;i++){
            int color = RandomColor();
            paint.setColor(color);
            paint.setFakeBoldText(false);
            float skewx = mRandom.nextInt(11)/10;
            skewx = mRandom.nextBoolean()?skewx:(-skewx);
            paint.setTextSkewX(skewx);

            int padding_top = base_padding_top+mRandom.nextInt(random_padding_top);
            canvas.drawText(proveCode.charAt(i)+"",base_padding_left,padding_top,paint);
            base_padding_left += random_padding_left;
        }

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
    }

    private static int RandomColor(){
        int red = mRandom.nextInt(256);
        int green = mRandom.nextInt(256);
        int blue = mRandom.nextInt(256);
        return Color.rgb(red,green,blue);
    }

}
