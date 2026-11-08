package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level {

    private GridPoint2 playerStart;
    private List<GridPoint2> treeCoordinates;

    public Level(GridPoint2 playerStart, List<GridPoint2> treeCoordinates) {
        this.playerStart = new GridPoint2(playerStart);
        this.treeCoordinates = new ArrayList<GridPoint2>(treeCoordinates);
    }

    public GridPoint2 getPlayerStart() {
        return new GridPoint2(playerStart);
    }

    public List<GridPoint2> getTreeCoordinates() {
        return Collections.unmodifiableList(treeCoordinates);
    }
}
