package com.ingwu.bluetoothclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Ing. Wu on 2017/3/28.
 */

public class OperationDesActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout ivBack;
    private TextView tvContinue;
    private String from;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optdes_lay);
        Intent intent = getIntent();
        if(intent!= null && intent.hasExtra("FROM")){
            from = intent.getStringExtra("FROM");
        }
        initView();
        initAction();
    }

    private void initView(){

        ivBack = (RelativeLayout) findViewById(R.id.rel_op_back);
        tvContinue = (TextView) findViewById(R.id.tv_optdes_contniue);
        if("MAIN".equals(from)){
            tvContinue.setVisibility(View.GONE);
        }
    }

    private void initAction(){
        ivBack.setOnClickListener(this);
        tvContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rel_op_back){
            finish();
        }else if(v.getId() == R.id.tv_optdes_contniue){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
    }
}
