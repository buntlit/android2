package com.example.sms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_CODE = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSms();
            }

        });

    }

    public void sendSms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE);
                }
                return;
            }
        }
        SmsManager.getDefault().sendTextMessage("tel:" + ((EditText) findViewById(R.id.enter_number)).getText().toString(),
                null,
                ((EditText)findViewById(R.id.enter_message)).getText().toString(),
                null,
                null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int index = -1;
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i] == Manifest.permission.SEND_SMS) {
                index = i;
                break;
            }
        }
        if (index == -1)
            return;
        if (requestCode == REQUEST_CODE && grantResults[index] == PackageManager.PERMISSION_GRANTED)
            sendSms();
    }
}
