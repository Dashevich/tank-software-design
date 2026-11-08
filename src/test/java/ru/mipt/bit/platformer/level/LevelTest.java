package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LevelTest {

    @Test
    public void storesPlayerStartAndTrees() {
        List<GridPoint2> trees = new ArrayList<GridPoint2>();
        trees.add(new GridPoint2(1, 3));
        trees.add(new GridPoint2(4, 2));

        Level level = new Level(new GridPoint2(1, 1), trees);

        assertEquals(new GridPoint2(1, 1), level.getPlayerStart());
        assertEquals(trees, level.getTreeCoordinates());
    }

    @Test
    public void isNotAffectedBySourceListChanges() {
        List<GridPoint2> trees = new ArrayList<GridPoint2>();
        trees.add(new GridPoint2(1, 3));
        Level level = new Level(new GridPoint2(1, 1), trees);

        trees.add(new GridPoint2(4, 2));

        assertEquals(1, level.getTreeCoordinates().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void treeCoordinatesCannotBeModified() {
        Level level = new Level(new GridPoint2(1, 1), new ArrayList<GridPoint2>());

        level.getTreeCoordinates().add(new GridPoint2(0, 0));
    }
}
