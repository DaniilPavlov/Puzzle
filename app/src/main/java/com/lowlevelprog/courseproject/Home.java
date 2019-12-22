package com.lowlevelprog.courseproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {
    static boolean soundIsOff = false;
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
    public void onResume() {
        super.onResume();
        if (mServ != null) {
            mServ.resumeMusic();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mServ != null) {
            mServ.pauseMusic();
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
        MenuItem filterAction = menu.findItem(R.id.sound);
        filterAction.setIcon(R.drawable.volume_up);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem optionItem) {
        int id = optionItem.getItemId();
        View menuItemView = findViewById(R.id.sound);
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
