package com.ivy.commondemo.net;

import com.ivy.commondemo.net.api.DouBanServiceApi;
import com.ivy.commondemo.net.client.OkHttp3Utils;
import com.ivy.commondemo.net.others.StringConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ivy on 2016/10/9.
 *
 * @description: 参考文章：http://blog.csdn.net/wlt111111/article/details/51455524
 */

public class Retrofit2Utils {

    private static Retrofit mRetrofit = null;
    private static String BASE_URL = "你的地址";
    private static OkHttpClient mOkHttpClient;

    //通用的
    private static Retrofit getRetrofit() {

        if (mRetrofit == null) {
            synchronized (Retrofit2Utils.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = OkHttp3Utils.getOkHttpClient();
                }
                if (mRetrofit == null) {
                    mRetrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(StringConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(mOkHttpClient)
                            .build();
                }
            }
        }
        return mRetrofit;
    }

    private static Retrofit mDouBanRetrofit = null;
    private static String BASE_DOUBAN_URL = "https://api.douban.com";

    //针对豆瓣的Retrofit
    private static Retrofit getDouBanRetrofit() {

        if (mDouBanRetrofit == null) {
            synchronized (Retrofit2Utils.class) {
                if (mOkHttpClient == null) {
                    mOkHttpClient = OkHttp3Utils.getOkHttpClient();
                }
                if (mDouBanRetrofit == null) {
                    mDouBanRetrofit = new Retrofit.Builder()
                            .baseUrl(BASE_DOUBAN_URL)
                            .addConverterFactory(StringConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(mOkHttpClient)
                            .build();
                }
            }
        }
        return mDouBanRetrofit;
    }

    private static DouBanServiceApi mDouBanServiceApi;

    public static DouBanServiceApi getServiceApi() {
        if (mDouBanServiceApi == null) {
            synchronized (Retrofit2Utils.class) {
                if (mDouBanServiceApi == null) {
                    mDouBanServiceApi = getDouBanRetrofit().create(DouBanServiceApi.class);
                }
            }
        }
        return mDouBanServiceApi;
    }
}
