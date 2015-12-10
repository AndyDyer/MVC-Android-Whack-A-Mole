package com.oreilly.demo.android.pa.uidemo;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import org.junit.Test;


/** Android UI demo program */
public class MonsterMash extends Activity {


    /** Monster diameter */
    public static final int DOT_DIAMETER = 6;
    /** Score and Timer*/
    public static int timeremaining = 30;
    public static int score = 0;
    public static int level = 1;
    public static int monstercap = 5 + level;


    public static void incScore() {
        score++;
    }

    /** Listen for taps. */
    private static final class TrackingTouchListener implements View.OnTouchListener {

        private final Monsters mMonsters;
        private List<Integer> tracks = new ArrayList<>();

        TrackingTouchListener(final Monsters monsters) { mMonsters = monsters; }
        private MonsterView monsterViewz;

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

            for (final Integer i: tracks)
            {
                final int idx = evt.findPointerIndex(i);

                double q = evt.getX(idx) / (v.getWidth() / 10);
                Math.floor(q);
                int myq = (int)q;
                double p = evt.getY(idx) / (v.getHeight() / 10);
                Math.floor(p);
                int myp = (int)p;




                String myheight = String.valueOf (myp);
                Log.d(" height ", myheight);


                if (Color.GREEN == mMonsters.checkState(myq,myp))
                {
                    mMonsters.removeMonster(myq,myp);
                }
                //TODO I Think something needs to happen here look up up here

            }
            return true;
        }

        private void addMonster(
                final Monsters monsters,
                final float x,
                final float y,
                final float p,
                final float s) {
                monsters.addMonster((int) x,(int) y, Color.CYAN);
        }
    }

    private final Random rand = new Random();

    /** The application model */
    private final Monsters monsterModel = new Monsters();

    /** The application view */
    private MonsterView monsterView;

    /** The monster generator */
    private Timer monsterGenerator;

    private Timer monsterTimer;

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
            return true;
        });
        // int timereaming = 30 , int score = 0; paint setstrokewidth to 5
        final EditText tb1 = (EditText) findViewById(R.id.text1);
        final EditText tb2 = (EditText) findViewById(R.id.text2);
        final EditText tb3 = (EditText) findViewById(R.id.text3);
        monsterModel.setMonstersChangeListener((Monsters monsters) -> {
            final Monster d = monsters.getLastMonster();
            tb1.setText("Score: " + score);
            tb2.setText("Time Left: " + timeremaining);
            tb3.setText("Level: " + level);
            monsterView.invalidate();
        });

        findViewById(R.id.button1).setOnClickListener((final View v) ->
                        onPause()
        );
        findViewById(R.id.button2).setOnClickListener((final View v) ->
                        onResume()
        );

    }

int q = 0;
    @Override public void onResume() {
        super.onResume();
        if (monsterGenerator == null) {
            monsterGenerator = new Timer();

            // countdown timer
            monsterTimer.schedule(new TimerTask() {
                public void run() {
                    timeremaining--;
                }
            }, /*initial delay*/ 5000, /*periodic delay*/ 2000);
            monsterTimer = new Timer();
            // generate new monsters, one every two seconds

            monsterGenerator.schedule(new TimerTask() {
                @Override
                public void run() {
                    // must invoke makeMonster on the UI thread to avoid
                    // ConcurrentModificationException on list of monsters

                    if (q < monstercap)
                    {
                        runOnUiThread(() -> makeMonster(monsterModel, monsterView, Color.BLACK));
                        q++;
                    }


                    runOnUiThread(() -> changeMonster(monsterModel));

                    if (isOver(monsterModel) == true)
                    {
                        level ++;
                        timeremaining ++;
                        q = 0;
                        // TODO something to this effect
                    }
                }
            }, /*initial delay*/ 5000, /*periodic delay*/ 2000);


        }
    }

    public boolean isOver (Monsters monster)
    {
    int sum = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++)
            {
               if (monster.getMonster(i,j) != null)
               {
                   sum++;
               }
            }

        }
        if (sum == 0)
        {
            return true;
                    // game is over
        }
        else
        {
            return false;
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
    int x = 0;
    int i = 0;
    void makeMonster(final Monsters monsters, final MonsterView view, int color) {
        color = Color.GREEN;
        if (x <= 0)
        {
            monsters.clearMonsters();
            // monsters.addMonster(3,3,Color.GREEN);
            x++;
        }
        for (int i = 0; i < 4; i++)
            monsters.addMonster((rand.nextInt(9)), (rand.nextInt(9)), color);



    }

    void changeMonster(final Monsters monsters){


        int chance, space,x = 0, y = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (!monsters.spaceEmpty(i, j)) {
                    chance = rand.nextInt(10);
                    if (chance >= 3){
                        if (monsters.getMonster(i,j).getColor() == Color.GREEN){
                            monsters.addMonster(i,j, Color.YELLOW);
                            monsters.removeMonster(i, j);
                        }
                        else if (monsters.getMonster(i,j).getColor() == Color.YELLOW){
                            monsters.addMonster(i,j, Color.GREEN);
                            monsters.removeMonster(i,j);
                        }
                    }
                    if (chance >=5){

                        space = rand.nextInt(7);
                        if (space == 0) {
                            x = -1; y = -1;
                        }
                        else if (space == 1) {
                            x = 0; y = -1;
                        }
                        else if (space == 2) {
                            x = 1; y = -1;
                        }
                        else if (space == 3) {
                            x = 1; y = 0;
                        }
                        else if (space == 4) {
                            x = 1; y = 1;
                        }
                        else if (space == 5) {
                            x = 0; y = 1;
                        }
                        else if (space == 6) {
                            x = -1; y = 1;
                        }
                        else if (space == 7) {
                            x = -1; y = 0;
                        }

                        monsters.moveMonsters(i, j, (i + x), (j + y));

                    }
                }
            }
//
        }


    }
}