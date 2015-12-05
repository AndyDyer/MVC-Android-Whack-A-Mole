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

import com.oreilly.demo.android.pa.uidemo.model.Monster;
import com.oreilly.demo.android.pa.uidemo.model.Monsters;
import com.oreilly.demo.android.pa.uidemo.view.MonsterView;


/** Android UI demo program */
public class MonsterMash extends Activity {
    /** Monster diameter */
    public static final int DOT_DIAMETER = 6;
    /** Score and Timer*/
    public int timeremaining = 30;
    public int score = 0;
    /** Listen for taps. */
    private static final class TrackingTouchListener implements View.OnTouchListener {
        private final Monsters mMonsters;
        private List<Integer> tracks = new ArrayList<>();

        TrackingTouchListener(final Monsters monsters) { mMonsters = monsters; }

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
                default:
                    return false;
            }

            for (final Integer i: tracks) {
                final int idx = evt.findPointerIndex(i);
                addMonster(
                        mMonsters,
                        evt.getX(idx),
                        evt.getY(idx),
                        evt.getPressure(idx),
                        evt.getSize(idx));
            }
            return true;
        }

        private void addMonster(
                final Monsters monsters,
                final float x,
                final float y,
                final float p,
                final float s) {
            monsters.addMonster(x, y, Color.CYAN);
        }
    }

    private final Random rand = new Random();

    /** The application model */
    private final Monsters monsterModel = new Monsters();

    /** The application view */
    private MonsterView monsterView;

    /** The monster generator */
    private Timer monsterGenerator;

    /** Called when the activity is first created. */
    @Override public void onCreate(final Bundle state) {
        super.onCreate(state);

        // install the view
        setContentView(R.layout.main);
//
        // find the monsters view
        monsterView = (MonsterView) findViewById(R.id.monsters);
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
        // int timereaming = 30 , int score = 0; paint setstrokewidth to 5
        final EditText tb1 = (EditText) findViewById(R.id.text1);
        final EditText tb2 = (EditText) findViewById(R.id.text2);
        monsterModel.setMonstersChangeListener((Monsters monsters) -> {
            final Monster d = monsters.getLastMonster();
            tb1.setText("Score: " + score);
            tb2.setText("Time Left: " + timeremaining);
            monsterView.invalidate();
        });
    }


    @Override public void onResume() {
        super.onResume();
        if (monsterGenerator == null) {
            monsterGenerator = new Timer();
            // generate new monsters, one every two seconds
            monsterGenerator.schedule(new TimerTask() {
                @Override
                public void run() {
                    // must invoke makeMonster on the UI thread to avoid
                    // ConcurrentModificationException on list of monsters
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
     * @param monsters the monsters we're drawing
     * @param view the view in which we're drawing monsters
     * @param color the color of the monster
     */
    void makeMonster(final Monsters monsters, final MonsterView view, final int color) {
        monsters.addMonster(
                (rand.nextFloat() / (view.getWidth()/10)),
                (rand.nextFloat() / (view.getHeight()/10)),
            color);
    }
}