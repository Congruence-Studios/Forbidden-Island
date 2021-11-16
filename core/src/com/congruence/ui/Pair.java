package com.congruence.ui;

public class Pair implements Comparable<Pair> {

    public int x;

    public int y;

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static final Pair INVALID = new Pair(-1, -1);

    public boolean isInvalid() {
        return x == -1 && y == -1;
    }

    @Override
    public int compareTo(Pair o) {
        if (x > o.x) {
            return 1;
        }
        else if (x==o.x) {
            if (y > o.y) {
                return 1;
            }
            else if (y==o.y) {
                return 0;
            }
            else return -1;
        }
        else return -1;
    }
}
