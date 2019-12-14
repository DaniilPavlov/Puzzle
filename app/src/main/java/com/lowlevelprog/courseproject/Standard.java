package com.lowlevelprog.courseproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Standard extends AppCompatActivity {
    boolean soundIsOff;

    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);

        // music
        soundIsOff = Home.soundIsOff;
        if (soundIsOff) {
            doUnbindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            stopService(music);
        } else {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
        }

        Button s1 = findViewById(R.id.but_s1);
        s1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toS1;
                        toS1 = new Intent(Standard.this, Puzzle.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        toS1.putExtra("strName", "1");
                        startActivity(toS1);
                    }
                }
        );
        Button s2 = findViewById(R.id.but_s2);
        s2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toS2;
                        toS2 = new Intent(Standard.this, Puzzle.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        toS2.putExtra("strName", "2");
                        startActivity(toS2);
                    }
                }
        );
        Button s3 = findViewById(R.id.but_s3);
        s3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toS3;
                        toS3 = new Intent(Standard.this, Puzzle.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        toS3.putExtra("strName", "3");
                        startActivity(toS3);
                    }
                }
        );
    }
}