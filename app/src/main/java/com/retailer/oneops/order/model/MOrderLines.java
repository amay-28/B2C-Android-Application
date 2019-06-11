package com.retailer.oneops.order.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.retailer.oneops.productListing.model.MProduct;

import lombok.Data;

@Data
public class MOrderLines implements Parcelable {
   private int id;
   private int orderId;
   private int productId;
   private int productVariantId;
   private int quantity;
   private int amount;
   private int total_discount;
   private String couponId;
   private String sellerId;
   private String created_at;
   private String updated_at;
   private MProduct product;

   protected MOrderLines(Parcel in) {
      id = in.readInt();
      orderId = in.readInt();
      productId = in.readInt();
      productVariantId = in.readInt();
      quantity = in.readInt();
      amount = in.readInt();
      total_discount = in.readInt();
      couponId = in.readString();
      sellerId = in.readString();
      created_at = in.readString();
      updated_at = in.readString();
      product = in.readParcelable(MProduct.class.getClassLoader());
   }

   public static final Creator<MOrderLines> CREATOR = new Creator<MOrderLines>() {
      @Override
      public MOrderLines createFromParcel(Parcel in) {
         return new MOrderLines(in);
      }

      @Override
      public MOrderLines[] newArray(int size) {
         return new MOrderLines[size];
      }
   };

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(id);
      dest.writeInt(orderId);
      dest.writeInt(productId);
      dest.writeInt(productVariantId);
      dest.writeInt(quantity);
      dest.writeInt(amount);
      dest.writeInt(total_discount);
      dest.writeString(couponId);
      dest.writeString(sellerId);
      dest.writeString(created_at);
      dest.writeString(updated_at);
      dest.writeParcelable(product, flags);
   }
}