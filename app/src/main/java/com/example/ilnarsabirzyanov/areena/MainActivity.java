package com.example.ilnarsabirzyanov.areena;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ImageButton playButton, easyButton, mediumButton, hardButton, soundButton;
    ProgressBar progressBar;
    BackgroundMusic music;

    enum Difficulty {
        EASY, MEDIUM, HARD
    }
    Difficulty difficulty = Difficulty.EASY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        playButton = (ImageButton)findViewById(R.id.playButton);
        easyButton = (ImageButton)findViewById(R.id.easyButton);
        mediumButton = (ImageButton)findViewById(R.id.mediumButton);
        hardButton = (ImageButton)findViewById(R.id.hardButton);
        soundButton = (ImageButton)findViewById(R.id.soundButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("difficulty", difficulty);
                startActivity(intent);
            }
        });

        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute("https://www.dropbox.com/s/xrzf3zndvogpkfk/areena.mp3?dl=1", "background.mp3");
            }
        });

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDifficulty(v.getId());
            }
        });
        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDifficulty(v.getId());
            }
        });
        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDifficulty(v.getId());
            }
        });
        File f = new File(getFilesDir(), "background.mp3");
        BackgroundMusic.BackgroundMusic(this, f);
        BackgroundMusic.play();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        BackgroundMusic.play();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        BackgroundMusic.pause();
    }

    protected void changeDifficulty(int id) {
        easyButton.setBackgroundResource(R.drawable.easy2);
        mediumButton.setBackgroundResource(R.drawable.medium2);
        hardButton.setBackgroundResource(R.drawable.hard2);
        switch (id) {
            case R.id.easyButton:
                difficulty = Difficulty.EASY;
                easyButton.setBackgroundResource(R.drawable.easy);
                break;
            case R.id.mediumButton:
                difficulty = Difficulty.MEDIUM;
                mediumButton.setBackgroundResource(R.drawable.medium);
                break;
            case R.id.hardButton:
                difficulty = Difficulty.HARD;
                hardButton.setBackgroundResource(R.drawable.hard);
                break;
        }
    }


    class DownloadTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        private File download(String link, String fileName) {
            HttpURLConnection httpURLConnection;
            InputStream inputStream = null;
            int totalSize;
            int downloadSize;
            byte[] buffer;
            int bufferLength;
            File file = null;
            URL url;

            FileOutputStream fileOutputStream = null;
            try {
                url = new URL(link);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                int responseCode = httpURLConnection.getResponseCode();
                totalSize = httpURLConnection.getContentLength();

                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                file = new File(getFilesDir(), fileName + ".tmp");
                fileOutputStream = new FileOutputStream(file);
                inputStream = httpURLConnection.getInputStream();

                downloadSize = 0;

                buffer = new byte[1024];
                while ((bufferLength = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bufferLength);
                    downloadSize += bufferLength;
                    publishProgress(downloadSize, totalSize);
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (file != null) {
                    file.deleteOnExit();
                }
                file = null;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return file;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            File music = download(params[0], params[1]);
            boolean b;
            if (music == null) {
                return false;
            }
            new File(getFilesDir(), params[1]).delete();
            music.renameTo(new File(getFilesDir(), params[1]));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean res) {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    final static String TAG = "Main";
}
