package com.retailer.oneops.auth.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import lombok.Data;

/*-------------- used for category -- sub category--- there sub category---------------------*/
@Data
public class MCategory  implements Parcelable {
    private String id;
    private String parentId;
    private String name;
    private String slug;
    private String treeId;
    private String image;
    private String level;
    private String created_at;
    private String updated_at;
    private List<MCategory> children;
    private Boolean isSelected=false;

    public MCategory(Parcel in) {
        id = in.readString();
        parentId = in.readString();
        name = in.readString();
        slug = in.readString();
        treeId = in.readString();
        image = in.readString();
        level = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        children = in.createTypedArrayList(MCategory.CREATOR);
        byte tmpIsSelected = in.readByte();
        isSelected = tmpIsSelected == 0 ? null : tmpIsSelected == 1;
    }

    public static final Creator<MCategory> CREATOR = new Creator<MCategory>() {
        @Override
        public MCategory createFromParcel(Parcel in) {
            return new MCategory(in);
        }

        @Override
        public MCategory[] newArray(int size) {
            return new MCategory[size];
        }
    };

    public MCategory() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(parentId);
        parcel.writeString(name);
        parcel.writeString(slug);
        parcel.writeString(treeId);
        parcel.writeString(image);
        parcel.writeString(level);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
        parcel.writeTypedList(children);
        parcel.writeByte((byte) (isSelected == null ? 0 : isSelected ? 1 : 2));
    }
}
