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

public class Hard extends AppCompatActivity {

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
        setContentView(R.layout.activity_hard);

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

        Button h1 = findViewById(R.id.but_h1);
        h1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toH1;
                        toH1 = new Intent(Hard.this, Puzzle.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        toH1.putExtra("strName", "4");
                        startActivity(toH1);
                    }
                }
        );
        Button h2 = findViewById(R.id.but_h2);
        h2.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toH2;
                        toH2 = new Intent(Hard.this, Puzzle.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        toH2.putExtra("strName", "5");
                        startActivity(toH2);
                    }
                }
        );
        Button h3 = findViewById(R.id.but_h3);
        h3.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toH3;
                        toH3 = new Intent(Hard.this, Puzzle.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        toH3.putExtra("strName", "6");
                        startActivity(toH3);
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
