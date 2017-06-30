package com.ingwu.bluetoothclient;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingwu.bluetoothclient.bean.DeviceRecord;
import com.ingwu.bluetoothclient.bean.Setting;
import com.ingwu.bluetoothclient.util.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Ing. Wu on 2017/3/28.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout ivBack;
    private TextView tvCaoZuodes;
    private TextView tvScan;
    private TextView tvShoushi;
    private TextView tvOpenFinger;

    private LinearLayout linearModifyShoushi;
    private LinearLayout linearCloseShoushi;
    private LinearLayout linearOpenFinger;
    private RelativeLayout relCusbtn;
    private RelativeLayout relLanguage;
    private SPUtil spUtil;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_layout);
        spUtil = SPUtil.getInstance(this);
        EventBus.getDefault().register(this);
        initView();
        initAction();
        setView();
    }

    private void setView() {
        if(!TextUtils.isEmpty(spUtil.getGestureSettings(""))){
            tvShoushi.setText(getResources().getString(R.string.set_modify_fingerpass));
            linearCloseShoushi.setVisibility(View.VISIBLE);
        }else {
            tvShoushi.setText(getResources().getString(R.string.set_open_fingerpass));
            linearCloseShoushi.setVisibility(View.GONE);
        }
    }

    private  void initView(){
        ivBack = (RelativeLayout) findViewById(R.id.rel_back);
//        tvCaoZuodes = (TextView) findViewById(R.id.tv_setting_caozuodes);
        tvScan = (TextView) findViewById(R.id.tv_setting_scan);
        tvShoushi = (TextView) findViewById(R.id.tv_shoushi);
        tvOpenFinger = (TextView) findViewById(R.id.tv_openfinger);
        linearCloseShoushi = (LinearLayout) findViewById(R.id.linear_setting_closeshoushi);
        linearModifyShoushi = (LinearLayout) findViewById(R.id.linear_setting_modifyshoushi);
        linearOpenFinger = (LinearLayout) findViewById(R.id.linear_setting_finger);
        relCusbtn = (RelativeLayout) findViewById(R.id.rel_set_cusbtn);
        relLanguage = (RelativeLayout) findViewById(R.id.rel_set_language);
    }
    private void initAction(){
        ivBack.setOnClickListener(this);
//        tvCaoZuodes.setOnClickListener(this);
        tvScan.setOnClickListener(this);
        linearCloseShoushi.setOnClickListener(this);
        linearModifyShoushi.setOnClickListener(this);
        linearOpenFinger.setOnClickListener(this);
        relLanguage.setOnClickListener(this);
        relCusbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(R.id.rel_back ==id){
            finish();
        }else if(R.id.tv_setting_scan == id){
            startActivity(new Intent(this,ScanEquipmentActivity.class));
        }else if(R.id.linear_setting_closeshoushi == id){
            Intent intent = new Intent(this,LockOffActivity.class);
            intent.putExtra("ISMODIFY",false);
            startActivityForResult(intent,1001);

        }else if(R.id.linear_setting_modifyshoushi == id){
            if(!TextUtils.isEmpty(spUtil.getGestureSettings(""))){
                Intent intent = new Intent(this,LockOffActivity.class);
                intent.putExtra("ISMODIFY",true);
                startActivityForResult(intent,1001);
            }else{
                Intent intent = new Intent(this,LockOnActivity.class);
                startActivityForResult(intent,1000);

            }
        }else if(R.id.linear_setting_finger == id){

        }else if(R.id.rel_set_cusbtn == id){
            startActivity(new Intent(this,CusOptionBtnActivity.class));
        }else if(R.id.rel_set_language == id){
            startActivity(new Intent(this,LanguageSetActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode == RESULT_OK){
                showShoushiView(true);
            }
        }else if(requestCode == 1001){
            if(RESULT_OK == resultCode){
                showShoushiView(false);
            }else if(resultCode == 1101){
                Intent intent = new Intent(this,LockOnActivity.class);
                intent.putExtra("ISMODIFY",true);
                startActivity(intent);
            }
        }
    }

    private void showShoushiView(boolean isShow) {
        if(isShow){
            tvShoushi.setText(getResources().getString(R.string.set_modify_fingerpass));
            linearCloseShoushi.setVisibility(View.VISIBLE);
        }else {
            tvShoushi.setText(getResources().getString(R.string.set_open_fingerpass));
            linearCloseShoushi.setVisibility(View.GONE);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeviceRecord event) {
        finish();
    }
}
