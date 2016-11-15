package com.fcsh.lizzy.fileoperate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import static java.lang.System.arraycopy;

/**http://blog.csdn.net/xiazdong/article/details/7687439*/
/**
 *  读取:从指定的utf8格式文件中,读取一段字符,并显示出来;
 *  保存: 将用户输入的字符,存为文件,
 *  readbytes: 二进制方式读取文件,每次读入1000bytes,并写入一个新的文件
 */

public class MainActivity extends Activity {
    private Button saveButton,readButton, readBytesButton;
    private EditText filenameEt,filecontentEt;
    private Context context = this;
    String filenameIn;
    String filenameOut;
    private View.OnClickListener listener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(v==saveButton){
                String filename = filenameEt.getText().toString()+".txt";
                String filecontent = filecontentEt.getText().toString();
                FileOutputStream out = null;
                try {

                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File f = new File(Environment.getExternalStorageDirectory(), filename);
                        out = new FileOutputStream(f, true);
                        out.write(filecontent.getBytes("UTF-8"));
                    }
                    else
                    {
                        out = context.openFileOutput(filename, Context.MODE_PRIVATE);
                        out.write(filecontent.getBytes("UTF-8"));
                    }
                    filecontentEt.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally{
                    try {
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            else if(v==readButton){
                String filename = filenameEt.getText().toString(); //获得读取的文件的名称
                FileInputStream in = null;
                ByteArrayOutputStream bout = null;
                byte[]buf = new byte[1024];
                bout = new ByteArrayOutputStream();
                int length = 0;
                try {
                    byte[] content;
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File f = new File(Environment.getExternalStorageDirectory(), filename);
                        in = new FileInputStream(f);
                        while ((length = in.read(buf)) != -1) {
                            bout.write(buf, 0, length);
                        }
                        content = bout.toByteArray();
                    }
                    else
                    {
                        in = context.openFileInput(filename); //获得输入流
                        while((length=in.read(buf))!=-1){
                            bout.write(buf,0,length);
                        }
                        content = bout.toByteArray();
                    }

                    filecontentEt.setText(new String(content,"UTF-8")); //设置文本框为读取的内容
                } catch (Exception e) {
                    e.printStackTrace();
                }
                filecontentEt.invalidate(); //刷新屏幕
                try{
                    in.close();
                    bout.close();
                }
                catch(Exception e){}
            }
            else if(v == readBytesButton){
                filenameIn = Environment.getExternalStorageDirectory()+"/" + filenameEt.getText();// "/storage/emulated/0/aaabbbccc.docx"; //获得读取的文件的名称
                filenameOut= Environment.getExternalStorageDirectory() +  "/" + filenameEt.getText() + "new";
                readFileByBytes(filenameIn);
            }
        }

    };
    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public void readFileByBytes(String fileName) {
        File file = new File(fileName);
        InputStream in = null;
        try {
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            // 一次读多个字节
            byte[] tempbytes = new byte[1024];
            int byteread = 0;
            in = new FileInputStream(fileName);
            showAvailableBytes(in);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
                //System.out.write(tempbytes, 0, byteread);
                /**
                 * 写入到其他文件。
                 */

                if(byteread < tempbytes.length)
                {
                    byte[] bytesend = new byte[byteread];
                    arraycopy(tempbytes ,0,bytesend,0,byteread);
                    appendFileMethod(bytesend );

                }
                else
                {
                    appendFileMethod(tempbytes );
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
    }
    /**
     * 显示输入流中还剩的字节数
     */
    private void showAvailableBytes(InputStream in) {
        try {
            System.out.println("当前字节输入流中的字节数为:" + in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void appendFileMethod(byte[] content) {
        try {
            // 打开一个随机访问文件流，按读写方式
            RandomAccessFile randomFile = new RandomAccessFile(filenameOut, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.write(content);
            randomFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        saveButton = (Button)this.findViewById(R.id.saveButton);
        readButton = (Button)this.findViewById(R.id.readButton);
        readBytesButton = (Button)this.findViewById(R.id.readBytesButton);
        filenameEt = (EditText)this.findViewById(R.id.filename);
        filecontentEt = (EditText)this.findViewById(R.id.filecontent);
        saveButton.setOnClickListener(listener);
        readButton.setOnClickListener(listener);
        readBytesButton.setOnClickListener(listener);
    }
}
