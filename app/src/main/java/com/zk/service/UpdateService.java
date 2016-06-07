package com.zk.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.zk.bean.bin;
import com.zk.database.SqlUtil;
import com.zk.database.TAG;

import java.util.Timer;
import java.util.TimerTask;

public class UpdateService extends Service {

    private CommandReceiver cmdReceiver;
    public UpdateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Timer timer = new Timer();
        timer.schedule(new GetRecentlyData() , 0 , 1000);
        IntentFilter filter = new IntentFilter() ;
        filter.addAction("AAAA");
        registerReceiver(cmdReceiver , filter);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class GetRecentlyData extends TimerTask {

        @Override
        public void run() {
            if (TAG.MYSQL_CONNECT_FLAG) {
                bin b  = SqlUtil.GetMostNewData();
                Log.w("result", b.toString());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(cmdReceiver);

    }

    //接受广播
    private  class CommandReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int cmd = intent.getIntExtra("cmd" , -1) ;

            if (cmd == TAG.CMD_STOP_SERVICE) {
                stopSelf(); //停止服务
            }
        }
    }

}
