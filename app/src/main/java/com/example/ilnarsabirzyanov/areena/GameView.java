package com.example.ilnarsabirzyanov.areena;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ilnarsabirzyanov.areena.MainActivity.Difficulty;
import com.example.ilnarsabirzyanov.areena.game.GameBoard;
import com.example.ilnarsabirzyanov.areena.game.GameUtils;

/**
 * Created by Cawa on 30.11.2015.
 */
public class GameView extends View {
    boolean needToSetCoordinates = true;
    boolean pause = false;
    int time = 0;
    Difficulty difficulty = Difficulty.EASY;
    GameBoard game;

    public GameView(Context context, AttributeSet attributes) {
        super(context, attributes);
        GameUtils.setListener(this);
        game = new GameBoard();
    }

    public boolean onMyTouch(MotionEvent event) {
        if (!pause) {
            game.addPoint(event.getX(), event.getY(), time);
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (needToSetCoordinates) {
            game.setCoord(getWidth(), getHeight());
            needToSetCoordinates = false;
            game.setDifficulty(difficulty.ordinal() + 1);
        }
        if (!pause) {
            time++;
        }
        boolean end = game.draw(canvas,time, pause);
        if (end) {
            pause = true;
            gameOver(time, game.getScore());
        }
        invalidate();
    }

    // here you can invoke some methods connected with ending of game
    public void gameOver(double time, int score) {
        time /= GameUtils.FPS;
        final Activity activity = (Activity)getContext();
        final TextView result = (TextView)activity.findViewById(R.id.textView);
        final ImageButton replay = (ImageButton)activity.findViewById(R.id.replayButton);
        final ImageButton menu = (ImageButton)activity.findViewById(R.id.menuButton);

        result.setText("Your result: " + Double.valueOf(time).intValue());
        result.setVisibility(VISIBLE);
        replay.setVisibility(VISIBLE);
        menu.setVisibility(VISIBLE);

        replay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                replay.setVisibility(INVISIBLE);
                menu.setVisibility(INVISIBLE);
                Intent intent = activity.getIntent();
                intent.putExtra("difficulty", difficulty);
                activity.finish();
                activity.startActivity(intent);
            }
        });
        menu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

}
