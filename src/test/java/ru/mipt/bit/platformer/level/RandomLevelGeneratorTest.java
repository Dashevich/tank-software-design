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
    private static final int AI_TANK_COUNT = 3;
    private static final long SEED = 42L;

    @Test
    public void generatesRequestedNumberOfTreesAndAiTanks() {
        RandomLevelGenerator generator = new RandomLevelGenerator(
                GameField.WIDTH, GameField.HEIGHT, OBSTACLE_COUNT, AI_TANK_COUNT, new Random(SEED));

        Level level = generator.generate();

        assertEquals(OBSTACLE_COUNT, level.getTreeCoordinates().size());
        assertEquals(AI_TANK_COUNT, level.getAiTankStarts().size());
    }

    @Test
    public void placesEverythingInsideField() {
        GameField field = new GameField(GameField.WIDTH, GameField.HEIGHT);
        RandomLevelGenerator generator = new RandomLevelGenerator(
                GameField.WIDTH, GameField.HEIGHT, OBSTACLE_COUNT, AI_TANK_COUNT, new Random(SEED));

        Level level = generator.generate();

        assertTrue(field.contains(level.getPlayerStart()));
        for (GridPoint2 tree : level.getTreeCoordinates()) {
            assertTrue(field.contains(tree));
        }
        for (GridPoint2 aiTank : level.getAiTankStarts()) {
            assertTrue(field.contains(aiTank));
        }
    }

    @Test
    public void objectsDoNotOverlap() {
        RandomLevelGenerator generator = new RandomLevelGenerator(
                GameField.WIDTH, GameField.HEIGHT, OBSTACLE_COUNT, AI_TANK_COUNT, new Random(SEED));

        Level level = generator.generate();

        Set<GridPoint2> unique = new HashSet<GridPoint2>();
        unique.add(level.getPlayerStart());
        unique.addAll(level.getTreeCoordinates());
        unique.addAll(level.getAiTankStarts());

        assertEquals(1 + OBSTACLE_COUNT + AI_TANK_COUNT, unique.size());
        assertFalse(level.getTreeCoordinates().contains(level.getPlayerStart()));
        assertFalse(level.getAiTankStarts().contains(level.getPlayerStart()));
    }

    @Test
    public void sameSeedGivesSameLevel() {
        Level first = new RandomLevelGenerator(
                GameField.WIDTH, GameField.HEIGHT, OBSTACLE_COUNT, AI_TANK_COUNT, new Random(SEED)).generate();
        Level second = new RandomLevelGenerator(
                GameField.WIDTH, GameField.HEIGHT, OBSTACLE_COUNT, AI_TANK_COUNT, new Random(SEED)).generate();

        assertEquals(first.getPlayerStart(), second.getPlayerStart());
        assertEquals(first.getTreeCoordinates(), second.getTreeCoordinates());
        assertEquals(first.getAiTankStarts(), second.getAiTankStarts());
    }

    @Test
    public void fillsAllCellsExceptPlayer() {
        int width = 3;
        int height = 2;
        RandomLevelGenerator generator = new RandomLevelGenerator(
                width, height, 2, 3, new Random(SEED));

        Level level = generator.generate();

        assertEquals(2, level.getTreeCoordinates().size());
        assertEquals(3, level.getAiTankStarts().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsTooManyObjects() {
        new RandomLevelGenerator(2, 2, 2, 2, new Random(SEED));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNegativeObstacleCount() {
        new RandomLevelGenerator(2, 2, -1, 0, new Random(SEED));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsNegativeAiTankCount() {
        new RandomLevelGenerator(2, 2, 0, -1, new Random(SEED));
    }
}
