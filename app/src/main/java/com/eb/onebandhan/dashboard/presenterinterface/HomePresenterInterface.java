package com.eb.onebandhan.dashboard.presenterinterface;

import java.util.Map;

public interface HomePresenterInterface {
    void getBannerListTask();
    void getCategoryListTask(Map<String,String> map);
    void getCollectionListTask(Map<String,String> map);
}
