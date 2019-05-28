package com.retailer.oneops.product.model;

import java.io.File;
import java.util.List;

import lombok.Data;

@Data
public class MImage {
    private String id;
    private String productId;
    private String url;
    private File file;
    private boolean isLocal;

    private List<MImage> image;
}
