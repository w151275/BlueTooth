package com.ingwu.bluetoothclient.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ingwu.bluetoothclient.R;

/**
 * Created by zhangqibo on 2015/7/3.
 */
public class LoadingDialog extends Dialog {

    public TextView mContentTextView;

    public LoadingDialog(Context context) {
        this(context, R.style.CommonDialog);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    public LoadingDialog create(String message) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.commonui_dialog_loading, null);
        addContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setCanceledOnTouchOutside(false);
        setContentView(layout);
        return this;
    }
}
