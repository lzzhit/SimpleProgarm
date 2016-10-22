package com.example.views;

import com.example.yemian.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;


public class NetworkItemView extends RelativeLayout {

    private ImageView imageView;
    private TextView textView;
    private ToggleButton toggleButton;

    public NetworkItemView(Context context) {
        this(context, null);
    }

    public NetworkItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetworkItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.network_item_layout, this);
        imageView = (ImageView)findViewById(R.id.network_item_icon);
        textView = (TextView)findViewById(R.id.network_item_text);
        toggleButton = (ToggleButton)findViewById(R.id.network_item_toggle);
    }

    public View getToggleButton(){
        return toggleButton;
    }

    public void setViewImage(int id){
        imageView.setImageResource(id);
    }

    public void setViewText(String s){
        textView.setText(s);
    }

    public void setSwitchStatus(boolean status){
        toggleButton.setChecked(status);
    }


    public void setToggleButtonListener(OnClickListener listener){
        toggleButton.setOnClickListener(listener);
    }
}
