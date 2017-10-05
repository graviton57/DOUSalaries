package com.havrylyuk.dou.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import com.havrylyuk.dou.ui.base.BaseActivity;
import com.havrylyuk.dou.ui.main.MainActivity;

/**
 * Created by Igor Havrylyuk on 29.09.2017.
 */

public class SplashActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }

    @Override
    protected void initUI() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
