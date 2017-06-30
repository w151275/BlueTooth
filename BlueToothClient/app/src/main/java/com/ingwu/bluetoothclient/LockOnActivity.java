package com.ingwu.bluetoothclient;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingwu.bluetoothclient.bean.Setting;
import com.ingwu.bluetoothclient.util.SPUtil;
import com.ingwu.bluetoothclient.view.GestureLockViewGroup;

/**
 * 设置手势密码
 */
public class LockOnActivity extends BaseActivity {
    private static final String TAG = LockOnActivity.class.getSimpleName();
    private TextView mTextView;
    private TextView tvTitle;
    private boolean isModify;
    private GestureLockViewGroup mGesture;
    private RelativeLayout ivBack;
    private SPUtil spUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_on);
        spUtil = SPUtil.getInstance(this);
        isModify = getIntent().getBooleanExtra("ISMODIFY",false);
        initView();
    }

    private void initView() {
        ivBack = (RelativeLayout) findViewById(R.id.rel_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle = (TextView) findViewById(R.id.tv_title_shoushi);
        if(isModify){
            tvTitle.setText(getResources().getString(R.string.set_modify_fingerpass));
        }else {
            tvTitle.setText(getResources().getString(R.string.set_fingerpass));
        }
        mTextView = (TextView) findViewById(R.id.tv_prompt_lock_on);
        mTextView.setText(getResources().getString(R.string.draw_fingerpass));
        mGesture = (GestureLockViewGroup) findViewById(R.id.gesture_lock_view_group_lock_on);
        mGesture.isFirstSet(true);
        mGesture.setUnMatchExceedBoundary(10000);
        mGesture.setOnGestureLockViewListener(mListener);
    }

    private void gestureEvent(boolean matched) {
        if (matched) {
            mTextView.setText(getResources().getString(R.string.set_fingerpass_success));
//            Setting setting = new Setting(mGesture.getChooseStr(),Setting.SHOW_PATH);
//            SPUtil.setGestureSettings(this,setting);
            spUtil.setGestureSettings(mGesture.getChooseStr());
            setResult(RESULT_OK);
            finish();
        } else {
            mTextView.setText(getResources().getString(R.string.set_fingerpass_no_equal));
        }
    }

    private void firstSetPattern(boolean patternOk) {
        if (patternOk) {
            mTextView.setText(getResources().getString(R.string.set_fingerpass_again));
        } else {
            mTextView.setText(getResources().getString(R.string.set_fingerpass_four_point));
        }
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
        }

        @Override
        public void onFirstSetPattern(boolean patternOk) {
            firstSetPattern(patternOk);
        }
    };
}
