package com.oreilly.demo.android.pa.uidemo.view;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import com.oreilly.demo.android.pa.uidemo.model.Monster;
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

    /**
     * @param context
     * @param attrs
     */
    public MonsterView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        setFocusableInTouchMode(true);
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
        final Paint paint = new Paint();
        paint.setStyle(Style.STROKE);
        paint.setColor(hasFocus() ? Color.BLUE : Color.GRAY);
        canvas.drawRect(0, 0, getWidth() - 1, getHeight() - 1, paint);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(4);
        int countx =0;
        int county = 0;
        int countx2 = 0;
        int county2 = 0;
        for (int x = 0; x < 10; x++)
        {
            canvas.drawLine(countx, county, countx, getHeight() - 1, paint);
            countx += getWidth() / 10;
        }
        for (int y = 0; y < getWidth() - 1; y++) {
            canvas.drawLine(countx2, county2, getWidth() - 1, county2, paint);
            county2 += getHeight()/10;
        }
        if (null == monsters) { return; }

        paint.setStyle(Style.FILL);
        //
        for (final Monster monster : monsters.getMonsters()){
            paint.setColor(monster.getColor());
            paint.setColor(Color.RED);
            canvas.drawCircle(
                    monster.getX(),
                    monster.getY(),50,
                    paint);
            paint.setColor(Color.GREEN);
            canvas.drawRect(monster.getX(),monster.getY(), monster.getX() + (getWidth()/10), monster.getY()+(getHeight()/10),paint);

        }
    }
}
