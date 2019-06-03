package com.retailer.oneops.checkout.model;

import java.util.List;

import lombok.Data;

@Data
public class MCartDetail {
    private int id;
    private int userId;
    private String created_at;
    private String updated_at;
    private List<MCart> cart_lines;

}
