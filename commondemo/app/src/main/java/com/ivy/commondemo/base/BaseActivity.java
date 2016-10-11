package com.ivy.commondemo.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ivy.commondemo.utils.AppManager;

import butterknife.ButterKnife;


/**
 * Created by Ivy on 2016/10/7.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);// 添加Activity到堆栈
        initView();//需要设置setContentView

        ButterKnife.bind(this);//绑定ButterKnife

        initListener();//监听

        loadData();//加载数据
    }


    /**
     * setContentView和findViewById操作(可以只用ButterKnife快捷操作)
     */
    protected abstract void initView();

    /**
     * 给控件添加点击监听事件(一般也可以使用ButterKnife)
     */
    protected abstract void initListener();

    /**
     * 加载数据的网络操作
     */
    protected abstract void loadData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }
}
