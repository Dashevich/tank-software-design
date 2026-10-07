package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {

    private GridPoint2 tankStart;
    private List<Tree> trees;

    public Level(GridPoint2 tankStart, List<Tree> trees) {
        this.tankStart = new GridPoint2(tankStart);
        this.trees = Collections.unmodifiableList(new ArrayList<Tree>(trees));
    }

    public GridPoint2 getTankStart() {
        return tankStart;
    }

    public List<Tree> getTrees() {
        return trees;
    }
}
