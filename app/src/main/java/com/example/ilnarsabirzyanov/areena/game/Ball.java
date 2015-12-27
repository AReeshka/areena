package com.example.ilnarsabirzyanov.areena.game;

import android.graphics.Canvas;

/**
 * Created by Cawa on 22.12.2015.
 */
public class Ball {

    public Point c, v, e, a;
    public double r, speed, acs;
    Ball(double xx, double yy, double rr) {
        c = new Point(xx, yy);
        r = rr;
        v = new Point(1, 0);
    }
    Ball(double xx, double yy, int diff) {
        c = new Point(xx, yy);
        r = 20;
        setDiff(diff);
    }

    void increaseSpeed() {
        double k = 1 / speed;
        speed += acs;
        k *= speed;
        v.mul(k);
    }
    void setDiff(int diff) {
        switch (diff) {
            case 0:
                speed = 1;
                acs = 0;
                break;
            case 1:
                speed = 2;
                acs = 0.001;
                break;
            case 2:
                speed = 3;
                acs = 0.002;
                break;
            default:
                speed = 4;
                acs = 0.004;
        }
        a = new Point(acs, 0);
        v = new Point(speed, 0);
    }
    public void draw(Canvas canvas) {
        GameUtils.drawBall(canvas, this);
    }
}
