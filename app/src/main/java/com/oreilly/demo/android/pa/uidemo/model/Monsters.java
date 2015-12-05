package com.oreilly.demo.android.pa.uidemo.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/** A list of dots. */
public class Monsters {
    /** DotChangeListener. */
    public interface MonstersChangeListener {
        void onMonstersChange(Monsters monster);
    }

    private final LinkedList<Monster> monsters = new LinkedList<>();
    private final List<Monster> safeMonsters = Collections.unmodifiableList(monsters);

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
    public List<Monster> getMonsters() { return safeMonsters; }

    /**
     * @param x dot horizontal coordinate.
     * @param y dot vertical coordinate.
     * @param color dot color.
     * @param diameter dot size.
     */
    public void addMonster(final float x, final float y, final int color, final int diameter) {
        monsters.add(new Monster(x, y, color, diameter));
        notifyListener();
    }

    /** Remove all dots. */
    public void clearMonsters() {
        monsters.clear();
        notifyListener();
    }

    private void notifyListener() {
        if (null != monstersChangeListener) {
            monstersChangeListener.onMonstersChange(this);
        }
    }
}
