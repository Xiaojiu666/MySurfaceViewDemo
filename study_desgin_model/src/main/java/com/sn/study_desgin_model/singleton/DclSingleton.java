package com.sn.study_desgin_model.singleton;


public class DclSingleton {

    private volatile static DclSingleton dclSingleton;

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
    // 3、 dclSingleton对象分配内存
    // 由于Java编译器允许处理器乱序处理，
    // JDK 1.5 之前JMM(内存模型)中Cache、寄存器到主内存回写顺序的规定，2、3顺序是无法保证的，有可能是1,3，2
    // 如果 3 先执行， 对象被创建， 切换B 线程 这时候，构造还未初始， 直接取走 dcl对象，就会出现DCL失效问题

    // JDK 1.6 之后 通过volatile关键字 ，   private volatile static DclSingleton dclSingleton; 保证 dcl 每次都从主内存中读取，
    //




    //问题 ： volatile、 JMM、 主内存是什么？



}
