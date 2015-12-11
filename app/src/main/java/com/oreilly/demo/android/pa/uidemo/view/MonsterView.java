package com.oreilly.demo.android.pa.uidemo.view;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;


import com.oreilly.demo.android.pa.uidemo.model.Monsters;


/**
 * I see spots!
 *
 * @author <a href="mailto:android@callmeike.net">Blake Meike</a>
 */
public class MonsterView extends View {

    private volatile Monsters monsters;

    /**
     * @param context the rest of the application
     */
    public MonsterView(final Context context) {
        super(context);
        setFocusableInTouchMode(true);
    }

    public int getScreenW(){
        int x = getWidth();
        return x;
    }
    public int getScreenH(){
        int x = getHeight();
        return x;
    }


    /**
     * @param context
     * @param attrs
     */
    public MonsterView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setFocusableInTouchMode(true);
    }

    /** This function draws the grids everytime the monster is drawn to preserve the borders of a cell */
    public void DrawGrid(Canvas mycanvas, Paint mypaint)
    {
        int countx = 0;
        int county = 0;
        int countx2 = 0;
        int county2 = 0;
        for (int x = 0; x < 10; x++) {
            mycanvas.drawLine(countx, county, countx, getHeight() - 1, mypaint);
            countx += getWidth() / 10;
        }
        for (int y = 0; y < getWidth() - 1; y++) {
            mycanvas.drawLine(countx2, county2, getWidth() - 1, county2, mypaint);
            county2 += getHeight() / 10;
        }

    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MonsterView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        setFocusableInTouchMode(true);
    }

    /**
     * @param monsters
     */
    public void setMonsters(final Monsters monsters) { this.monsters = monsters; }

    /**
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override protected void onDraw(final Canvas canvas) {
        /** paint setup*/
        final Paint paint = new Paint();
        paint.setStyle(Style.STROKE);
        paint.setColor(hasFocus() ? Color.BLUE : Color.GRAY);
        canvas.drawRect(0, 0, getWidth() - 1, getHeight() - 1, paint);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4);

        final Paint paintb = new Paint();
        paintb.setColor(Color.BLACK);
        paintb.setStrokeWidth(4);
        DrawGrid(canvas,paintb);

        paint.setStyle(Style.FILL);
    // this draws monsters from 2d array to corresponding cell

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (!monsters.spaceEmpty(i, j)) {

                        paint.setColor(monsters.getMonster(i, j).getColor());
                        canvas.drawRect
                                (monsters.getMonster(i, j).getX() * (getWidth() / 10),
                                        (monsters.getMonster(i, j).getY() * (getHeight() / 10)),
                                        (((monsters.getMonster(i, j).getX() + 1) * (getWidth() / 10))),

                                        (((monsters.getMonster(i, j).getY() + 1) * (getHeight() / 10))),
                                        paint);
                        DrawGrid(canvas, paintb);

                    }
                }

            }

    }
}
