package com.oreilly.demo.android.pa.uidemo.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.oreilly.demo.android.pa.uidemo.view.MonsterView;

/** A list of dots. */
public class Monsters {
    /** DotChangeListener. */
    public interface MonstersChangeListener {
        void onMonstersChange(Monsters monster);
    }
    private volatile MonsterView view;
    private final LinkedList<Monster> monsters = new LinkedList<>();
    private final List<Monster> safeMonsters = Collections.unmodifiableList(monsters);
    Monster[][] monsterArray = new Monster[10][10];
    private MonstersChangeListener monstersChangeListener;

    /** @param l set the change listener. */
    public void setMonstersChangeListener(final MonstersChangeListener l) {
        monstersChangeListener = l;
    }

    /** @return the most recently added dot. */
    public Monster getLastMonster() {
        return (monsters.size() <= 0) ? null : monsters.getLast();

    }


    /** @return immutable list of dots. */
    //public List<Monster> getMonsters() { return safeMonsters; }

    /**
     * @param x dot horizontal coordinate.
     * @param y dot vertical coordinate.
     * @param color dot color.
     */
    public void addMonster(final int x, final int y, final int color) {
        if (spaceEmpty(x,y))
            monsterArray[x][y] = new Monster(x, y, color);
       // monsters.add(new Monster(x, y, color));
        notifyListener();

    }

    public int getX(int x, int y){
       return (int) monsterArray[x][y].getX();
    }
    public int getY(int x, int y){
        return (int) monsterArray[x][y].getY();
    }

    public int getColor(int x, int y){
        return monsterArray[x][y].getColor();
    }
    /** Remove all dots. */
    public void clearMonsters() {
        for(int i = 0; i<10; i++) {
            for (int j = 0; j < 10; j++) {
                monsterArray[i][j] = null;
            }
        }
        notifyListener();

    }

    public Monster getMonster(int i, int j){

        return monsterArray[i][j];



    }
    public int checkState (int x, int y){
        Monster temp = monsterArray[x][y];
        int color = temp.getColor();
        return color;
    }

    public void removeMonster (int x, int y){
        monsterArray[x][y] = null;
        notifyListener();
    }

    public void moveMonsters(final int currentX, final int currentY, final int newX, final int newY){
        monsterArray[newX][newY] = monsterArray [currentX][currentY];
        monsterArray[currentX][currentY] = null;
        notifyListener();
    }

    public boolean spaceEmpty (final int x, final int y){
        if (monsterArray[x][y] == null)
            return true;
        else
            return false;
    }

    private void notifyListener() {
        if (null != monstersChangeListener) {
            monstersChangeListener.onMonstersChange(this);
        }
    }
}
