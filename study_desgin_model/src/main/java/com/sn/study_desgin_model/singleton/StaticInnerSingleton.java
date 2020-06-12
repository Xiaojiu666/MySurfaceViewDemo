package com.sn.study_desgin_model.singleton;

import android.text.Layout;
import android.view.LayoutInflater;

public class StaticInnerSingleton {


    /**
     * 静态内部类单例
     * 第一次加载StaticInnerSingleton 类时并不会初始化 mStaticInnerSingleton
     * 在调用get方法时，才会导致mStaticInnerSingleton 实例化
     * 线程安全、对象唯一性、延迟了单例的实例化
     */
    public StaticInnerSingleton() {
        LayoutInflater.from().inflate()
    }

    public static StaticInnerSingleton getInstance() {
        return SingletonHodler.mStaticInnerSingleton;
    }

    private static class SingletonHodler {
        // 只初始化一次 不会被改变
        private static final StaticInnerSingleton mStaticInnerSingleton = new StaticInnerSingleton();
    }
}
