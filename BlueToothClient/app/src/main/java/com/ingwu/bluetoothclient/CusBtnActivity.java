package com.ingwu.bluetoothclient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingwu.bluetoothclient.adapter.CusBtnAdapter;
import com.ingwu.bluetoothclient.bean.CusBtnBean;

import java.util.List;

/**
 * Created by Ing. Wu on 2017/3/28.
 */

public class CusBtnActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private RelativeLayout ivBack;
    private TextView tvOK;
    private GridView gridView;
    private CusBtnAdapter cusBtnAdapter;
    private List<CusBtnBean> list;
    private CusBtnBean bean;
    private String pos;
    private String btnName;
    private int cusPos = -1;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cusbtn_lay);
        if(getIntent()!= null){
            pos = getIntent().getStringExtra("POS");
        }
        if("UP".equals(pos)){
            btnName = spBaseUtil.getUpbtnPos();
            if(TextUtils.isEmpty(btnName)){
                btnName = "上";
            }
            list = cusBtnBgUtil.getUpList(btnName);
        }else {
            btnName = spBaseUtil.getDownbtnPos();
            if(TextUtils.isEmpty(btnName)){
                btnName = "下";
            }
            list = cusBtnBgUtil.getDownList(btnName);
        }
        initView();
        initAction();
        cusBtnAdapter = new CusBtnAdapter(this,list);
        gridView.setAdapter(cusBtnAdapter);

    }

    private void initView(){
        ivBack = (RelativeLayout) findViewById(R.id.rel_back);
        tvOK = (TextView) findViewById(R.id.tv_cusbtn_ok);
        gridView = (GridView) findViewById(R.id.grid_cus_btn);
    }

    private void initAction(){
        gridView.setOnItemClickListener(this);
        ivBack.setOnClickListener(this);
        tvOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rel_back){
            finish();
        }else if(v.getId() == R.id.tv_cusbtn_ok){
            if(cusPos != -1){
                if("UP".equals(pos)){
                    spBaseUtil.setUpbtnPos(list.get(cusPos).getName());
                }else {
                    spBaseUtil.setDownbtnPos(list.get(cusPos).getName());
                }
            }
            finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        bean = list.get(position);
        for(int i = 0;i<list.size();i++){
            bean = list.get(i);
            if(i==position){
                bean.setSelect(true);
            }else {
                bean.setSelect(false);
            }
        }
        cusPos = position;
        cusBtnAdapter.notifyDataSetChanged();
    }
}
