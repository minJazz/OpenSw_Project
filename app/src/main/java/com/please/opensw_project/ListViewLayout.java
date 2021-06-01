package com.please.opensw_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListViewLayout extends LinearLayout {
    private TextView btn;
    public ListViewLayout(Context context, ListItem item){
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.activity_main,this,true);

        btn = (EditText)findViewById(R.id.product_search);
        btn.setText(item.getText());
    }

    public void setText(String text){
        btn.setText(text);
    }



}
