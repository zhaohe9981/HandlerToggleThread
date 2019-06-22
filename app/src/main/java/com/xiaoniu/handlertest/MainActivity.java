package com.xiaoniu.handlertest;

import android.app.Activity;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextView tv_info;
    private StringBuffer stringBuffer;
    private Handler uiHandler;
    private Handler threadHandler;
    private HandlerThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initUIHandler();
    }

    private void initView() {
        tv_info = findViewById(R.id.tv_info);
    }

    private void initUIHandler() {
        uiHandler = new MyUiHandler(MainActivity.this);
    }

    class MyUiHandler extends Handler{
        WeakReference<Activity> reference = null;
        public MyUiHandler(Activity activity) {
            reference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (reference.get() == null){
                return;
            }
            tv_info.setText((String)msg.obj);
        }
    }

    /**
     * 按钮的点击事件
     * @param view
     */
    public void onThreadTask(View view){
        collectInfo("点击Button");
        if (thread == null){
            thread = new HandlerThread("handler_thread#");
            thread.start();
            threadHandler = new Handler(thread.getLooper()){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    //子线程handler接收到消息，开始处理任务
                    collectInfo("threadHandler接收到消息开始处理");
                    SystemClock.sleep(2000);
                    //告诉UI handler任务完成的消息
                    collectInfo("子线程处理好任务了");
                }
            };
        }
        if (stringBuffer != null){
            stringBuffer.delete(0,stringBuffer.length());
        }
        //给子线程的handler消息
        collectInfo("开始给threadHandler发送消息");
        threadHandler.obtainMessage(1).sendToTarget();
    }

    /**
     * 收集信息
     * @param msg
     */
    private void collectInfo(String msg) {
        Log.d(TAG, msg);
        if (stringBuffer == null){
            stringBuffer = new StringBuffer();
        }
        stringBuffer
                .append("当前线程是：")
                .append(Thread.currentThread().getName())
                .append("\n")
                .append(msg)
                .append(DateUtil.fortmatTime(System.currentTimeMillis()))
                .append("\n")
                .append("\n");
        uiHandler.obtainMessage(1,stringBuffer.toString()).sendToTarget();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
