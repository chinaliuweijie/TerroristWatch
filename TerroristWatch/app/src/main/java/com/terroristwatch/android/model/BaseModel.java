package com.terroristwatch.android.model;

import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2017/4/14.
 */
public interface BaseModel {

    void loadData(String url, LinkedHashMap<String, String> linkedHashMap, OnLoadDataListener onLoadDataListener);
}
