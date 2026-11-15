package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class RandomAiTankLevelGeneratorTest {

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

    @Test(expected = IllegalArgumentException.class)
    public void rejectsTooManyAiTanks() {
        generate("_X_\n___\n", 100);
    }

    private static Level generate(String content, int aiTankCount) {
        return new RandomAiTankLevelGenerator(
                new FileLevelGenerator(new StringReader(content), WIDTH, HEIGHT),
                WIDTH,
                HEIGHT,
                aiTankCount,
                new Random(SEED)
        ).generate();
    }
}
