package com.terroristwatch.android.model;

/**
 * Created by Administrator on 2017/4/14.
 */

public interface OnLoadDataListener {
    void onSuccess(String url, String baseBean);

    void onFailure(String msg, Exception e, String url);
}
