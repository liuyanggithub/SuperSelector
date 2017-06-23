package com.ly.selector;

import android.app.Application;
import android.content.Context;

import com.ly.selector.util.ToastUtils;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;

/**
 * <Pre>
 *     应用上下文
 * </Pre>
 *
 * @author 刘阳
 * @version 1.0
 *          <p>
 *          Create by 2017/5/17 10:53
 */

public class MyApplication extends Application {
    private static MyApplication instance;

    public static Context getAppContext(){
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ToastUtils.register(this);
        //初始化日志工具
        Logger.init().logLevel(LogLevel.FULL);
        //LeakCanary检测OOM
        LeakCanary.install(this);
    }
}
