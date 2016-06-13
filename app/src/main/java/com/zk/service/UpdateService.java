package com.zk.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zk.bean.bin;
import com.zk.database.SqlUtil;
import com.zk.database.TAG;
import com.zk.event.EventAA;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateService extends Service {

    private CommandReceiver cmdReceiver;
    private OkHttpClient client = new OkHttpClient();
    private String uri= "";
    public UpdateService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Timer timer = new Timer();
    //    timer.schedule(new GetRecentlyData() , 0 , 1000);
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
            /*
            此处联网请求数据
            */
            enqueue();
        }
    }

    private void enqueue() {

        Request request = new Request.Builder().url(uri).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                EventBus.getDefault().post(new EventAA("error", EventAA.ACTION_SEND_MSG));
            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (response.isSuccessful()) {
                    String result  = response.body().string();
                    EventBus.getDefault().post(new EventAA(result , EventAA.ACTION_SEND_MSG));
                }
            }
        });
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
