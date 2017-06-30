package com.ingwu.bluetoothclient.util;

import android.content.Context;
import android.content.res.Resources;

import com.ingwu.bluetoothclient.R;
import com.ingwu.bluetoothclient.bean.CusBtnBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 初始化图片
 * Created by Ing. Wu on 2017/4/27.
 */

public class CusBtnBgUtil {
    private int[] _drawableId;
    private int[] _nameId;
    private int[] _defaultId;

    private  CusBtnBean upBean;//当前选中的按钮
    private  CusBtnBean downBean;//当前选中的按钮
    private  Resources resources;

    private static CusBtnBgUtil instance;
    public static CusBtnBgUtil getInstance(Context context){
        if(instance == null){
            synchronized (CusBtnBgUtil.class){
                instance = new CusBtnBgUtil(context);
            }
            return instance;
        }
        return instance;
    }

    public CusBtnBgUtil(Context context) {
//        _drawableId = new int[]{R.mipmap.operationicon_up,R.mipmap.operationicon_down,R.mipmap.operationicon_on,R.mipmap.operationicon_off,R.mipmap.operationicon_left,
//                R.mipmap.operationicon_right,R.mipmap.operationicon_lock,R.mipmap.operationicon_unlock,R.mipmap.operationicon_ring,R.mipmap.operationicon_light,};
//        _defaultId = new int[]{R.mipmap.operationicon_up,R.mipmap.operationicon_down,R.mipmap.operationicon_on,R.mipmap.operationicon_off,R.mipmap.operationicon_left,
//                R.mipmap.operationicon_right,R.mipmap.operationicon_lock,R.mipmap.operationicon_unlock,R.mipmap.operationicon_ring,R.mipmap.operationicon_light,};
        _drawableId = new int[]{R.mipmap.operationicon_up,R.mipmap.operationicon_down,R.mipmap.operationicon_left,
                R.mipmap.operationicon_right};
        _defaultId = new int[]{R.mipmap.operationicon_up,R.mipmap.operationicon_down,R.mipmap.operationicon_left,
                R.mipmap.operationicon_right,};
//
//        _nameId = new int[]{R.string.cus_btn_up,R.string.cus_btn_down,R.string.cus_btn_open,R.string.cus_btn_close,
//                R.string.cus_btn_left,R.string.cus_btn_right,R.string.cus_btn_lock,R.string.cus_btn_unlock,
//                R.string.cus_btn_voice,R.string.cus_btn_light};
        _nameId = new int[]{R.string.cus_btn_up,R.string.cus_btn_down,
                R.string.cus_btn_left,R.string.cus_btn_right,};
        resources = context.getResources();
    }

    public List<CusBtnBean> get(){
        List<CusBtnBean> list = new ArrayList<>();
        for(int i = 0;i<_drawableId.length;i++){
                CusBtnBean cusBtnBean = new CusBtnBean();
                cusBtnBean.setName(resources.getString(_nameId[i]));
                cusBtnBean.setId(_drawableId[i]);
                cusBtnBean.setDefaultId(_defaultId[i]);
                list.add(cusBtnBean);
        }
        return list;
    }

    public List<CusBtnBean> getUpList(String name){
        List<CusBtnBean> list = new ArrayList<>();
        for(int i = 0;i<_drawableId.length;i++){
            if(!name.equals(resources.getString(_nameId[i])) && i!=1){
                CusBtnBean cusBtnBean = new CusBtnBean();
                cusBtnBean.setName(resources.getString(_nameId[i]));
                cusBtnBean.setId(_drawableId[i]);
                cusBtnBean.setDefaultId(_defaultId[i]);
                list.add(cusBtnBean);
            }
        }
        return list;
    }

    public List<CusBtnBean> getDownList(String name){
        List<CusBtnBean> list = new ArrayList<>();
        for(int i = 0;i<_drawableId.length;i++){
            if(!name.equals(resources.getString(_nameId[i])) && i!=0){
                CusBtnBean cusBtnBean = new CusBtnBean();
                cusBtnBean.setName(resources.getString(_nameId[i]));
                cusBtnBean.setId(_drawableId[i]);
                cusBtnBean.setDefaultId(_defaultId[i]);
                list.add(cusBtnBean);
            }
        }
        return list;
    }


    public CusBtnBean getCusBtnbyName(String name){
        List<CusBtnBean> list = get();
        CusBtnBean bean = null;
        for(CusBtnBean btnBean :list){
            if(btnBean.getName().equals(name)){
                bean = btnBean;
                break;
            }
        }
        return bean;
    }

    public CusBtnBean getUpBean() {
        return upBean;
    }

    public void setUpBean(CusBtnBean bean) {
        this.upBean = bean;
    }

    public CusBtnBean getDownBean() {
        return downBean;
    }

    public void setDownBean(CusBtnBean downBean) {
        this.downBean = downBean;
    }
}
