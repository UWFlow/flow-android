package com.uwflow.flow_android.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class HelperUtil {

    public static byte[] bitmapToByteArray(Bitmap img) {
        if (img == null)
            return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap byteArrayToBitmap(byte[] img) {
        if (img == null)
            return null;
        else
            return BitmapFactory.decodeByteArray(img, 0, img.length);
    }
}
