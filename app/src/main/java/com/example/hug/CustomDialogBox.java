package com.example.hug;


import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class CustomDialogBox extends Dialog {

    public Activity c;
    public String msg;

    public CustomDialogBox(Activity a,String msg) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_custom_dialog);
        TextView title = findViewById(R.id.title);

        title.setText(msg);

    }

}

