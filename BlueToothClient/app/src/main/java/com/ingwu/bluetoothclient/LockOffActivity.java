package com.ingwu.bluetoothclient;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ingwu.bluetoothclient.bean.Setting;
import com.ingwu.bluetoothclient.util.SPUtil;
import com.ingwu.bluetoothclient.view.GestureLockViewGroup;

/**
 * 取消手势密码
 */
public class LockOffActivity extends BaseActivity {
    private static final String TAG = LockOffActivity.class.getSimpleName();
    private boolean isModify,isAuth;
    private String from;
    private Context mContext;
    private TextView mTextView;
    private TextView tvTitle;
    private GestureLockViewGroup mGesture;
    private RelativeLayout ivBack;
    private ImageView ivPromet;
    private SPUtil spUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_off);
        spUtil =spUtil.getInstance(this);
        if(getIntent()!= null){
            isModify = getIntent().getBooleanExtra("ISMODIFY",false);
            isAuth = getIntent().getBooleanExtra("ISAUTH",false);
            from = getIntent().getStringExtra("ISFROM");
        }
         mContext = this;
        initView();
    }

    private void initView() {
        ivPromet = (ImageView) findViewById(R.id.iv_lockoff_help);
        ivPromet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogView(getString(R.string.main_dialog_tile),getString(R.string.lock_dialog_content),getString(R.string.main_dialog_postivebtn));
            }
        });
        ivBack = (RelativeLayout) findViewById(R.id.rel_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(isAuth){
            ivBack.setVisibility(View.GONE);
        }else {
            ivBack.setVisibility(View.VISIBLE);
        }
        tvTitle = (TextView) findViewById(R.id.tv_title_shoushi);
        if(isAuth){
            tvTitle.setText(getResources().getString(R.string.fingerpass_lock));
        }else {
            if(isModify){
                tvTitle.setText(getResources().getString(R.string.set_modify_fingerpass));
            }else {
                tvTitle.setText(getResources().getString(R.string.auth_fingerpass));
            }

        }
        mTextView = (TextView) findViewById(R.id.tv_prompt_lock_off);
        if(isModify){
            mTextView.setText(getResources().getString(R.string.draw_old_fingerpass));
        }else {
            mTextView.setText(getResources().getString(R.string.verify_draw_fingerpass));
        }
        mGesture = (GestureLockViewGroup) findViewById(R.id.gesture_lock_view_group_lock_off);
        mGesture.setAnswer(spUtil.getGestureSettings(""));
        mGesture.setShowPath(true);
        mGesture.setOnGestureLockViewListener(mListener);
    }

    private void gestureEvent(boolean matched) {
        if (matched) {
            if(isModify){
                setResult(1101);
                finish();
            }else if(isAuth){
                finish();
            }else {
                mTextView.setText(getResources().getString(R.string.auth_fingerpass_success));
                spUtil.setGestureSettings("");
                setResult(RESULT_OK);
                finish();
            }
        } else {
            mTextView.setText(getResources().getString(R.string.auth_fingerpass_fail));
        }
    }

    private void unmatchedExceedBoundary() {
        // 正常情况这里需要做处理（如退出或重登）
        finish();
        Toast.makeText(mContext, "错误次数太多，请重新登录", Toast.LENGTH_SHORT).show();
    }

    // 回调监听
    private GestureLockViewGroup.OnGestureLockViewListener mListener = new
            GestureLockViewGroup.OnGestureLockViewListener() {
        @Override
        public void onGestureEvent(boolean matched) {
            gestureEvent(matched);
        }

        @Override
        public void onUnmatchedExceedBoundary() {
            unmatchedExceedBoundary();
        }

        @Override
        public void onFirstSetPattern(boolean patternOk) {
        }
    };

    @Override
    public void onBackPressed() {
        if("MAIN".equals(from)){
            setResult(RESULT_OK);
            finish();
        }
    }
}
