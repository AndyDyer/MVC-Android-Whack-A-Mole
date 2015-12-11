package com.oreilly.demo.android.pa.uidemo.model;

import android.graphics.Color;



import com.oreilly.demo.android.pa.uidemo.MonsterMash;


/** A list of dots. */
public class Monsters {
    /** DotChangeListener. */
    public interface MonstersChangeListener {
        void onMonstersChange(Monsters monster);
    }

    //monster 2d array
    Monster[][] monsterArray = new Monster[10][10];
    private MonstersChangeListener monstersChangeListener;

    /** @param l set the change listener.*/
    public void setMonstersChangeListener(final MonstersChangeListener l) {
        monstersChangeListener = l;
    }

    /** @return the most recently added dot.*/
   /* public Monster getLastMonster() {
        return (monsters.size() <= 0) ? null : monsters.getLast();

    }*/





    /**
     * @param x dot horizontal coordinate.
     * @param y dot vertical coordinate.
     * @param color dot color.
     */
    //adds a monster to the array with xy and color
    public void addMonster(final int x, final int y, final int color) {
        if (spaceEmpty(x,y))
            monsterArray[x][y] = new Monster(x, y, color);

    }


   //clears all monsters fills board with null
    public void clearMonsters() {
        for(int i = 0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                removeMonster(i,j);
            }
        }
    }

    //call to run on tick to avoid calling listener to much
    public void checkBoard() {
        notifyListener();
    }

    //gets monster from array
    public Monster getMonster(int i, int j){
        return monsterArray[i][j];
    }

    //checks state (color) of monster
    public int checkState (int x, int y){
        if(!spaceEmpty(x,y)) {
            Monster temp = monsterArray[x][y];
            int color = temp.getColor();
            if (color == Color.YELLOW)
                MonsterMash.incScore();
            return color;
        }
        else {return 1;}
    }

    //removes monster from array
    public void removeMonster (int x, int y){
        monsterArray[x][y] = null;
        notifyListener();
    }

    //moves monster around in the array
    public void moveMonsters(final int currentX, final int currentY, final int newX, final int newY){
        if (newX <= 9 && newY <= 9 && newX >= 0 && newY >= 0 && !spaceEmpty(currentX,currentY)) {
            int color = monsterArray[currentX][currentY].getColor();
            removeMonster(currentX,currentY);
            addMonster(newX, newY, color);
        }
    }

    // checks if array location is null or not
    public boolean spaceEmpty (final int x, final int y){
        if (monsterArray[x][y] == null) {
            return true;
        }
        else {return false;}
    }

    //Listener update
    private void notifyListener() {
       if (null != monstersChangeListener) {
            monstersChangeListener.onMonstersChange(this);
        }
    }
}
