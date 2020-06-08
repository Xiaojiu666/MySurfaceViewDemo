package com.sn.processcommunication.Server;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IMyService extends IInterface {
    //DESCRIPTOR是唯一标识
    static final java.lang.String DESCRIPTOR = "com.sn.processcommunication.Server";
    //TRANSACTION_say是binder通信的cmd
    static final int TRANSACTION_say = android.os.IBinder.FIRST_CALL_TRANSACTION;
    //service中的接口，提供给客户端用实现某些功能
    public void callHi(String str) throws RemoteException;
}
