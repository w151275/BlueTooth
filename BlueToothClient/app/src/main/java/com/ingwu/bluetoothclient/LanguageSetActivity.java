package com.ingwu.bluetoothclient;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Locale;
/**
 * Created by Ing. Wu on 2017/3/28.
 */

public class LanguageSetActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout ivBack;
    private TextView tvOK;
    private RelativeLayout relChinese;
    private RelativeLayout relEnglish;
    private RelativeLayout relGerman;
    private String language = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languageset_lay);
        initView();
        initAction();
    }

    private void initView(){
        tvOK = (TextView) findViewById(R.id.tv_language_ok);
        ivBack = (RelativeLayout) findViewById(R.id.rel_back);
        relChinese = (RelativeLayout) findViewById(R.id.rel_language_chinese);
        relEnglish = (RelativeLayout) findViewById(R.id.rel_language_english);
        relGerman = (RelativeLayout) findViewById(R.id.rel_language_german);
    }

    private void initAction(){
        tvOK.setOnClickListener(this);
        relChinese.setOnClickListener(this);
        relEnglish.setOnClickListener(this);
        relGerman.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(v.getId() == R.id.rel_back){
            finish();
        }else if(v.getId() == R.id.tv_language_ok){
            changeLanguage();
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if(id == R.id.rel_language_german){
            language = "GERMAN";
            relChinese.setBackgroundColor(getResources().getColor(R.color.black));
            relEnglish.setBackgroundColor(getResources().getColor(R.color.black));
            relGerman.setBackgroundColor(getResources().getColor(R.color.gray));
        }else if(id == R.id.rel_language_chinese){
            language = "CHINESE";
            relChinese.setBackgroundColor(getResources().getColor(R.color.gray));
            relEnglish.setBackgroundColor(getResources().getColor(R.color.black));
            relGerman.setBackgroundColor(getResources().getColor(R.color.black));

        }else if(id == R.id.rel_language_english){
            language = "ENGLISH";
            relChinese.setBackgroundColor(getResources().getColor(R.color.black));
            relEnglish.setBackgroundColor(getResources().getColor(R.color.gray));
            relGerman.setBackgroundColor(getResources().getColor(R.color.black));
        }
    }

    private void changeLanguage(){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言

        if("CHINESE".equals(language)){
            config.locale = Locale.CHINESE;
        }else if("ENGLISH".equals(language)){
            config.locale = Locale.ENGLISH;
        }else if("GERMAN".equals(language)){
            config.locale = Locale.GERMAN;
        }else {
            config.locale = Locale.CHINESE;
        }
        resources.updateConfiguration(config, dm);
    }
}
