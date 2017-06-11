package com.terroristwatch.android.presenter;

import com.lzy.okgo.OkGo;
import com.terroristwatch.android.model.OnLoadDataListener;
import com.terroristwatch.android.model.SimpleModel;
import com.terroristwatch.android.view.ILibelInfoView;

import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2017/4/19.
 */

public class LibelInfoPresenter implements BasePresenter,OnLoadDataListener {

    private ILibelInfoView iLibelInfoView;
    private SimpleModel baseModel;

    public LibelInfoPresenter(ILibelInfoView iBaseView){
        this.iLibelInfoView = iBaseView;
        baseModel = new SimpleModel();
    }


    @Override
    public void startLoad(String uri, LinkedHashMap<String, String> linkedHashMap) {
        iLibelInfoView.showLibelProgress(uri);
        baseModel.loadData(uri,linkedHashMap,this);
    }

    @Override
    public void onSuccess(String uri,String baseBean) {
        iLibelInfoView.hideLibelProgress(uri);
        iLibelInfoView.getLibelData(uri,baseBean);
        OkGo.getInstance().cancelTag(uri);
    }

    @Override
    public void onFailure(String msg, Exception e,String url) {
        iLibelInfoView.showLoadFailMsg(e);
        //可以取消同一个tag的
        OkGo.getInstance().cancelTag(url);
    }
}
