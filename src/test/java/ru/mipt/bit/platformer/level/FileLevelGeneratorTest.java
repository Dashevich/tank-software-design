package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
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

    @Test
    public void readsPlayerStartCountingRowsFromBottom() {
        Level level = generate(EXAMPLE_LEVEL);

        assertEquals(new GridPoint2(1, 1), level.getPlayerStart());
    }

    @Test
    public void readsAllTrees() {
        Level level = generate(EXAMPLE_LEVEL);
        List<GridPoint2> trees = level.getTreeCoordinates();

        assertEquals(15, trees.size());
        assertTrue(trees.contains(new GridPoint2(1, 5)));
        assertTrue(trees.contains(new GridPoint2(2, 5)));
        assertTrue(trees.contains(new GridPoint2(0, 4)));
        assertTrue(trees.contains(new GridPoint2(5, 3)));
        assertTrue(trees.contains(new GridPoint2(0, 2)));
    }

    @Test
    public void doesNotPlaceAiTanks() {
        Level level = generate(EXAMPLE_LEVEL);

        assertTrue(level.getAiTankStarts().isEmpty());
    }

    @Test
    public void ignoresSurroundingWhitespaceAndBlankLines() {
        Level level = generate("_T_ \n\n_X_  \n");

        assertEquals(new GridPoint2(1, 0), level.getPlayerStart());
        assertEquals(1, level.getTreeCoordinates().size());
        assertEquals(new GridPoint2(1, 1), level.getTreeCoordinates().get(0));
        assertTrue(level.getAiTankStarts().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsLevelWithoutPlayer() {
        generate("_T_\n___\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsLevelWithSeveralPlayers() {
        generate("X__\n__X\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsUnknownCell() {
        generate("_X_\n_#_\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsLevelWiderThanField() {
        generate("_______X\n________\n");
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsLevelTallerThanField() {
        generate(
                "______\n"
                        + "______\n"
                        + "______\n"
                        + "______\n"
                        + "______\n"
                        + "______\n"
                        + "_X____\n"
        );
    }

    private static Level generate(String content) {
        return new FileLevelGenerator(new StringReader(content), WIDTH, HEIGHT).generate();
    }
}
