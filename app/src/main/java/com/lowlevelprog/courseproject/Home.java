package com.lowlevelprog.courseproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {
    MenuItem filterAction;
    View menuItemView;
    static boolean soundIsOff;
    private boolean mIsBound = false;
    private MusicService mServ;
    HomeWatcher mHomeWatcher;
    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder binder) {
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
    protected void onResume() {
        super.onResume();

        if (mServ != null) {
            mServ.resumeMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        PowerManager pm = (PowerManager)
                getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (pm != null) {
            isScreenOn = pm.isScreenOn();
        }

        if (!isScreenOn) {
            if (mServ != null) {
                mServ.pauseMusic();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        doUnbindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        mHomeWatcher.stopWatch();
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        menuItemView = findViewById(R.id.sound);

        doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);

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

        Toolbar toolbar = findViewById(R.id.bar);
        toolbar.setTitle("Puzzle");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        filterAction = menu.findItem(R.id.sound);
        if (soundIsOff) {
            filterAction.setIcon(R.drawable.volume_down);
        } else filterAction.setIcon(R.drawable.volume_up);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem optionItem) {
        int id = optionItem.getItemId();
        if (id == R.id.sound) {
            if (!soundIsOff) {
                doUnbindService();
                Intent music = new Intent();
                music.setClass(this, MusicService.class);
                stopService(music);
                optionItem.setIcon(R.drawable.volume_down);
                soundIsOff = true;
            } else {
                doBindService();
                Intent music = new Intent();
                music.setClass(this, MusicService.class);
                startService(music);
                optionItem.setIcon(R.drawable.volume_up);
                soundIsOff = false;
            }
        }
        return super.onOptionsItemSelected(optionItem);
    }
}
