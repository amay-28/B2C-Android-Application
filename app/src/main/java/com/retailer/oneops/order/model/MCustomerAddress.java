package com.retailer.oneops.order.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

/**
 * Created by Sumit Yadav on 6/6/19.
 */
@Data
public class MCustomerAddress implements Parcelable {
    String city,name,state,pincode,locality,addressLine1,addressLine2,mobileNumber;

    protected MCustomerAddress(Parcel in) {
        city = in.readString();
        name = in.readString();
        state = in.readString();
        pincode = in.readString();
        locality = in.readString();
        addressLine1 = in.readString();
        addressLine2 = in.readString();
        mobileNumber = in.readString();
    }

    public static final Creator<MCustomerAddress> CREATOR = new Creator<MCustomerAddress>() {
        @Override
        public MCustomerAddress createFromParcel(Parcel in) {
            return new MCustomerAddress(in);
        }

        @Override
        public MCustomerAddress[] newArray(int size) {
            return new MCustomerAddress[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(name);
        dest.writeString(state);
        dest.writeString(pincode);
        dest.writeString(locality);
        dest.writeString(addressLine1);
        dest.writeString(addressLine2);
        dest.writeString(mobileNumber);
    }
}
