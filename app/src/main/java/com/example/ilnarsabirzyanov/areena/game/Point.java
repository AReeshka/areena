package com.example.ilnarsabirzyanov.areena.game;

/**
 * Created by Cawa on 22.12.2015.
 */
public class Point {
    double x;
    double y;
    Point(double xx, double yy) {
        x = xx;
        y = yy;
    }
    Point() {
    }
    Point(Point p) {
        x = p.x;
        y = p.y;
    }

    Point add(Point p) {
        Point ans = new Point(p);
        ans.x += x;
        ans.y += y;
        return ans;
    }
    Point sub(Point p) {
        Point ans = new Point(p);
        ans.x -= x;
        ans.y -= y;
        return ans;
    }
    void neg() {
        x = -x;
        y = -y;
    }
    Point mul (double k) {
        Point ans = new Point(this);
        ans.x*=k;
        ans.y*=k;
        return ans;
    }
    double scal(Point p) {
        return x*p.x + y*p.y;
    }
    double mod2() {
        return x*x + y*y;
    }
    double cos2(Point p) {
        if ((this.mod2() == 0) ||(p.mod2() == 0)) {
            return 0;
        }
        double tmp = scal(p);
        tmp *= tmp;
        tmp /= (mod2()*p.mod2());
        return tmp;
    }
}