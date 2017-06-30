package com.chinatelecom.dialoglibrary;

import android.content.Context;
import android.content.DialogInterface;

import com.chinatelecom.dialoglibrary.BounceEnter.BounceBottomEnter;
import com.chinatelecom.dialoglibrary.listener.OnBtnClickL;
import com.chinatelecom.dialoglibrary.widget.MaterialDialog;

/**
 * 对话框构建
 *
 * <p>构建简单的对话框
 *
 * @author zhangluya
 * @version 1.0.0 createTime: 2016/1/29 17:04
 * @see
 */
public final class DialogBuilder {

    /**
     * 底部显示两个按钮的对话框，底部按钮显示默认文本
     *
     * @param context
     *         上下文对象，构建dialog时需要依赖Activity级别的上下文
     * @param message
     *         对话框显示的信息
     * @param onConfirmClick
     *         右边按钮的点击回调
     * @return
     */
    public static MaterialDialog build(Context context, String message, OnBtnClickL onConfirmClick) {
        return build(context, "取消", "确定", message, 2, onConfirmClick);
    }

    /**
     * 底部显示两个按钮的对话框
     *
     * @param context
     *         上下文对象，构建dialog时需要依赖Activity级别的上下文
     * @param left
     *         左边按钮的文字
     * @param right
     *         右边按钮的文字
     * @param message
     *         对话框显示的信息
     * @param onConfirmClick
     *         右边按钮的点击回调
     * @return
     */
    public static MaterialDialog build(Context context, String left, String right, String message, OnBtnClickL onConfirmClick) {
        return build(context, left, right, message, 2, onConfirmClick);
    }

    /**
     * 底部只显示一个按钮的对话框，使用默认的点击回调（点击后dismiss），使用默认的按钮显示文字
     *
     * @param context
     *         上下文对象，构建dialog时需要依赖Activity级别的上下文
     * @param message
     *         对话框显示的信息
     * @return
     */
    public static MaterialDialog build(Context context, String message) {
        return build(context, null, "确定", message, 1, null);
    }

    public static MaterialDialog buildSingleBtnDialog(Context context, String message, OnBtnClickL onConfirmClick) {
        return build(context, null, "确定", message, 1, onConfirmClick);
    }

    /**
     * 底部只显示一个按钮的对话框，使用默认的点击回调（点击后dismiss）
     *
     * @param context
     *         上下文对象，构建dialog时需要依赖Activity级别的上下文
     * @param left
     *         按钮的文字
     * @param message
     *         对话框显示的信息
     * @return
     */
    public static MaterialDialog build(Context context, String left, String message) {
        return build(context, null, left, message, 1, null);
    }

    /**
     * 底部只显示一个按钮的对话框
     *
     * @param context
     *         上下文对象，构建dialog时需要依赖Activity级别的上下文
     * @param right
     *         按钮的文字
     * @param message
     *         对话框显示的信息
     * @param l
     *         点击按钮的回调
     * @return
     */
    public static MaterialDialog build(Context context, String right, String message, OnBtnClickL l) {
        return build(context, null, right, message, 1, l);
    }

    private static MaterialDialog build(Context context, String left, String right, String message, int btnNum, OnBtnClickL onConfirmClick) {
        final MaterialDialog dialog = new MaterialDialog(context);
        dialog.content(message);
        dialog.btnNum(btnNum);
        if(btnNum == 1) {
            dialog.btnText(right);
        } else  {
            dialog.btnText(left, right);
        }
        OnBtnClickL cancelListener = new OnBtnClickL() {
            @Override
            public void onBtnClick(DialogInterface dialog) {
//                L.i("DialogBuilder", DialogBuilder.class.getSimpleName() + " :onBtnClick()>>");
                dialog.dismiss();
            }
        };
        dialog.setOnBtnClickL(cancelListener, onConfirmClick == null ? cancelListener : onConfirmClick);
        dialog.setCanceledOnTouchOutside(false);
        dialog.isTitleShow(true).showAnim(new BounceBottomEnter());
        return dialog;
    }
}
