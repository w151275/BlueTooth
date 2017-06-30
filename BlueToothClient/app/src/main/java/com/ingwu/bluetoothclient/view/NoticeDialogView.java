package com.ingwu.bluetoothclient.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingwu.bluetoothclient.R;

/**
 * Created by Ing. Wu on 2017/6/12.
 */

public class NoticeDialogView  {

    private Context context;
    private ClikCallback clikCallback;

    public NoticeDialogView(Context context) {
        this.context = context;
    }

    public void setClick(ClikCallback click){
        clikCallback = click;
    }
    public void show(){
        final Dialog dialog = new Dialog(context,R.style.selectorDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_notice_lay,null);
        TextView leftbtn = (TextView) view.findViewById(R.id.tv_dialog_refuse);
        TextView rightBtn = (TextView) view.findViewById(R.id.tv_dialog_confirm);
        leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                clikCallback.click(v);
            }
        });
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                clikCallback.click(v);
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.addContentView(view,layoutParams);
        dialog.show();
    }


    public interface ClikCallback{
        void click(View v);
    }
}
