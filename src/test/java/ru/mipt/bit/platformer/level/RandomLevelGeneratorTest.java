package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.GameField;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RandomLevelGeneratorTest {

    private static final int OBSTACLE_COUNT = 10;
    private static final long SEED = 42L;

    @Test
    public void generatesRequestedNumberOfTrees() {
        RandomLevelGenerator generator = new RandomLevelGenerator(
                GameField.WIDTH, GameField.HEIGHT, OBSTACLE_COUNT, new Random(SEED));

        Level level = generator.generate();

        assertEquals(OBSTACLE_COUNT, level.getTreeCoordinates().size());
    }

    @Test
    public void placesEverythingInsideField() {
        GameField field = new GameField(GameField.WIDTH, GameField.HEIGHT);
        RandomLevelGenerator generator = new RandomLevelGenerator(
                GameField.WIDTH, GameField.HEIGHT, OBSTACLE_COUNT, new Random(SEED));

        Level level = generator.generate();

        assertTrue(field.contains(level.getPlayerStart()));
        for (GridPoint2 tree : level.getTreeCoordinates()) {
            assertTrue(field.contains(tree));
        }
    }

    @Test
    public void treesDoNotOverlapEachOtherOrPlayer() {
        RandomLevelGenerator generator = new RandomLevelGenerator(
                GameField.WIDTH, GameField.HEIGHT, OBSTACLE_COUNT, new Random(SEED));

        Level level = generator.generate();

        Set<GridPoint2> unique = new HashSet<GridPoint2>(level.getTreeCoordinates());
        assertEquals(OBSTACLE_COUNT, unique.size());
        assertFalse(unique.contains(level.getPlayerStart()));
    }

    @Test
    public void sameSeedGivesSameLevel() {
        Level first = new RandomLevelGenerator(
                GameField.WIDTH, GameField.HEIGHT, OBSTACLE_COUNT, new Random(SEED)).generate();
        Level second = new RandomLevelGenerator(
                GameField.WIDTH, GameField.HEIGHT, OBSTACLE_COUNT, new Random(SEED)).generate();

        assertEquals(first.getPlayerStart(), second.getPlayerStart());
        assertEquals(first.getTreeCoordinates(), second.getTreeCoordinates());
    }

    @Test
    public void fillsAllCellsExceptPlayer() {
        int width = 3;
        int height = 2;
        RandomLevelGenerator generator = new RandomLevelGenerator(width, height, width * height - 1, new Random(SEED));

        Level level = generator.generate();

        assertEquals(width * height - 1, level.getTreeCoordinates().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsTooManyObstacles() {
        new RandomLevelGenerator(2, 2, 4, new Random(SEED));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNegativeObstacleCount() {
        new RandomLevelGenerator(2, 2, -1, new Random(SEED));
    }
}
