package com.ingwu.bluetoothclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Ing. Wu on 2017/8/28.
 */

public class SplashActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvComfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_lay);
        initView();
        initAction();
    }

    private void initView(){
        tvComfirm = (TextView) findViewById(R.id.tv_splash_confirm);

    }
    private void initAction(){
        tvComfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tv_splash_confirm){
            startActivity(new Intent(this,OperationDesActivity.class));
            finish();
        }
    }
}
