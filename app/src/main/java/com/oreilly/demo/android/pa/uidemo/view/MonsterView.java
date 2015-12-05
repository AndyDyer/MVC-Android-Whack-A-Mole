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
        canvas.drawRect(0, 0, getWidth() - 1, getHeight() -1, paint);

        if (null == monsters) { return; }

        paint.setStyle(Style.FILL);
        for (final Monster monster : monsters.getMonsters()) {
            paint.setColor(monster.getColor());
            canvas.drawCircle(
                    monster.getX(),
                    monster.getY(),
                    monster.getDiameter(),
                    paint);
        }
    }
}
