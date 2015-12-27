package com.example.ilnarsabirzyanov.areena.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.example.ilnarsabirzyanov.areena.GameView;

/**
 * Created by Cawa on 22.12.2015.
 */
public class GameUtils {
    public static double EPS = 0.000001;
    public static double FPS = 60;
    public static double w = 1;
    public static int RAND_FRAME_W = 90;
    public static void drawTrace(Canvas canvas, Trace trace) {
        Paint paint =  new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        for (int i = 1; i < trace.points.size(); i++) {
            canvas.drawLine((float) trace.points.get(i - 1).point.x, (float)trace.points.get(i - 1).point.y,
                    (float)trace.points.get(i).point.x, (float)trace.points.get(i).point.y, paint);
        }

    }
    public static void drawBall(Canvas canvas, Ball ball) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(1);
        canvas.drawCircle((float) ball.c.x, (float) ball.c.y, (float) ball.r, paint);
    }

    public static void drawCBall(Canvas canvas, CBall ball) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(1);
        canvas.drawCircle((float) ball.c.x, (float) ball.c.y, (float) ball.r, paint);
    }


    public static void setListener(final GameView listener) {
        listener.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                listener.onMyTouch(event);
                return true;
            }
        });
    }

    public static double getDist(Point a, Point b, Point c) {
        double ans = Math.min(a.sub(c).mod2(), b.sub(c).mod2());
        return checkedDist(a, b, c, Math.sqrt(ans));
    }

    public static double checkedDist(Point a, Point b, Point c, double ans) {
        if (a.sub(b).scal(a.sub(c))*a.sub(b).scal(b.sub(c)) < 0) {
            return Math.min(dist(a, b, c), ans);
        }
        return ans;
    }

    public static double dist(Point a, Point b, Point c) {
        Point abV = b.sub(a);
        Point acV = c.sub(a);
        double angle = 1 - abV.cos2(acV);
        angle = Math.sqrt(angle*acV.mod2());
        return angle;
    }

    public static Ball bounce(Ball ball, Point a, Point b, double t) {
        if (t < 0) {
            t *= 1;
        }
        ball.c = ball.c.add(ball.e.mul(t));
        ball.e = ball.e.mul(1 - t);

        double d = dist(a, b, ball.c);
        double cd = getDist(a, b, ball.c);
        if (d != cd) {
            ball.e.neg();
            ball.v.neg();
            return ball;
        }
        Point ac = ball.c.sub(a);
        Point bc = ball.c.sub(b);
        Point ab = b.sub(a);
        double alpha = 1 - ab.cos2(ball.e);
        Point addV = new Point(ab.y, -ab.x);
        Point addE = new Point(addV);
        double k = (ball.v.mod2() * alpha) * 4;
        k/= addV.mod2();
        addV = addV.mul(Math.sqrt(k));
        k = (ball.e.mod2() * alpha) * 4;
        k/= addE.mod2();
        addE = addE.mul(Math.sqrt(k));
        if (addE.scal(ball.e) > 0) {
            addE.neg();
            addV.neg();
        }
        ball.e = ball.e.add(addE);
        ball.v = ball.v.add(addV);
        if (ball.e.mod2() < GameUtils.EPS) {
            ball.e = new Point(0, 0);
        }
        if (ball.v.mod2() > ball.speed* ball.speed) {
            //k *= ball.speed;
            ball.v = ball.v.mul(ball.speed/Math.sqrt(ball.v.mod2()));
        }
        return ball;
    }

    public static double distP(Point a, Point b) {
        return Math.sqrt(a.sub(b).mod2());
    }

    //  ab - ball's traectory; cd border
    public static int MAXTIME = 99999999;
    public static double getTme(Ball ball, Point c, Point d, double rad) {
        Point a = ball.c;
        Point b = ball.c.add(ball.e);
        if (getDist(a, b, c) < rad) {
            rad *= 1;
        }
        Point ad = d.sub(a);
        Point ab = b.sub(a);
        Point ac = c.sub(a);
        Point cd = d.sub(c);
        Line l1 = new Line(a, b);
        Line l2 = new Line(c, d);
        if (l1.a * l2.b == l2.a * l1.b) {// if lines are parallel
            return MAXTIME;
        }
        Point per = l1.per(l2); //
        double t = MAXTIME;
        double t1 = per.sub(a).x / b.sub(a).x;
        if ((t1 >= 0)) {
            double sin = Math.sqrt(1 - a.sub(per).cos2(c.sub(per)));
            double l = Math.sqrt(a.sub(per).mod2()) - rad / sin;
            Point dl = ball.v;
            dl = dl.mul(l /ball.speed);
            Point cc = a.add(dl);
            if (getDist(c, d, cc) - EPS < rad) {
                t = l / ball.speed;
            }
            if (t < 0) {
                t*= 1;
            }
        }
        if (t < 1) {
            t *= 1;
        }
        double t2 = (Math.sqrt(ad.mod2()) - rad)/(ad.scal(ab) / Math.sqrt(ad.mod2()));
        if (getDist(a, b, d) < rad) {
            //t2 = Math.sqrt(t2);
            t = Math.min(t, t2);
        }
        double t3 = (Math.sqrt(ac.mod2()) - rad)/(ac.scal(ab) / Math.sqrt(ac.mod2()));
        if (t3 > 0) {
            //t3 = Math.sqrt(t3);
            t = Math.min(t, t3);
        }
        if ((t < 0)) {
            t *= 1;
        }

        return t;
    }
}
