package com.sn.processcommunication.Server;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyService extends Binder implements IMyService {

    public static final String TAG = "MyService ";

    public MyService() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IMyService asInterface(android.os.IBinder obj) {
        if ((obj == null)) return null;
        android.os.IInterface iInterface = obj.queryLocalInterface(DESCRIPTOR);
        if ((iInterface instanceof IMyService)){
            return ((IMyService) iInterface);
        }
        return null;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION: {
                reply.writeString(DESCRIPTOR);
                return true;
            }
            case TRANSACTION_say: {//响应命令
                data.enforceInterface(DESCRIPTOR);
                String str = data.readString();
                sayHello(str);
                reply.writeNoException();
                return true;
            }}
        return super.onTransact(code, data, reply, flags);
    }

    private void sayHello(String str) {
        Log.e(TAG, "sayHello , " + str);
    }

    @Override
    public void callHi(String str) throws RemoteException {
        Log.e(TAG, "callHi , " + str);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }
}
