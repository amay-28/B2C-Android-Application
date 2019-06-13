package com.retailer.oneops.order.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.retailer.oneops.auth.model.MAddress;
import com.retailer.oneops.productListing.model.MProduct;

import java.util.List;

import lombok.Data;

/**
 * Created by Sumit Yadav on 5/6/19.
 */
@Data
public class MOrders implements Parcelable {
    private int id;
    private int userId;
    private String token;
    private String status;
    private String payment_mode;
    private boolean payment_confirmed;
    private int addressId;
    private String created_at;
    private String updated_at;
    private List<MOrderLines> order_lines;
    private MAddress customer_address;
    private String order_type;

    protected MOrders(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        token = in.readString();
        status = in.readString();
        payment_mode = in.readString();
        payment_confirmed = in.readByte() != 0;
        addressId = in.readInt();
        created_at = in.readString();
        updated_at = in.readString();
        order_lines = in.createTypedArrayList(MOrderLines.CREATOR);
        customer_address = in.readParcelable(MCustomerAddress.class.getClassLoader());
        order_type = in.readString();
    }

    public static final Creator<MOrders> CREATOR = new Creator<MOrders>() {
        @Override
        public MOrders createFromParcel(Parcel in) {
            return new MOrders(in);
        }

        @Override
        public MOrders[] newArray(int size) {
            return new MOrders[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(token);
        dest.writeString(status);
        dest.writeString(payment_mode);
        dest.writeByte((byte) (payment_confirmed ? 1 : 0));
        dest.writeInt(addressId);
        dest.writeString(created_at);
        dest.writeString(updated_at);
        dest.writeTypedList(order_lines);
        dest.writeParcelable(customer_address, flags);
        dest.writeString(order_type);
    }
}
