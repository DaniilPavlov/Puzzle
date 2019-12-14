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

public class Home extends AppCompatActivity {
    static boolean soundIsOff = false;
    HomeWatcher mHomeWatcher;
    // variables for music
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
    public void onResume(){
        super.onResume();
        if (mServ != null) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);
        //pausing when needed
        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }

            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                }
            }
        });
        mHomeWatcher.startWatch();


        Button menu = findViewById(R.id.but_menu);
        menu.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toMenu;
                        toMenu = new Intent(Home.this, LevelChoice.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(toMenu);
                    }
                }
        );
        Button about = findViewById(R.id.but_about);
        about.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent toAbout;
                        toAbout = new Intent(Home.this, About.class).
                                setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(toAbout);
                    }
                }
        );
    }

    public void soundOff(View view) {
        if (!soundIsOff) {
            doUnbindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            stopService(music);
            findViewById(R.id.soundRegulator).setBackgroundResource(R.drawable.volume_down);
            soundIsOff = true;
        } else {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
            findViewById(R.id.soundRegulator).setBackgroundResource(R.drawable.volume_up);
            soundIsOff = false;
        }
    }
}
