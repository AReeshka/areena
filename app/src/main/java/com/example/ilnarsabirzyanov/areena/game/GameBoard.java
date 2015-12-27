package com.example.ilnarsabirzyanov.areena.game;

import android.graphics.Canvas;

import java.util.Vector;

/**
 * Created by Cawa on 22.12.2015.
 */
public class GameBoard {
    int score = 0;
    int ballsNum = 1;
    int TIMEOUT = 60*60;
    double BALLR = 20;
    int MAXUTSIZE = 100;
    int MAXUTLENGTH = 750;
    int MAXUTDL = 200;

    double prevTime;
    double dt = 1;
    boolean valid;
    double sizeX, sizeY, centerX, centerY;
    Ball ball;
    Vector<Trace> traces;
    Vector<CBall> balls;
    Trace lastTrace;


    public GameBoard() {
        ball = new Ball(centerX, centerY, BALLR);
        lastTrace = new Trace();
        traces = new Vector<>();
        balls = new Vector<>();
    }



    public void setCoord(int x, int y) {
        sizeX = x;
        sizeY = y;
        centerX = x / 2;
        centerY = y / 2;
        ball = new Ball(centerX, centerY, BALLR);
        lastTrace = new Trace(MAXUTSIZE, MAXUTLENGTH, MAXUTDL);
        genBalls();
    }

    void genBalls() {
        while (balls.size() < ballsNum) {
            CBall b = new CBall(sizeX, sizeY , BALLR);
            balls.add(b);
        }
    }

    public void setDifficulty(int par) {
        lastTrace.setDiff(par);
        ball.setDiff(par);
        Trace trace = new Trace(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        switch (par) {
            case(1):
                trace.addPoint(0, 0, Integer.MAX_VALUE);
                trace.addPoint(sizeX, 0, Integer.MAX_VALUE);
                trace.addPoint(sizeX, sizeY, Integer.MAX_VALUE);
                trace.addPoint(0, sizeY, Integer.MAX_VALUE);
                break;
            case(2):
                trace.addPoint(0, 0, Integer.MAX_VALUE);
                trace.addPoint(sizeX, 0, Integer.MAX_VALUE);
                trace.addPoint(sizeX, sizeY, Integer.MAX_VALUE);
                traces.add(trace);
                break;
            case(3):
                break;
            default:
                trace.addPoint(0, 0, Integer.MAX_VALUE);
                trace.addPoint(sizeX, 0, Integer.MAX_VALUE);
                trace.addPoint(sizeX, sizeY, Integer.MAX_VALUE);
                trace.addPoint(0, sizeY, Integer.MAX_VALUE);
                trace.addPoint(0, 0, Integer.MAX_VALUE);
                traces.add(trace);
        }
        traces.add(trace);
    }

    public void addPoint(double x, double y, double t) {
        if (GameUtils.distP(new Point(x, y), ball.c) > ball.r + GameUtils.w + GameUtils.EPS)
        lastTrace.addPoint(x, y, t + TIMEOUT);
        prevTime = t;/*
        if ((ball.c.x < 0) || (ball.c.y < 0) || (ball.c.x > sizeX) || (ball.c.y > sizeY)) {
            ball.c = new Point(centerX, centerY);
        }*/
    }

    public boolean draw(Canvas canvas, int time, boolean pause) {/*
        for (int i = 0; i < traces.size(); i++) {
            traces.get(i).check(time);
        }*/
        if (!pause) {
            lastTrace.check(time);
            calculateBall(time);
            score += checkPoints();
        }

        for (int i = 0; i < traces.size(); i++) {
            traces.get(i).draw(canvas);
        }

        lastTrace.draw(canvas);
        for (int i = 0; i < balls.size(); i++) {
            balls.get(i).draw(canvas);
        }
        ball.draw(canvas);
        boolean ans = checkGameOver();
        if (ans) {
            pause = true;
        }
        ball.increaseSpeed();
        return ans;
    }

    boolean checkGameOver() {
        return ((ball.c.x < -GameUtils.RAND_FRAME_W) || (ball.c.y < -GameUtils.RAND_FRAME_W) || (ball.c.x > sizeX + GameUtils.RAND_FRAME_W) || (ball.c.y > sizeY + GameUtils.RAND_FRAME_W));
    }

    int checkPoints() {
        int ans = 0, i = 0;
        while (i < balls.size()) {
            CBall b = balls.get(i);
            if (GameUtils.distP(b.c, ball.c) < ball.r + b.r) {
                ans += b.p;
                balls.remove(i);
            } else {
                i++;
            }
        }
        genBalls();
        return ans;
    }

    void calculateBall(int time) {
        ball.e = ball.v;
        int g = 0;
        while (!ball.e.equals(new Point(0, 0))) {
            g++;
            Point p3 ;
            Point p4;
            Point pa1 = null, pa2 = null;
            double t = -1;
            int j = -1;
            if (lastTrace.points.size() >= 2) {
                p3 = lastTrace.points.get(0).point;
                for (int i = 1; i < lastTrace.points.size(); i++) {
                    p4 = lastTrace.points.get(i).point;
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
            }
            for (int k = 0; k < traces.size(); k++) {
                Trace trace = traces.get(k);
                if (trace.points.size() >= 2) {
                    p3 = trace.points.get(0).point;
                    for (int i = 1; i < trace.points.size(); i++) {
                        p4 = trace.points.get(i).point;
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
                }
            }
            if (j == -1) {
                ball.c = ball.c.add(ball.e);
                return;
            } else {
                ball = GameUtils.bounce(ball, pa1, pa2, t);
            }
            if (g == 100) {
                break;
            }
        }
    }

    public int getScore() {
        return score;
    }
}
