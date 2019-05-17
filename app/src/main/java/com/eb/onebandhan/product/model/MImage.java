package com.eb.onebandhan.product.model;

import lombok.Data;

@Data
public class MImage {
    private String id;
    private String productId;
    private String url;
    private boolean isLocal;
}
