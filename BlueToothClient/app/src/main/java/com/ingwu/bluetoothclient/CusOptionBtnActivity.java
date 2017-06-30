package com.ingwu.bluetoothclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingwu.bluetoothclient.adapter.CusBtnAdapter;
import com.ingwu.bluetoothclient.bean.CusBtnBean;
import com.ingwu.bluetoothclient.util.CusBtnBgUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ing. Wu on 2017/3/28.
 */

public class CusOptionBtnActivity extends BaseActivity implements View.OnClickListener {
    private ImageView ivUP,ivDown;
    private RelativeLayout ivBack;
    private RelativeLayout relUP;
    private RelativeLayout relDown;
    private TextView tvUp,tvDown;
    private List<CusBtnBean> list;
    private String upName = "",downName="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cusoption_btn_lay);
        cusBtnBgUtil = CusBtnBgUtil.getInstance(this);
        list = cusBtnBgUtil.get();
        initView();
        initAction();
    }

    private void initView(){
        ivBack = (RelativeLayout) findViewById(R.id.rel_back);
        relDown = (RelativeLayout) findViewById(R.id.rel_cus_down);
        relUP = (RelativeLayout) findViewById(R.id.rel_cus_up);
        ivUP = (ImageView) findViewById(R.id.iv_cus_up);
        ivDown = (ImageView) findViewById(R.id.iv_cus_down);
        tvDown = (TextView) findViewById(R.id.tv_cus_down);
        tvUp = (TextView) findViewById(R.id.tv_cus_up);

    }

    private void initAction(){
        relUP.setOnClickListener(this);
        relDown.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        upName = spBaseUtil.getUpbtnPos();
        downName = spBaseUtil.getDownbtnPos();
        tvUp.setText(getString(R.string.cus_btn_up));
        ivUP.setBackgroundResource(R.mipmap.operationicon_up);
        tvDown.setText(getString(R.string.cus_btn_down));
        ivDown.setBackgroundResource(R.mipmap.operationicon_down);
        for(CusBtnBean cusbtnEnum:list){
            if(upName.equals(cusbtnEnum.getName())){
                tvUp.setText(cusbtnEnum.getName());
                ivUP.setBackgroundResource(cusbtnEnum.getDefaultId());
            }
            if(downName.equals(cusbtnEnum.getName())){
                tvDown.setText(cusbtnEnum.getName());
                ivDown.setBackgroundResource(cusbtnEnum.getDefaultId());
            }
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rel_back){
            finish();
        }else if(v.getId() == R.id.rel_cus_up ){
            Intent intent = new Intent(this,CusBtnActivity.class);
            intent.putExtra("POS","UP");
            startActivity(intent);
        }else if(v.getId() == R.id.rel_cus_down){
            Intent intent = new Intent(this,CusBtnActivity.class);
            intent.putExtra("POS","DOWN");
            startActivity(intent);

        }
    }
}
