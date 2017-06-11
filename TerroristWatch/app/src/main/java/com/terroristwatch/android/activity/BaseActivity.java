package com.terroristwatch.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.terroristwatch.android.R;
import com.terroristwatch.android.global.MyApplication;
import com.terroristwatch.android.presenter.SimplePresenter;
import com.terroristwatch.android.runtimepermissions.PermissionsManager;
import com.terroristwatch.android.runtimepermissions.PermissionsResultAction;
import com.terroristwatch.android.utils.ToastUtils;
import com.terroristwatch.android.view.IBaseView;
import com.terroristwatch.android.widget.customdialog.LoadingView;
import com.terroristwatch.android.widget.customdialog.SweetAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

import butterknife.ButterKnife;


public class BaseActivity extends AppCompatActivity implements IBaseView,View.OnClickListener{

    public TextView tvCenter;
    public ImageView ivLeft;
    public ImageView ivRight;
    public ImageView ivRightBig2;
    public TextView tvRight;
    public ImageView ivRightBig;
    public RelativeLayout rlRightBnt;
    public ImageView ivRightRed;
    public MyApplication app;
    private SweetAlertDialog loadingDialog;
   // private ProgressBar pbBase;
    private LoadingView spinView;
    private TextView tvError;
    private SimplePresenter presenter;
    FrameLayout flContent ;


    @Override
    public void setContentView(int layoutResID) {
        this.app = (MyApplication) this.getApplication();
        // 得到包含了title的布局
        View baseView = getLayoutInflater().inflate(R.layout.app_bar_main, null);
        // 得到存放内容的控件
         flContent = (FrameLayout) baseView
                .findViewById(R.id.flContent);
        // 加载子类setContentView中传递进来的布局文件
        View childView = getLayoutInflater().inflate(layoutResID, null);
        // 将子类需要显示的内容放到flContent中进行显示
        flContent.addView(childView);
        // 找出title布局中的控件
        tvCenter = (TextView) baseView.findViewById(R.id.base_tvtitle);
        tvRight = (TextView) baseView.findViewById(R.id.base_tvRight);
        ivRightBig = (ImageView) baseView.findViewById(R.id.base_ivRightBig);
        ivLeft = (ImageView) baseView.findViewById(R.id.base_ivLeft);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ivRight = (ImageView) baseView.findViewById(R.id.base_ivRight);
        ivRightRed = (ImageView) baseView.findViewById(R.id.base_ivRight_red);
        rlRightBnt = (RelativeLayout) baseView.findViewById(R.id.base_rlRight);
        spinView = (LoadingView) baseView.findViewById(R.id.spinview);
        tvError = (TextView) baseView.findViewById(R.id.tv_error);
        ivRightBig2 = (ImageView) baseView.findViewById(R.id.base_ivRightBig2);
        // 设置默认状态，默认状态一般根据最常见的状态为主
        super.setContentView(baseView);
        ButterKnife.bind(this);
    }








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册一个监听连接状态的listener   所有的activity都监听
        presenter = new SimplePresenter(this);
        /**
         * 请求所有必要的权限----
         */
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
				//Toast.makeText(BaseActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
              //  Toast.makeText(BaseActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }



    public  void startLoad(String uri, LinkedHashMap<String,String> linkedHashMap){
        if(presenter!=null){
            presenter.startLoad(uri,linkedHashMap);
        }
        setAfreshAgain(uri,linkedHashMap);
    }

    /**
     * 点击屏幕重新加载
     * @param uri
     * @param linkedHashMap
     */
   private void setAfreshAgain(final String uri, final LinkedHashMap<String,String> linkedHashMap){
       tvError.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startLoad(uri,linkedHashMap);
           }
       });
   }



    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.from_left_in, R.anim.to_right_out);
        hideSoftKeyboard();
    }



    public void showLoadingDialog(String url,String loadText){
//        if(loadingDialog==null){
//            loadingDialog = new SweetAlertDialog(this);
//        }
        loadingDialog = new SweetAlertDialog(this);
        loadingDialog.setCancelOKHttpUrl(url);
        loadingDialog.setTitleText(loadText);
        loadingDialog.show();
    }

    public void setLoadingText(String text){
        if(loadingDialog!=null){
            loadingDialog.setTitleText(text);
        }
    }


    public void dismissLoadDialog(){
        if(loadingDialog != null){
            loadingDialog.cancel();
        //    loadingDialog.dismiss();
        }
    }

    @Override
    public void showProgress() {
       // pbBase.setVisibility(View.VISIBLE);
        spinView.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        flContent.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
       // pbBase.setVisibility(View.GONE);
        spinView.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
        flContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData(String data) {
        flContent.setAlpha(1);
    }

    @Override
    public void showLoadFailMsg(Exception e) {
      //  pbBase.setVisibility(View.GONE);
        dismissLoadDialog();
        spinView.setVisibility(View.GONE);
     //   tvError.setVisibility(View.VISIBLE);
        // 设置子view 不可用
        flContent.setVisibility(View.VISIBLE);
        flContent.setAlpha(0.3f);
        disableSubControls(flContent);
        ToastUtils.showLongToast(this,"无法连接到网络，请检查网络设置");
        try {
            JSONObject js = new JSONObject();
            js.put("code",100);
            this.initData(js.toString());
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) throws NullPointerException{

    }

    protected void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (this.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (this.getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // RefWatcher refWatcher = MyApplication.getRefWatcher(this);
       // refWatcher.watch(this);
    }


    /**
     * 遍历布局，并禁用所有子控件
     *
     * @param viewGroup
     *            布局对象
     */
    public  void disableSubControls(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View v = viewGroup.getChildAt(i);
            if (v instanceof ViewGroup) {
                if (v instanceof Spinner) {
                    Spinner spinner = (Spinner) v;
                    spinner.setClickable(false);
                    spinner.setEnabled(false);
                } else if (v instanceof ListView) {
                    ((ListView) v).setClickable(false);
                    ((ListView) v).setEnabled(false);
                } else {
                    disableSubControls((ViewGroup) v);
                }
            } else if (v instanceof EditText) {
                ((EditText) v).setEnabled(false);
                ((EditText) v).setClickable(false);
            } else if (v instanceof Button) {
                ((Button) v).setEnabled(false);
                ((Button) v).setClickable(false);
            }else if(v instanceof TextView){
                ((TextView) v).setEnabled(false);
                ((TextView) v).setClickable(false);
            }
        }
    }
}
