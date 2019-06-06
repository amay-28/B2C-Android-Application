package com.retailer.oneops.checkout.model;

import lombok.Data;

@Data
public class MOrderLinesRequest {
    private int productId;
    private int productVariantId;
}
