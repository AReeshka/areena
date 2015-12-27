package com.example.ilnarsabirzyanov.areena;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.ilnarsabirzyanov.areena.MainActivity.Difficulty;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        GameView view = (GameView)findViewById(R.id.game);
        int t = intent.getIntExtra("difficulty", 0);
        view.difficulty = Difficulty.values()[t];
        int tt =0;
        BackgroundMusic.play();
    }

    @Override
    public void onResume() {
        super.onResume();
        BackgroundMusic.play();
    }

    @Override
    public void onPause() {
        super.onPause();
        BackgroundMusic.pause();
    }
}
