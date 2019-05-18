package com.eb.onebandhan.product.model;

import com.eb.onebandhan.dashboard.model.MCollection;

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
