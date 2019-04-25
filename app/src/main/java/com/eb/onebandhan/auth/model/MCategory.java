package com.eb.onebandhan.auth.model;

import java.util.List;

import lombok.Data;

/*-------------- used for category -- sub category--- there sub category---------------------*/
@Data
public class MCategory {
    private String id;
    private String parentId;
    private String name;
    private String slug;
    private String treeId;
    private String image;
    private String level;
    private String created_at;
    private String updated_at;
    private List<MCategory> children;
    private Boolean isSelected=false;
}
