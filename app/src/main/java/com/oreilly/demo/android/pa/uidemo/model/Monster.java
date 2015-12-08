package com.oreilly.demo.android.pa.uidemo.model;


/** A dot: the coordinates, color and size. */
public final class Monster {
    private final float x, y;
    private final int color;


    /**
     * @param x horizontal coordinate.
     * @param y vertical coordinate.
     * @param color the color.
     */
    public Monster(final float x, final float y, final int color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    /** @return the horizontal coordinate. */
    public float getX() { return x; }

    /** @return the vertical coordinate. */
    public float getY() { return y; }

    /** @return the color. */
    public int getColor() { return color; }
}