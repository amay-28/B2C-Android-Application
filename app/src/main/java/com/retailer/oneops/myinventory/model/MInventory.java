package com.retailer.oneops.myinventory.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.retailer.oneops.productListing.model.MProduct;

import lombok.Data;

@Data
public class MInventory implements Parcelable {
    private int product_id;
    private int margin;
    private int user_id;
    private int id;
    private MProduct product;

    public MInventory() {

    }

    public MInventory(Parcel in) {
        product_id = in.readInt();
        margin = in.readInt();
        user_id = in.readInt();
        id = in.readInt();
        product = in.readParcelable(MProduct.class.getClassLoader());
    }

    public static final Creator<MInventory> CREATOR = new Creator<MInventory>() {
        @Override
        public MInventory createFromParcel(Parcel in) {
            return new MInventory(in);
        }

        @Override
        public MInventory[] newArray(int size) {
            return new MInventory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(product_id);
        dest.writeInt(margin);
        dest.writeInt(user_id);
        dest.writeInt(id);
        dest.writeParcelable(product, flags);
    }
}
