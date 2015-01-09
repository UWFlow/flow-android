package com.uwflow.flow_android.db_object;

import android.graphics.Bitmap;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.uwflow.flow_android.util.HelperUtil;

@DatabaseTable(tableName = "ScheduleImage")
public class ScheduleImage {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    private byte[] image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return HelperUtil.byteArrayToBitmap(image);
    }

    public void setImage(Bitmap image) {
        byte[] bytes = HelperUtil.bitmapToByteArray(image);
        if (bytes != null)
            this.image = bytes;
    }
}
