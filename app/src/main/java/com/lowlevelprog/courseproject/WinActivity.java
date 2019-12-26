package com.lowlevelprog.courseproject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WinActivity extends AppCompatActivity {

    boolean soundIsOff;
    private boolean mIsBound = false;
    private MusicService mServ;
    TextView connection;
    ImageView ivBasicImage;
    HomeWatcher mHomeWatcher;
    private ImgViewModel viewModel;
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
        ivBasicImage = findViewById(R.id.image_win);
        connection = findViewById(R.id.connection);
        TextView lastC = findViewById(R.id.current);
        lastC.setText("Your level time is " + PuzzleActivity.elapsedMillis + " seconds");

        viewModel = ViewModelProviders.of(this).get(ImgViewModel.class);

        final ImageView imageWin = findViewById(R.id.image_win);
        final ProgressBar spinner = findViewById(R.id.spinner);


        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            connection.setVisibility(View.INVISIBLE);
            spinner.setVisibility(View.VISIBLE);

            viewModel.loadImage(imageWin, "https://im0-tub-ru.yandex.net/i?id=389e4c5fcd7e6a3fd8b022fad23329a4&n=13");
            final Observer<Bitmap> observer = new Observer<Bitmap>() {
                @Override
                public void onChanged(Bitmap set) {
                    imageWin.setVisibility(View.VISIBLE);
                    imageWin.setImageBitmap(viewModel.get_image().getValue());
                }
            };
            spinner.setVisibility(View.GONE);
            viewModel.get_image().observe(this, observer);

        } else {
            connection.setVisibility(View.VISIBLE);

            Bitmap imageToSet;
            if (viewModel.get_image().getValue() != null) {
                connection.setVisibility(View.INVISIBLE);
                imageToSet = viewModel.get_image().getValue();
                imageWin.setImageBitmap(imageToSet);
                imageWin.setVisibility(View.VISIBLE);
            }

        }

        soundIsOff = HomeActivity.soundIsOff;
        if (!soundIsOff) {
            doBindService();
            Intent music = new Intent();
            music.setClass(this, MusicService.class);
            startService(music);
        }

        mHomeWatcher = new HomeWatcher(this);
        mHomeWatcher.setOnHomePressedListener(new HomeWatcher.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                    doUnbindService();
                }
            }

            @Override
            public void onHomeLongPressed() {
                if (mServ != null) {
                    mServ.pauseMusic();
                    doUnbindService();
                }
            }
        });
        mHomeWatcher.startWatch();
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
}
