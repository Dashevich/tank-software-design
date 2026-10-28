package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomLevelGenerator implements LevelGenerator {

    private int width;
    private int height;
    private int obstacleCount;
    private int aiTankCount;
    private Random random;

    public RandomLevelGenerator(int width, int height, int obstacleCount, int aiTankCount, Random random) {
        int reservedCells = obstacleCount + aiTankCount + 1;
        if (obstacleCount < 0 || aiTankCount < 0 || reservedCells > width * height) {
            throw new IllegalArgumentException("Too many objects");
        }
        this.width = width;
        this.height = height;
        this.obstacleCount = obstacleCount;
        this.aiTankCount = aiTankCount;
        this.random = random;
    }

    @Override
    public Level generate() {
        List<GridPoint2> cells = allCells();
        Collections.shuffle(cells, random);
        GridPoint2 playerStart = cells.get(0);
        int treesEnd = 1 + obstacleCount;
        List<GridPoint2> treeCoordinates = cells.subList(1, treesEnd);
        List<GridPoint2> aiTankStarts = cells.subList(treesEnd, treesEnd + aiTankCount);
        return new Level(playerStart, treeCoordinates, aiTankStarts);
    }

    private List<GridPoint2> allCells() {
        List<GridPoint2> cells = new ArrayList<GridPoint2>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells.add(new GridPoint2(x, y));
            }
        }
        return cells;
    }
}
