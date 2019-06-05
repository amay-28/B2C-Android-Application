package com.retailer.oneops.checkout.model;

import com.retailer.oneops.auth.model.MAddress;

import java.util.List;

import lombok.Data;

@Data
public class MOrder {
    private int id;
    private int userId;
    private String token;
    private String status;
    private String payment_mode;
    private String payment_confirmed;
    private int addressId;
    private String created_at;
    private String updated_at;
    private MAddress customer_address;
    private String order_type;
    private String order_delivery_address_type;
    private List<MOrderLines> order_lines;
}
