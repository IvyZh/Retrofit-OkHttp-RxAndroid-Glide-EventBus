package com.ivy.commondemo.net.client;


import com.ivy.commondemo.base.MyApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Ivy on 2016/10/11.
 *
 * @description:
 */

public class OkHttp3Utils {
    private static OkHttpClient mOkHttpClient = null;
    private static int cacheSize = 10 << 20; // 10 MiB
    private static Cache cache = new Cache(MyApplication.getContext().getCacheDir(), cacheSize);

    private OkHttp3Utils() {//私有化构造器

    }

    public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            synchronized (OkHttpClient.class) {
                if (mOkHttpClient == null) {

                    // 日志拦截器
                    HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
                    logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    mOkHttpClient = new OkHttpClient.Builder()
                            //设置一个自动管理cookies的管理器
                            //.cookieJar(new CookiesManager())
                            .cache(cache)
                            //添加日志拦截器
                            .addInterceptor(logInterceptor)
                            //添加网络拦截器,让所有网络请求都附上你的拦截器，我这里设置了一个 token 拦截器，就是在所有网络请求的 header 加上 token 参数
                            //.addNetworkInterceptor(new CookiesInterceptor(MyApplication.getContext()))
                            .retryOnConnectionFailure(true)//方法为设置出现错误进行重新连接。
                            .connectTimeout(15, TimeUnit.SECONDS)//设置超时时间
                            .readTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return mOkHttpClient;
    }
}