package com.github.abcmmee.wifiproxy;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String HOSTNAME = "hostname";
    private static final String PORT = "port";

    private EditText mInput_host;
    private EditText mInput_port;
    private Button mConnect;
    private Button mReset;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        initializeValues();
        setViewListener();
    }

    private void findViews() {
        mInput_host = (EditText) findViewById(R.id.editText_host);
        mInput_port = (EditText) findViewById(R.id.editText_port);
        mConnect = (Button) findViewById(R.id.button_connect);
        mReset = (Button) findViewById(R.id.button_reset);
    }

    private void initializeValues() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mInput_host.setText(sp.getString(HOSTNAME, ""));
        mInput_port.setText(sp.getString(PORT, ""));
    }

    private void setViewListener() {
        mConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String host = mInput_host.getText().toString();
                int port = Integer.valueOf(mInput_port.getText().toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    WifiProxyUtils.setWifiProxy5(MainActivity.this, host, port);
                } else {
                    WifiProxyUtils.setWifiProxy4(MainActivity.this, host, port);
                }
                finish();
            }
        });

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    WifiProxyUtils.unsetWifiProxy5(MainActivity.this);
                } else {
                    WifiProxyUtils.unsetWifiProxy4(MainActivity.this);
                }
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        String hostname = mInput_host.getText().toString();
        String port = mInput_port.getText().toString();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(HOSTNAME, hostname);
        editor.putString(PORT, port);
        editor.apply();
    }
}
