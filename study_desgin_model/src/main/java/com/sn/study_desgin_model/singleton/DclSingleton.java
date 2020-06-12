package com.sn.study_desgin_model.singleton;


public class DclSingleton {

    private static DclSingleton dclSingleton;

    /**
     * DCL  优点:既能保证需要时调用， 又能保证线程安全
     *          资源利用率高，第一次执行get方法时，单例才会被实例化，效率高
     *      缺点:第一次加载时反应稍慢，也由于Java内存模型的原因会偶尔DLC 失效，
     */
    public DclSingleton() {

    }

    //保证同步，通过synchronized 保证多线程正常
    public static DclSingleton getInstance() {
        if (dclSingleton == null) {
            // 1、非空判断: 避免不必要的同步
            synchronized (DclSingleton.class) {
                //2、非空:初次创建实例
                if (dclSingleton == null)
                    dclSingleton = new DclSingleton();
            }
        }
        return dclSingleton;
    }
    //  dclSingleton = new DclSingleton() 流程
    // 1、 给 DclSingleton的实例分配内存
    // 2、 调用DclSingleton的构造函数，初始化成员字段
    // 3、


}
