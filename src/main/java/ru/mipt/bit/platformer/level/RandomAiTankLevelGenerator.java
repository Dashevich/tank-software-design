package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Application. Puts AI tanks on a ready map.
 */
public class RandomAiTankLevelGenerator implements LevelGenerator {

    private LevelGenerator layoutGenerator;
    private int width;
    private int height;
    private int aiTankCount;
    private Random random;

    public RandomAiTankLevelGenerator(
            LevelGenerator layoutGenerator,
            int width,
            int height,
            int aiTankCount,
            Random random
    ) {
        this.layoutGenerator = layoutGenerator;
        this.width = width;
        this.height = height;
        this.aiTankCount = aiTankCount;
        this.random = random;
    }

    @Override
    public Level generate() {
        Level layout = layoutGenerator.generate();
        List<GridPoint2> aiTankStarts = placeAiTanks(layout);
        return new Level(layout.getPlayerStart(), layout.getTreeCoordinates(), aiTankStarts);
    }

    private List<GridPoint2> placeAiTanks(Level layout) {
        Set<GridPoint2> taken = new HashSet<GridPoint2>();
        taken.add(layout.getPlayerStart());
        taken.addAll(layout.getTreeCoordinates());
        taken.addAll(layout.getAiTankStarts());

        List<GridPoint2> freeCells = new ArrayList<GridPoint2>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                GridPoint2 cell = new GridPoint2(x, y);
                if (!taken.contains(cell)) {
                    freeCells.add(cell);
                }
            }
        }

        if (aiTankCount > freeCells.size()) {
            throw new IllegalArgumentException("Too many AI tanks");
        }

        Collections.shuffle(freeCells, random);
        return new ArrayList<GridPoint2>(freeCells.subList(0, aiTankCount));
    }
}
