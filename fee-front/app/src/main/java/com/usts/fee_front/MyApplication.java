package com.usts.fee_front;

import android.app.Application;
import android.content.Context;

import com.usts.fee_front.pojo.Student;

/**
 * @author zdaneel
 */


public class MyApplication extends Application {
    private static Context context;
    private static MyApplication instance;
    private static Student student;
    @Override
    public void onCreate() {
        super.onCreate();
        //获取去全局context
        context = getApplicationContext();
        //实例个人Application类
        instance = this ;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static void setInstance(MyApplication instance) {
        MyApplication.instance = instance;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        MyApplication.context = context;
    }

    public static Student getStudent() {
        return student;
    }

    public static void setStudent(Student student) {
        //这一步很重要
        MyApplication.student = student;
    }
}

