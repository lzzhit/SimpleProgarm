package com.example.pages;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.yemian.R;
import com.fcsh.lizzy.simplesocket.MainActivity;

/**
 * Created by Administrator on 2016/10/18.
 */

public class StartActivity extends Activity {
    private Button zhw_udp;
    private Button zhw_ip;
    private Button jq_16;
    private Button jq_36;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        zhw_udp = (Button) findViewById(R.id.zhw_udp);
        zhw_ip = (Button) findViewById(R.id.zhw_ip);
        jq_16 = (Button) findViewById(R.id.jq_16);
        jq_36 = (Button) findViewById(R.id.jq_36);


        zhw_udp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(StartActivity.this, Netconfig_Radio.class);
                startActivity(intent);
            }
        });

        zhw_ip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        jq_16.setOnClickListener(new View.OnClickListener() {//按键单击事件
            @Override

            public void onClick(View v) {

                // TODO Auto-generated method stub

                new AlertDialog.Builder(StartActivity.this).setTitle("提示")//设置对话框标题

                        .setMessage("成功！")//设置显示的内容

                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮

                            @Override

                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                // TODO Auto-generated method stub

                                //finish();

                            }

                        }).show();//在按键响应事件中显示此对话框



            }

        });
        jq_36.setOnClickListener(new View.OnClickListener() {//按键单击事件
            @Override

            public void onClick(View v) {

                // TODO Auto-generated method stub

                new AlertDialog.Builder(StartActivity.this).setTitle("提示")//设置对话框标题

                        .setMessage("成功！")//设置显示的内容

                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮

                            @Override

                            public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件

                                // TODO Auto-generated method stub

                                //finish();

                            }

                        }).show();//在按键响应事件中显示此对话框



            }

        });
    }

}
