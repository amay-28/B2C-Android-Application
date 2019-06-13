package com.retailer.oneops.auth.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;

@Data
public class MAddress implements Parcelable {
    private int id;
    private String name;
    private String mobileNumber;
    private String postalCode;
    private String addressLine1;
    private String addressLine2;
    private String locality;
    private String city;
    private String state;

    public MAddress(Parcel in) {
        id = in.readInt();
        name = in.readString();
        mobileNumber = in.readString();
        postalCode = in.readString();
        addressLine1 = in.readString();
        addressLine2 = in.readString();
        locality = in.readString();
        city = in.readString();
        state = in.readString();
    }

    public static final Creator<MAddress> CREATOR = new Creator<MAddress>() {
        @Override
        public MAddress createFromParcel(Parcel in) {
            return new MAddress(in);
        }

        @Override
        public MAddress[] newArray(int size) {
            return new MAddress[size];
        }
    };

    public MAddress() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(mobileNumber);
        dest.writeString(postalCode);
        dest.writeString(addressLine1);
        dest.writeString(addressLine2);
        dest.writeString(locality);
        dest.writeString(city);
        dest.writeString(state);
    }

   /* @Override
    public String toString() {
        return "MAddress{" +
                ", addressLine1='" + addressLine1 + '\'' + "," +
                ", addressLine2='" + addressLine2 + '\'' +
                ", city='" + city + '\'' + "-" +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }*/

    public String toString() {
        return " " + addressLine1 + ", " + "" + addressLine2 + ", " + "" + locality + ",\n" + " " + city + " -" + " " + postalCode;
    }
}
