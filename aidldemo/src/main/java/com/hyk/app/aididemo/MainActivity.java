package com.hyk.app.aididemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.hyk.app.servicedemo.IMyAidlInterface;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity-aidl";
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
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

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
//                远程启动服务
                Intent intent = new Intent();
                intent.setAction("com.hyk.myservice");
                //5.0+
                Intent it = new Intent(IntentUtil.createExplicitFromImplicitIntent(getApplicationContext(), intent));
                startService(it);
                break;
            case R.id.stop:
                Intent intent1 = new Intent();
                intent1.setAction("com.hyk.myservice");
                //5.0+
                Intent it1 = new Intent(IntentUtil.createExplicitFromImplicitIntent(getApplicationContext(), intent1));
                stopService(it1);
                break;
            case R.id.bind:
                Intent intent2 = new Intent();
                intent2.setAction("com.hyk.myservice");
                //5.0+
                Intent it2 = new Intent(IntentUtil.createExplicitFromImplicitIntent(getApplicationContext(), intent2));
                bindService(it2, connection, BIND_AUTO_CREATE);
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
