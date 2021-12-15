package com.example.handlerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {
    private TextView tvTime;
    private Handler mHandler;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTime = findViewById(R.id.tv_time);

        //demoOne();
        demoTwo();
        demoThree();
    }

    private void demoOne() {
        // handler 객체 생성
        mHandler = new Handler(getMainLooper());

        // 1초 주기로 textview 업데이트하기
        thread = new Thread(() -> {
            while (true) {
                // 1초 대기
                SystemClock.sleep(1000);
                String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                // time update
                // ui update -> only main thread
                mHandler.post(() -> tvTime.setText(time));
            }
        });

        thread.start();
    }

    private void demoTwo() {
        mHandler = new Handler(getMainLooper(), msg -> {
            /*switch (msg.what){
                case MSG_SOMETHING:
                    break;
                ...
                ...
            }*/

            tvTime.setText(msg.obj.toString());
            return true;
        });

        // 1초 주기로 textview 업데이트하기
        thread = new Thread(() -> {
            while (true) {
                // 1초 대기
                SystemClock.sleep(1000);
                String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

                // time update
                // ui update -> only main thread
                Message msg = Message.obtain();
                msg.obj = time;

                mHandler.sendMessage(msg);
            }
        });

        thread.start();
    }

    private void demoThree() {
        thread = new Thread(() -> {
            // 1초 대기
            SystemClock.sleep(1000);
            String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

            // time update
            // ui update -> only main thread
            runOnUiThread(() -> tvTime.setText(time));
        });


    }
}
