package com.view.processcommunication.Client;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.view.processcommunication.Server.IMyService;

public class MyServiceProxy implements IMyService {

    private IBinder mRemote;

    public MyServiceProxy(android.os.IBinder remote) {
        mRemote = remote;
    }

    public java.lang.String getInterfaceDescriptor() {
        return DESCRIPTOR;
    }



    @Override
    public void callHi(String str) throws RemoteException {
        Parcel data = android.os.Parcel.obtain();
        Parcel reply = android.os.Parcel.obtain();
        try {
            data.writeInterfaceToken(DESCRIPTOR);
            data.writeString(str);
            mRemote.transact(TRANSACTION_say, data, reply, 0);//通过获取的mRemote（即service在本地的代理）发送命令
            reply.readException();
        } finally {
            reply.recycle();
            data.recycle();
        }
    }

    @Override
    public IBinder asBinder() {
        return mRemote;
    }
}
