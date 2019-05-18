package com.eb.onebandhan.product.model;

import java.io.File;
import java.util.List;

import lombok.Data;

@Data
public class MImageServer {
    private String id;
    private String productId;
    private String url;
    private File file;

    private List<MImageServer> image;
}
