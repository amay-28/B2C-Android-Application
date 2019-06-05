package com.retailer.oneops.productListing.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.retailer.oneops.auth.model.MCategory;
import com.retailer.oneops.product.model.MImage;

import java.util.List;

import lombok.Data;

@Data
public class MProduct implements Parcelable {
    private String id;
    private String name;
    private String categoryId;
    private String price;
    private String cost_price;
    private String type;
    private Boolean is_published;
    private String product_attributes;
    private String variant_attributes;
    private String created_at;
    private String updated_at;
    private String description;
    private String specifications;
    private String user_id;
    private MCategory category;
    private List<MImage> images;
    private List<MProductVariant> product_variant;

    public MProduct(Parcel in) {
        id = in.readString();
        name = in.readString();
        categoryId = in.readString();
        price = in.readString();
        cost_price = in.readString();
        type = in.readString();
        byte tmpIs_published = in.readByte();
        is_published = tmpIs_published == 0 ? null : tmpIs_published == 1;
        product_attributes = in.readString();
        variant_attributes = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        description = in.readString();
        specifications = in.readString();
        user_id = in.readString();
        category = in.readParcelable(MCategory.class.getClassLoader());
    }

    public static final Creator<MProduct> CREATOR = new Creator<MProduct>() {
        @Override
        public MProduct createFromParcel(Parcel in) {
            return new MProduct(in);
        }

        @Override
        public MProduct[] newArray(int size) {
            return new MProduct[size];
        }
    };

    public MProduct() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(categoryId);
        dest.writeString(price);
        dest.writeString(cost_price);
        dest.writeString(type);
        dest.writeByte((byte) (is_published == null ? 0 : is_published ? 1 : 2));
        dest.writeString(product_attributes);
        dest.writeString(variant_attributes);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeString(description);
        dest.writeString(specifications);
        dest.writeString(user_id);
        dest.writeParcelable(category, flags);
    }
}
