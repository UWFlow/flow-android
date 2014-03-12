package com.uwflow.flow_android.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

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

    public static Bitmap rotateBitmap(Bitmap source, float angle)
    {
        if (source == null)
            return null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
