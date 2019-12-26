package com.lowlevelprog.courseproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class ImgViewModel extends ViewModel {

    private MutableLiveData<Bitmap> _image = new MutableLiveData<Bitmap>();
    private MutableLiveData<Boolean> _spinner = new MutableLiveData<Boolean>();
    private Boolean connected = false;

    public Boolean getConnected() {
        return connected;
    }

    public void setConnected(Boolean connected) {
        this.connected = connected;
    }

    public MutableLiveData<Bitmap> get_image() {
        return _image;
    }

    private void set_image(Bitmap image) {
        this._image.postValue(image);
    }

    public MutableLiveData<Boolean> get_spinner() {
        return _spinner;
    }

    private void set_spinner(Boolean spinner) {
        this._spinner.setValue(spinner);
    }

    void loadImage(ImageView imageView, String url) {

        set_spinner(true);

        Picasso.with(imageView.getContext())
                .load(url)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        set_image(bitmap);
                        set_spinner(false);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
    }
}
