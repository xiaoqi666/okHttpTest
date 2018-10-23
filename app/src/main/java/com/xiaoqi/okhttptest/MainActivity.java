package com.xiaoqi.okhttptest;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String GET_URL = "http://t.weather.sojson.com/api/weather/city/101030100";
    private TextView tvHello;
    /**
     * 1.创建OkHttpClient和Request对象
     * 2.将Request封装成Call对象
     * 3.调用Call的execute()发送同步请求
     * //发送同步请求后,就会进入阻塞状态,直到收到响应
     * 4. 推荐让 OkHttpClient 保持单例
     * 每一个 OkHttpClient 实例都拥有自己的连接池和线程池，
     * 重用这些资源可以减少延时和节省资源，
     * 如果为每个请求创建一个 OkHttpClient 实例，显然就是一种资源的浪费。
     * 共享连接池和配置信息
     */
    OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS).build();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        // syncRequest();
        yichuRequest();
    }


    /**
     * 同步GET
     */
    public void syncRequest() {
        Request request = new Request.Builder().url(GET_URL).get().build();
        final Call call = client.newCall(request);
        new Thread() {
            @Override
            public void run() {
                super.run();
                Response response = null;//Response响应报文的信息
                try {
                    response = call.execute();
                    if (response != null)
                        Log.e("TAG", "----------------" + response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 异步GET
     */
    public void yichuRequest() {
        Request request = new Request.Builder().url(GET_URL).get().build();
        final Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功,虽然不是主线程,但是可以更新UI
                Log.e("TAG", "----------------" + response.body().string());
                tvHello.setText("成功");//可以更新UI

                //false,子线程
                Log.e("TAG", "----------------" + (Looper.getMainLooper().getThread() == Thread.currentThread()));
            }
        });

    }


    private void initView() {
        tvHello = findViewById(R.id.tv_hello);
    }
}
