package com.infinite.drag_turn_page;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private VerticalViewPager scrollView;
    private LinearLayout divide,up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView= (VerticalViewPager) findViewById(R.id.sc);
        up= (LinearLayout) findViewById(R.id.up);
        divide= (LinearLayout) findViewById(R.id.divide_layout);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
