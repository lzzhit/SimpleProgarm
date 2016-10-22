package com.fcsh.lizzy.ipsocket;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

//客户端的实现
public class MainActivity extends Activity {
    private TextView text1;
    private Button but1;
    private EditText edit1;
    private final String TAG = "MainActivity";
    private String sendMesg;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text1 = (TextView) findViewById(R.id.text1);
        but1 = (Button) findViewById(R.id.but1);
        edit1 = (EditText) findViewById(R.id.edit);

        Thread myServerThread = new Thread(new MySocketServer());
        myServerThread.start();
        but1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMesg = edit1.getText().toString() + "\r\n";
                Thread myClient = new Thread(new MyClient());
                myClient.start();
            }
        });


    }

    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x11) {
                Bundle bundle = msg.getData();
                text1.append("server:" + bundle.getString("msg") + "\n");
            }
        }

    };

    class MyClient implements Runnable {
        private final String TAG = "MyClient";
        Socket socket = null;

        public void run() {
            try {
                socket = new Socket("127.0.0.1", 54321);
                //向服务器发送信息
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.println(sendMesg);
                out.close();
                socket.close();
            } catch (UnknownHostException e) {
                Log.e(TAG, "UnknownHostException" + e.toString());
            } catch (IOException e) {
                Log.e(TAG, "IOException" + e.toString());
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    class MySocketServer implements Runnable {
        private final String TAG = "MySocketServer";

        public void run() {
            try {
                ServerSocket serverSocket = new ServerSocket(54321);
                while (true) {
                    System.out.println("等待接收用户连接：");
                    //接受客户端请求
                    Socket client = serverSocket.accept();
                    try {
                        Message msg = new Message();
                        msg.what = 0x11;
                        Bundle bundle = new Bundle();
                        bundle.clear();
                        //接受客户端信息
                        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        String str = in.readLine();
                        System.out.println("read:  " + str);
                        //向服务器发送消息
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                        out.println("return	" + str);
                        in.close();
                        out.close();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    } finally {
                        client.close();
                        Log.i(TAG, "socket Close");
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "IOException :" + e.toString());
            }
        }
    }
}
