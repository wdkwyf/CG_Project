package edu.cg.wuyufei.util;

import android.app.Application;
import android.content.Context;

/**
 * Created by wuyufei on 15/10/3.
 */
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getContext() {
        return MyApplication.context;
    }
}
