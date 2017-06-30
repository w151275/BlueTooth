package com.ingwu.bluetoothclient;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.chinatelecom.dialoglibrary.BounceEnter.BounceBottomEnter;
import com.chinatelecom.dialoglibrary.listener.OnBtnClickL;
import com.chinatelecom.dialoglibrary.widget.MaterialDialog;
import com.ingwu.bluetoothclient.util.CusBtnBgUtil;
import com.ingwu.bluetoothclient.util.SPUtil;
import com.ingwu.bluetoothclient.view.LoadingDialog;

public class BaseActivity extends Activity {
    private static final String TAG = BaseActivity.class.getSimpleName();
    protected CusBtnBgUtil cusBtnBgUtil;
    protected SPUtil spBaseUtil;
    private LoadingDialog mProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        spBaseUtil= SPUtil.getInstance(this);
        cusBtnBgUtil = CusBtnBgUtil.getInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
//        myApp.setLockTime(0);
    }

    protected void showDialogView(String title,String msg,String btnName){
        showDialogView(title,msg,btnName,null);
    }
    protected void showDialogView(String title,String msg,String btnName,OnBtnClickL btn){

        MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.btnNum(1)
                .content(msg)
                .title(title)
                .btnText(btnName)
                .showAnim(new BounceBottomEnter())
                .show();
        materialDialog.setOnBtnClickL(btn != null ? btn : new OnBtnClickL() {
            @Override
            public void onBtnClick(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        materialDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        materialDialog.setCancelable(false);
        materialDialog.setCanceledOnTouchOutside(false);
    }

    protected void showTobtnDialog(String title,String msg,String leftText,String rightText,OnBtnClickL leftListener,OnBtnClickL rightListener){
        final MaterialDialog dialog = new MaterialDialog(this);
        dialog.content(msg)
                .btnText(null == leftText ? "取消" : leftText, null == rightText ? "确定" : rightText)
                .showAnim(new BounceBottomEnter())
                .show();
        OnBtnClickL defaultListener = new OnBtnClickL() {
            @Override
            public void onBtnClick(DialogInterface dialog) {
                dialog.dismiss();
            }
        };
        dialog.setOnBtnClickL(null == leftListener ? defaultListener : leftListener,
                null == rightListener ? defaultListener : rightListener);
        if (null == title) {
            dialog.isTitleShow(false);
        } else {
            dialog.isTitleShow(true);
            if (!"".equals(title)) {
                dialog.title(title);
            }
        }
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public class ScreenLockBoardcast extends BroadcastReceiver {
        private String action = null;
        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            if (Intent.ACTION_SCREEN_ON.equals(action)) {
                // 开屏
            } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
                // 锁屏
            } else if (Intent.ACTION_USER_PRESENT.equals(action)) {
                if(!TextUtils.isEmpty(spBaseUtil.getGestureSettings(""))){
                    Intent intent1 = new Intent(BaseActivity.this, LockOffActivity.class);
                    intent1.putExtra("ISAUTH",true);
                    context.startActivity(intent1);
                }
            }
        }
    }

    public void showProgressDialog() {
        if (null == mProgressDialog) {
            mProgressDialog = new LoadingDialog(this).create("");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }
    public void closeProgressDialog(){
        if (!isFinishing() && mProgressDialog!= null){
            mProgressDialog.dismiss();
        }
    }
}
