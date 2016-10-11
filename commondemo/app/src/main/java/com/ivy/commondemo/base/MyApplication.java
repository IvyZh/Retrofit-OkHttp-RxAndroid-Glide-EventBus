package com.ivy.commondemo.base;

import android.app.Application;

import com.ivy.commondemo.net.client.OkHttp3Utils;

import okhttp3.OkHttpClient;

/**
 * Created by Ivy on 2016/10/11.
 *
 * @description:
 */

public class MyApplication extends Application {
    private static MyApplication mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        initOkHttp();//初始化OkHttp

    }

    /**
     * 初始化单例OkHttpClient对象
     */
    private void initOkHttp() {
        OkHttpClient client = OkHttp3Utils.getOkHttpClient();
    }

    public static MyApplication getContext() {
        return mContext;
    }

}
