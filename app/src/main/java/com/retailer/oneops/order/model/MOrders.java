package com.retailer.oneops.order.model;

import com.retailer.oneops.productListing.model.MProduct;

import java.util.List;

import lombok.Data;

/**
 * Created by Sumit Yadav on 5/6/19.
 */
@Data
public class MOrders {
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
    private MCustomerAddress customer_address;
    private String order_type;

}
