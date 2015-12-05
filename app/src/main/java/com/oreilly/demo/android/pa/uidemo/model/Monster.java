package com.oreilly.demo.android.pa.uidemo.model;


/** A dot: the coordinates, color and size. */
public final class Monster {
    private final float x, y;
    private final int color;
    private final int diameter;

    /**
     * @param x horizontal coordinate.
     * @param y vertical coordinate.
     * @param color the color.
     * @param diameter dot diameter.
     */
    public Monster(final float x, final float y, final int color, final int diameter) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.diameter = diameter;
    }

    /** @return the horizontal coordinate. */
    public float getX() { return x; }

    /** @return the vertical coordinate. */
    public float getY() { return y; }

    /** @return the color. */
    public int getColor() { return color; }

    /** @return the dot diameter. */
    public int getDiameter() { return diameter; }
}