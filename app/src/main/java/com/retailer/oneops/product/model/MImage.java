package com.retailer.oneops.product.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.List;

import lombok.Data;

@Data
public class MImage implements Parcelable {
    private String id;
    private String productId;
    private String url;
    private File file;
    private boolean isLocal;

    private List<MImage> image;

    public MImage() {

    }

    protected MImage(Parcel in) {
        id = in.readString();
        productId = in.readString();
        url = in.readString();
        isLocal = in.readByte() != 0;
        image = in.createTypedArrayList(MImage.CREATOR);
    }

    public static final Creator<MImage> CREATOR = new Creator<MImage>() {
        @Override
        public MImage createFromParcel(Parcel in) {
            return new MImage(in);
        }

        @Override
        public MImage[] newArray(int size) {
            return new MImage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(productId);
        dest.writeString(url);
        dest.writeByte((byte) (isLocal ? 1 : 0));
        dest.writeTypedList(image);
    }
}
