package com.terroristwatch.android.model;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONObject;

import java.util.LinkedHashMap;

import okhttp3.Call;

import static android.R.attr.id;

/**
 * Created by Administrator on 2017/4/14.
 */

public class SimpleModel implements BaseModel {

    @Override
    public void loadData(final String url, LinkedHashMap<String, String> linkedHashMap, final OnLoadDataListener onLoadDataListener) {
        JSONObject jsonObject = new JSONObject(linkedHashMap);
        OkGo.post(url)//
                .tag(url)//
        //	.params("param1", "paramValue1")//  这里不要使用params，upJson 与 params 是互斥的，只有 upJson 的数据会被上传
                .upJson(jsonObject.toString())//
                .execute(new StringCallback() {
                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                    }

                    @Override
                    public void onSuccess(String s, Call call, okhttp3.Response response) {
                        //上传成功
                        onLoadDataListener.onSuccess(url,s);
                    }

                    @Override
                    public void onError(Call call, okhttp3.Response response, Exception e) {
                        super.onError(call, response, e);
                        onLoadDataListener.onFailure(id+"",e,url);
                    }
                });

    }
}
