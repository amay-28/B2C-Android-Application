package com.eb.onebandhan.product.presenterinterface;

import com.eb.onebandhan.auth.model.MCategory;
import com.eb.onebandhan.auth.model.MUser;

import java.util.Map;

public interface DialogPresenterInterface {
    void getCategoryListTask(Map<String,String> map);
}
