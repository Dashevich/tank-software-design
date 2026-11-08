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
    private Random random;

    public RandomLevelGenerator(int width, int height, int obstacleCount, Random random) {
        if (obstacleCount < 0 || obstacleCount >= width * height) {
            throw new IllegalArgumentException("Too many obstacles: " + obstacleCount);
        }
        this.width = width;
        this.height = height;
        this.obstacleCount = obstacleCount;
        this.random = random;
    }

    @Override
    public Level generate() {
        List<GridPoint2> cells = allCells();
        Collections.shuffle(cells, random);
        GridPoint2 playerStart = cells.get(0);
        List<GridPoint2> treeCoordinates = cells.subList(1, obstacleCount + 1);
        return new Level(playerStart, treeCoordinates);
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
