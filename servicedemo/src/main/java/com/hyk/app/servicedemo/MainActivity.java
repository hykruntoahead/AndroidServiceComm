package com.hyk.app.servicedemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private boolean isBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            IMyAidlInterface iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);

            try {
                iMyAidlInterface.showProgress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

//            MyService.MyBinder mb = (MyService.MyBinder) service;
//
//            int step = mb.getProgress();
//
//            Log.d(TAG, "onServiceConnected: --当前进度:" + step);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: ");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void operate(View view) {
        switch (view.getId()) {
            case R.id.start:
                //启动服务
                //如果服务已创建，后续启动服务,操作的都是同一个服务，不会重新创建
                Intent intent = new Intent(this, MyService.class);
                startService(intent);
                break;
            case R.id.stop:
                Intent intent1 = new Intent(this, MyService.class);
                stopService(intent1);
                break;
            case R.id.bind:
                //绑定服务 -- 可对Service相关进度进行监控
                //如果服务不存在:onCreate---> onBinder--->onUnBinder--->onDestroy
                //如果服务已经存在：那么bindService方法只能使onBinder调用；unbindService方法只能使onUNBinder调用
                Intent intent2 = new Intent(this, MyService.class);
                bindService(intent2, connection, BIND_AUTO_CREATE);
                isBinder = true;

                break;
            case R.id.unbind:
                if (isBinder) {
                    unbindService(connection);
                    isBinder = false;
                }
                break;

        }
    }
}
