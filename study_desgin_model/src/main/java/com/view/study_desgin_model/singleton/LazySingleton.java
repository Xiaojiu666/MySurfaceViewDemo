package com.view.study_desgin_model.singleton;

public class LazySingleton {

    public static LazySingleton lazySingleton;

    public  LazySingleton(){}

    //保证同步，通过synchronized 保证多线程正常
    public static synchronized LazySingleton getInstance() {
        if (lazySingleton == null) {
            lazySingleton = new LazySingleton();
        }
        return lazySingleton;
    }


}
