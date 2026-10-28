package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FileLevelGeneratorTest {

    private static final String EXAMPLE_LEVEL =
            "_TT___\n"
                    + "TTTTTT\n"
                    + "_____T\n"
                    + "TTTTTT\n"
                    + "_X____\n"
                    + "__\n";
    private static final int WIDTH = 6;
    private static final int HEIGHT = 6;
    private static final int AI_TANK_COUNT = 2;
    private static final long SEED = 42L;

    @Test
    public void readsPlayerStartCountingRowsFromBottom() {
        Level level = generate(EXAMPLE_LEVEL, AI_TANK_COUNT);

        assertEquals(new GridPoint2(1, 1), level.getPlayerStart());
    }

    @Test
    public void readsAllTrees() {
        Level level = generate(EXAMPLE_LEVEL, AI_TANK_COUNT);
        List<GridPoint2> trees = level.getTreeCoordinates();

        assertEquals(15, trees.size());
        assertTrue(trees.contains(new GridPoint2(1, 5)));
        assertTrue(trees.contains(new GridPoint2(2, 5)));
        assertTrue(trees.contains(new GridPoint2(0, 4)));
        assertTrue(trees.contains(new GridPoint2(5, 3)));
        assertTrue(trees.contains(new GridPoint2(0, 2)));
    }

    @Test
    public void placesAiTanksOnFreeCells() {
        Level level = generate(EXAMPLE_LEVEL, AI_TANK_COUNT);

        assertEquals(AI_TANK_COUNT, level.getAiTankStarts().size());

        Set<GridPoint2> taken = new HashSet<GridPoint2>();
        taken.add(level.getPlayerStart());
        taken.addAll(level.getTreeCoordinates());
        for (GridPoint2 aiTank : level.getAiTankStarts()) {
            assertFalse(taken.contains(aiTank));
            taken.add(aiTank);
        }
    }

    @Test
    public void ignoresSurroundingWhitespaceAndBlankLines() {
        Level level = generate("_T_ \n\n_X_  \n", 0);

        assertEquals(new GridPoint2(1, 0), level.getPlayerStart());
        assertEquals(1, level.getTreeCoordinates().size());
        assertEquals(new GridPoint2(1, 1), level.getTreeCoordinates().get(0));
        assertTrue(level.getAiTankStarts().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsLevelWithoutPlayer() {
        generate("_T_\n___\n", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsLevelWithSeveralPlayers() {
        generate("X__\n__X\n", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsUnknownCell() {
        generate("_X_\n_#_\n", 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsTooManyAiTanks() {
        generate("_X_\n___\n", 100);
    }

    private static Level generate(String content, int aiTankCount) {
        return new FileLevelGenerator(
                new StringReader(content),
                WIDTH,
                HEIGHT,
                aiTankCount,
                new Random(SEED)
        ).generate();
    }
}
