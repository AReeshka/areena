package com.example.ilnarsabirzyanov.areena.game;

import android.graphics.Canvas;

import java.util.Vector;

/**
 * Created by Cawa on 22.12.2015.
 */
public class Trace {
    class TempPoint{
        Point point;
        double time;
        double dl;
        TempPoint(double xx, double yy, double tt) {
            point = new Point(xx, yy);
            time = tt;
        }
    }

    int MAXSIZE;
    int MAXLENGTH;
    int MAXDL = 200;
    Vector<TempPoint> points;
    float length;


    Trace() {
        points = new Vector<>();
    }
    Trace(int MAXUSIZE, int MAXULENGTH, int MAXUDL) {
        points = new Vector<>();
        MAXSIZE = MAXUSIZE;
        MAXLENGTH = MAXULENGTH;
        MAXDL = MAXUDL;
    }
    void setDiff(int diff) {
        switch (diff) {
            case 0:
                MAXSIZE = 100;
                MAXLENGTH = 750;
                MAXDL = 1000;
                break;
            case 1:
                MAXSIZE = 50;
                MAXLENGTH = 200;
                break;
            case 2:
                MAXSIZE = 50;
                MAXLENGTH = 150;
                break;
            default:
                MAXSIZE = 50;
                MAXLENGTH = 100;
        }
    }
    public void draw(Canvas canvas) {
        GameUtils.drawTrace(canvas, this);
    }

    void addPoint(double x, double y, double time) {
        if (points.size() == MAXSIZE) {
            points.remove(0);
        }
        TempPoint p = new TempPoint(x, y, time);
        if (points.size() > 0) {
            Point pp = points.get(points.size() - 1).point;
            p.dl = Math.sqrt(pp.sub(p.point).mod2());
            if (p.dl > MAXDL) {
                points.clear();
                length = 0;
            }
            length +=p.dl;
        }
        points.add(p);
        while (length > MAXLENGTH) {
            TempPoint tp = points.remove(0);
            length -= tp.dl;
        }


    }

    boolean check(int time) {
        if (points.size() == 0)
            return false;
        while (points.get(0).time < time) {
            TempPoint tp = points.remove(0);
            length -= tp.dl;
            if (points.size() == 0)
                return false;
        }
        return true;
    }

    Ball calc(Ball ball) {
        ball.e = ball.v;
        if (points.size() < 2) {
            return ball;
        }
        while (!ball.e.equals(new Point(0, 0))) {
            Point p1 = ball.c;
            Point p2 = ball.c.add(ball.e);
            Point p3 = points.get(0).point;
            Point p4;
            Point pa1 = null, pa2 = null;
            Point param = null;
            double t = -1;
            int j = -1;
            for (int i = 1; i < points.size(); i++) {
                p4 = points.get(i).point;
                double tmp = GameUtils.getTme(ball, p3, p4, ball.r + GameUtils.w);
                if (tmp <= 1) {
                    if (t == -1) {
                        pa1 = p3;
                        pa2 = p4;
                        t = tmp;
                        j = i;
                    } else {
                        if (t > tmp) {
                            t = tmp;
                            j = i;
                            pa1 = p3;
                            pa2 = p4;
                        }
                    }
                }
                p3 = p4;
            }
            if (j == -1) {
                ball.c = ball.c.add(ball.e);
                return ball;
            }
            ball = GameUtils.bounce(ball, pa1, pa2, t);
        }
        return ball;
    }


}
