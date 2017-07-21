package me.inrush.italker;

import android.widget.TextView;

import butterknife.BindView;
import me.inrush.common.app.Activity;

public class MainActivity extends Activity {
    @BindView(R.id.txt_test)
    TextView txtTest;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        txtTest.setText("Hello IM");
    }
}
