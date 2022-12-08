package com.usts.fee_front;

import android.app.Application;
import android.content.Context;

/**
 * @author zdaneel
 */
public class MyApplication extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
