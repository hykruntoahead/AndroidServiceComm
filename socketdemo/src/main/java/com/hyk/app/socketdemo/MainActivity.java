package com.hyk.app.socketdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hyk.app.socketdemo.biz.UdpClientBiz;

public class MainActivity extends AppCompatActivity {

    private EditText mEtMsg;
    private Button mBtnSend;
    private TextView mTvMsg;


    private UdpClientBiz mUdpClientBiz;
    private StringBuilder stringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUdpClientBiz = new UdpClientBiz();
        stringBuilder = new StringBuilder();
        initView();

        mBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = mEtMsg.getText().toString();
                if (TextUtils.isEmpty(msg)) {
                    return;
                }

                appendMsgToContent("client:" + msg + "\n");

                mUdpClientBiz.sendMsg(msg, new UdpClientBiz.OnMsgReturnedListener() {
                    @Override
                    public void onMsgReturned(final String msg) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                appendMsgToContent("server:" + msg + "\n");
                            }
                        });
                    }

                    @Override
                    public void onError(Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }
        });
    }

    private void appendMsgToContent(String msg) {
        stringBuilder.append(msg);
        mTvMsg.setText(stringBuilder);
    }

    private void initView() {
        mEtMsg = findViewById(R.id.et_msg);
        mBtnSend = findViewById(R.id.btn_send);
        mTvMsg = findViewById(R.id.tv_msg);
    }
}
