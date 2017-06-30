package com.ingwu.bluetoothclient;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Ing. Wu on 2017/3/28.
 */

public class OperationDesActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivBack;
    private TextView tvContinue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optdes_lay);
        initView();
        initAction();
    }

    private void initView(){

        ivBack = (ImageView) findViewById(R.id.iv_optdes_back);
        tvContinue = (TextView) findViewById(R.id.tv_optdes_contniue);
    }

    private void initAction(){
        ivBack.setOnClickListener(this);
        tvContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rel_back){
            finish();
        }else if(v.getId() == R.id.tv_optdes_contniue){
            finish();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
