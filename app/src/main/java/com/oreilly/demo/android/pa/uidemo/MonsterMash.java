package com.oreilly.demo.android.pa.uidemo;


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


import com.oreilly.demo.android.pa.uidemo.model.Monsters;
import com.oreilly.demo.android.pa.uidemo.view.MonsterView;

import org.junit.Test;


/** Android UI demo program */
public class MonsterMash extends Activity {

    int i = 0;
    /** Monster diameter */
    public static final int DOT_DIAMETER = 6;
    /** Score and Timer*/
    public int timeremaining = 30;
    public static int score = 0;
    public  int level = 0;
    public  int monstercap = 5 + level;

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
            //Analyzes the touches on the screen
            for (final Integer i: tracks)
            {
                final int idx = evt.findPointerIndex(i);
                //Rounds touch locations of the points
                double q = evt.getX(idx) / (v.getWidth() / 10);
                Math.floor(q);
                int myq = (int)q;
                double p = evt.getY(idx) / (v.getHeight() / 10);
                Math.floor(p);
                int myp = (int)p;




                String myheight = String.valueOf (myp);
                Log.d(" height ", myheight);

                //Sees if the monster is vulnerable
                if (Color.YELLOW == mMonsters.checkState(myq,myp))
                {
                    //If it is it is removed from the 2D array
                    mMonsters.removeMonster(myq,myp);
                }
                //TODO I Think something needs to happen here look up up here

            }
            return true;
        }
        //Adds a monster
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



    /** Called when the activity is first created. */
    @Override public void onCreate(final Bundle state) {
        super.onCreate(state);

        // install the view
        setContentView(R.layout.main);

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
        //Links to the XML for buttons
        final EditText tb1 = (EditText) findViewById(R.id.text1);
        final EditText tb2 = (EditText) findViewById(R.id.text2);
        final EditText tb3 = (EditText) findViewById(R.id.text3);
        monsterModel.setMonstersChangeListener((Monsters monsters) -> {
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

    //Starts when the game is started or unpaused
    @Override public void onResume() {
        super.onResume();
        if (monsterGenerator == null) {
            monsterGenerator = new Timer();

            // generate new monsters, one every two seconds
            monsterGenerator.schedule(new TimerTask() {
                @Override
                public void run() {


                    timeremaining--;
                    // monsterModel.checkBoard();
                    if (isOver(monsterModel) == true)
                    {
                        // game over
                        i = 0;
                        timeremaining = 30;
                        level++;
                    }

                    if (timeremaining == 0)
                    {

                        timeremaining--;
                        monsterGenerator.cancel();
                        onPause();
                        onRestart();
                        score = 0;
                        timeremaining = 30;
                        level = 1;
                        i = 0;
                        onResume();

                    }

                    runOnUiThread(() -> makeMonster(monsterModel, monsterView, Color.BLACK));
                    runOnUiThread(() -> changeMonster(monsterModel));
                    runOnUiThread(() -> monsterModel.checkBoard());

                }
            }, /*initial delay*/ 2000, /*periodic delay*/ 1000);


        }
    }


    //Checks if the game is over - Has 0 monsters
    public boolean isOver(Monsters monster)
    {
        int sum  = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (monster.getMonster(i,j) != null)
                {
                sum++;
                }

            }
        }
        if (sum == 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    //Pauses the game
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
    //Randomly makes the monsters
    void makeMonster(final Monsters monsters, final MonsterView view, int color) {
        color = Color.GREEN;
        if (x <= 0)
        {
            //fills board with nulls
            monsters.clearMonsters();
            x++;
        }
        while ( i < 5 + level) {
            monsters.addMonster((rand.nextInt(9)), (rand.nextInt(9)), color);
            i++;
        }
    }

    //Changes the monster's location and state (color)
    void changeMonster(final Monsters monsters){
        int chance, space,x = 0, y = 0;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (!monsters.spaceEmpty(i, j)) {
                    chance = rand.nextInt(10);
                    if (chance >= 3){
                        if (monsters.getMonster(i,j).getColor() == Color.GREEN){
                           monsters.removeMonster(i, j);
                            monsters.addMonster(i, j, Color.YELLOW);
                        }
                        else if (monsters.getMonster(i,j).getColor() == Color.YELLOW){
                            monsters.removeMonster(i, j);
                            monsters.addMonster(i, j, Color.GREEN);
                        }
                    }
                    //checks which square it will move to
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
                      if ((i + x) <= 9 && (j + y) <= 9 && (i + x) >= 0 && (j + y) >= 0 && monsters.spaceEmpty( (i + x), (j + y)))
                        monsters.moveMonsters(i, j, (i + x), (j + y));

                    }
                }
            }
        }
    }
}