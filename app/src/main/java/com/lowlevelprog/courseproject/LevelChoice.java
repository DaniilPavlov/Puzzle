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

public class LevelChoice extends AppCompatActivity {

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
        setContentView(R.layout.activity_levelchoice);

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

        Button standard = findViewById(R.id.but_standard);
        standard.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toStart;
                        toStart = new Intent(LevelChoice.this, Standard.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(toStart);
                    }
                }
        );
        Button hard = findViewById(R.id.but_hard);
        hard.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toHard;
                        toHard = new Intent(LevelChoice.this, Hard.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(toHard);
                    }
                }
        );
    }
    @Override
    public void onResume(){
        super.onResume();
        if (mServ != null) {
            mServ.resumeMusic();
        }
    }
}
