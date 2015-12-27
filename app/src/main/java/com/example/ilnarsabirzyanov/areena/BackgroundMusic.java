package com.example.ilnarsabirzyanov.areena;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.File;

/**
 * Created by lalala on 27.12.15.
 */
public class BackgroundMusic {
    public static MediaPlayer mp;

    public static void BackgroundMusic(Activity activity, File f) {
        if (f.exists()) {
            mp = MediaPlayer.create(activity, Uri.fromFile(f));
            mp.setLooping(true);
        }
    }

    public static void play() {
        if (mp != null) mp.start();
    }

    public static void pause() {
        if (mp != null) mp.pause();
    }
}
