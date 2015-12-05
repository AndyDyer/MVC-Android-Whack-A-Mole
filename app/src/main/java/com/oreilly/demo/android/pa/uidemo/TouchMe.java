package com.oreilly.demo.android.pa.uidemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;

import com.oreilly.demo.android.pa.uidemo.model.Dot;
import com.oreilly.demo.android.pa.uidemo.model.Dots;
import com.oreilly.demo.android.pa.uidemo.model.Monster;
import com.oreilly.demo.android.pa.uidemo.model.Monsters;
import com.oreilly.demo.android.pa.uidemo.view.DotView;
import com.oreilly.demo.android.pa.uidemo.view.MonsterView;


/** Android UI demo program */
public class TouchMe extends Activity {
    /** Dot diameter */
    public static final int DOT_DIAMETER = 6;

    /** Listen for taps. */
    private static final class TrackingTouchListener implements View.OnTouchListener {
        private final Monsters mDots;
        private List<Integer> tracks = new ArrayList<>();

        TrackingTouchListener(final Monsters monsters) { mDots = monsters; }

        @Override public boolean onTouch(final View v, final MotionEvent evt) {
            final int action = evt.getAction();
            switch (action & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    final int idx1 = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
                            >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    tracks.add(evt.getPointerId(idx1));
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    final int idx2 = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
                            >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                    tracks.remove(evt.getPointerId(idx2));
                    break;

                case MotionEvent.ACTION_MOVE:
                    final int n = evt.getHistorySize();
                    for (Integer i : tracks) {
                        final int idx = evt.findPointerIndex(i);

                    }
                    break;


                default:
                    return false;
            }

            return true;
        }

        private void addDot(
                final Dots dots,
                final float x,
                final float y,
                final float p,
                final float s) {
            dots.addDot(x, y, Color.CYAN, (int) ((p + 0.5) * (s + 0.5) * DOT_DIAMETER));
        }
    }

    private final Random rand = new Random();

    /** The application model */
    private final Monsters monsterModel = new Monsters();

    /** The application view */
    private MonsterView monsterView;

    /** The dot generator */
    private Timer monsterGenerator;

    /** Called when the activity is first created. */
    @Override public void onCreate(final Bundle state) {
        super.onCreate(state);

        // install the view
        setContentView(R.layout.main);

        // find the dots view
        monsterView = (MonsterView) findViewById(R.id.dots);
        monsterView.setMonsters(monsterModel);

        monsterView.setOnCreateContextMenuListener(this);
        monsterView.setOnTouchListener(new TrackingTouchListener(monsterModel));

        monsterView.setOnKeyListener((final View v, final int keyCode, final KeyEvent event) -> {
            if (KeyEvent.ACTION_DOWN != event.getAction()) {
                return false;
            }

            int color;
            switch (keyCode) {
                case KeyEvent.KEYCODE_SPACE:
                    color = Color.MAGENTA;
                    break;
                case KeyEvent.KEYCODE_ENTER:
                    color = Color.BLUE;
                    break;
                default:
                    return false;
            }

            makeMonster(monsterModel, monsterView, color);

            return true;
        });

        // wire up the controller
        findViewById(R.id.button1).setOnClickListener((final View v) ->
            makeMonster(monsterModel, monsterView, Color.RED)
        );
        findViewById(R.id.button2).setOnClickListener((final View v) ->
            makeMonster(monsterModel, monsterView, Color.GREEN)
        );

        final EditText tb1 = (EditText) findViewById(R.id.text1);
        final EditText tb2 = (EditText) findViewById(R.id.text2);
        monsterModel.setMonstersChangeListener((Monsters monsters) -> {
            final Monster d = monsters.getLastMonster();
            tb1.setText((null == d) ? "" : String.valueOf(d.getX()));
            tb2.setText((null == d) ? "" : String.valueOf(d.getY()));
            monsterView.invalidate();
        });
    }

    @Override public void onResume() {
        super.onResume();
        if (monsterGenerator == null) {
            monsterGenerator = new Timer();
            // generate new dots, one every two seconds
            monsterGenerator.schedule(new TimerTask() {
                @Override
                public void run() {
                    // must invoke makeDot on the UI thread to avoid
                    // ConcurrentModificationException on list of dots
                    runOnUiThread(() -> makeMonster(monsterModel, monsterView, Color.BLACK));
                }
            }, /*initial delay*/ 0, /*periodic delay*/ 2000);
        }
    }

    @Override public void onPause() {
        super.onPause();
        if (monsterGenerator != null) {
            monsterGenerator.cancel();
            monsterGenerator = null;
        }
    }

    /** Install an options menu. */
    @Override public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.simple_menu, menu);
        return true;
    }

    /** Respond to an options menu selection. */
    @Override public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                monsterModel.clearMonsters();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Install a context menu. */
    @Override public void onCreateContextMenu(
            final ContextMenu menu,
            final View v,
            final ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, 1, Menu.NONE, "Clear").setAlphabeticShortcut('x');
    }

    /** Respond to a context menu selection. */
    @Override public boolean onContextItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                monsterModel.clearMonsters();
                return true;
            default:
                return false;
        }
    }

    /**
     * @param monsters the dots we're drawing
     * @param view the view in which we're drawing dots
     * @param color the color of the dot
     */
    void makeMonster(final Monsters monsters, final MonsterView view, final int color) {
        final int pad = (DOT_DIAMETER + 2) * 2;
        monsters.addMonster(
                DOT_DIAMETER + (rand.nextFloat() * (view.getWidth() - pad)),
                DOT_DIAMETER + (rand.nextFloat() * (view.getHeight() - pad)),
                color,
                DOT_DIAMETER);
    }
}