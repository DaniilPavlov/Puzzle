package com.lowlevelprog.courseproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Win extends AppCompatActivity {

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
        setContentView(R.layout.activity_win);
        ImageView ivBasicImage = findViewById(R.id.image_win);
        TextView connection = findViewById(R.id.connection);
        TextView lastC = findViewById(R.id.current);
        lastC.setText("Your level time is " + Puzzle.elapsedMillis + " seconds");

        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            connection.setVisibility(View.INVISIBLE);
            Picasso.with(getApplicationContext()).load("https://im0-tub-ru.yandex.net/i?id=389e4c5fcd7e6a3fd8b022fad23329a4&n=13").into(ivBasicImage);
        } else {
            connection.setVisibility(View.VISIBLE);
        }

        soundIsOff = Home.soundIsOff;
        if (!soundIsOff) {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
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
        doUnbindService();
    }

}
