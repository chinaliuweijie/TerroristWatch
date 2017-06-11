package com.terroristwatch.android.presenter;

import com.lzy.okgo.OkGo;
import com.terroristwatch.android.model.BaseModel;
import com.terroristwatch.android.model.OnLoadDataListener;
import com.terroristwatch.android.model.SimpleModel;
import com.terroristwatch.android.view.IBaseView;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2017/4/14.
 */

public class SimplePresenter implements BasePresenter,OnLoadDataListener {

   private IBaseView iBaseView;
   private BaseModel baseModel;


   public SimplePresenter(IBaseView iBaseView){
       this.iBaseView = iBaseView;
       baseModel = new SimpleModel();
   }


    @Override
    public void startLoad(String uri, LinkedHashMap<String,String> linkedHashMap) {
        this.iBaseView.showProgress();
        baseModel.loadData(uri,linkedHashMap,this);
    }


    @Override
    public void onSuccess(String uri,String baseBean) {
        try {
            this.iBaseView.hideProgress();
            this.iBaseView.initData(baseBean);
            //可以取消同一个tag的
            OkGo.getInstance().cancelTag(uri);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(String msg, Exception e,String url) {
        this.iBaseView.showLoadFailMsg(e);
        //可以取消同一个tag的
        OkGo.getInstance().cancelTag(url);
    }
}
