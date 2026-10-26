package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class LevelTest {

    @Test
    public void storesPlayerTreesAndAiTanks() {
        List<GridPoint2> trees = new ArrayList<GridPoint2>();
        trees.add(new GridPoint2(1, 3));
        List<GridPoint2> aiTanks = new ArrayList<GridPoint2>();
        aiTanks.add(new GridPoint2(4, 2));

        Level level = new Level(new GridPoint2(1, 1), trees, aiTanks);

        assertEquals(new GridPoint2(1, 1), level.getPlayerStart());
        assertEquals(trees, level.getTreeCoordinates());
        assertEquals(aiTanks, level.getAiTankStarts());
    }

    @Test
    public void isNotAffectedBySourceListChanges() {
        List<GridPoint2> trees = new ArrayList<GridPoint2>();
        trees.add(new GridPoint2(1, 3));
        List<GridPoint2> aiTanks = new ArrayList<GridPoint2>();
        aiTanks.add(new GridPoint2(2, 2));
        Level level = new Level(new GridPoint2(1, 1), trees, aiTanks);

        trees.add(new GridPoint2(4, 2));
        aiTanks.add(new GridPoint2(5, 5));

        assertEquals(1, level.getTreeCoordinates().size());
        assertEquals(1, level.getAiTankStarts().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void treeCoordinatesCannotBeModified() {
        Level level = new Level(new GridPoint2(1, 1), new ArrayList<GridPoint2>(), Collections.<GridPoint2>emptyList());

        level.getTreeCoordinates().add(new GridPoint2(0, 0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void aiTankStartsCannotBeModified() {
        Level level = new Level(new GridPoint2(1, 1), Collections.<GridPoint2>emptyList(), new ArrayList<GridPoint2>());

        level.getAiTankStarts().add(new GridPoint2(0, 0));
    }
}
