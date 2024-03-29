package com.retailer.oneops.checkout.model;

import com.retailer.oneops.auth.model.MAddress;

import java.util.List;

import lombok.Data;

@Data
public class MOrderRequest {
    private String payment_mode;
    private String payment_confirmed;
    private int addressId;
    private MAddress customer_address;
    private String order_type;
    private String order_delivery_address_type;
    private List<MOrderLinesRequest> order_lines;
}
