package com.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button btnStart;
    private TextView textCount;
    private Button btnAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        btnStart = findViewById(R.id.btn_start);
        textCount = findViewById(R.id.textCount);
        btnAgain = findViewById(R.id.btn_again);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setVisibility(View.INVISIBLE);
                btnStart.setVisibility(View.INVISIBLE);
                textCount.setVisibility(View.VISIBLE);
                asncTsk();
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setVisibility(View.VISIBLE);
                btnStart.setVisibility(View.VISIBLE);
                textCount.setVisibility(View.INVISIBLE);
                textCount.setText("");
                btnAgain.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void asncTsk() {
        new AsyncTask<Void, Integer, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                textCount.setText("" + values[0]);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    for (int i = 1; i <= 30; i++) {

                        TimeUnit.SECONDS.sleep(1);
                        publishProgress(i);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }



            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                btnAgain.setVisibility(View.VISIBLE);
            }
        }.execute();


    }
}
