package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class FileLevelGenerator implements LevelGenerator {

    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char EMPTY = '_';

    private Reader source;
    private int width;
    private int height;
    private int aiTankCount;
    private Random random;

    public FileLevelGenerator(Reader source, int width, int height, int aiTankCount, Random random) {
        this.source = source;
        this.width = width;
        this.height = height;
        this.aiTankCount = aiTankCount;
        this.random = random;
    }

    @Override
    public Level generate() {
        List<String> lines = readLines();
        GridPoint2 playerStart = null;
        List<GridPoint2> treeCoordinates = new ArrayList<GridPoint2>();

        for (int row = 0; row < lines.size(); row++) {
            String line = lines.get(row);
            int y = lines.size() - 1 - row;
            for (int x = 0; x < line.length(); x++) {
                char cell = line.charAt(x);
                GridPoint2 coordinates = new GridPoint2(x, y);
                if (cell == TREE) {
                    treeCoordinates.add(coordinates);
                } else if (cell == PLAYER) {
                    if (playerStart != null) {
                        throw new IllegalArgumentException("More than one player");
                    }
                    playerStart = coordinates;
                } else if (cell != EMPTY) {
                    throw new IllegalArgumentException("Bad cell: " + cell);
                }
            }
        }

        if (playerStart == null) {
            throw new IllegalArgumentException("No player");
        }

        List<GridPoint2> aiTankStarts = placeAiTanks(playerStart, treeCoordinates);
        return new Level(playerStart, treeCoordinates, aiTankStarts);
    }

    private List<GridPoint2> placeAiTanks(GridPoint2 playerStart, List<GridPoint2> treeCoordinates) {
        Set<GridPoint2> taken = new HashSet<GridPoint2>();
        taken.add(playerStart);
        taken.addAll(treeCoordinates);

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
        return freeCells.subList(0, aiTankCount);
    }

    private List<String> readLines() {
        List<String> lines = new ArrayList<String>();
        try (BufferedReader reader = new BufferedReader(source)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    lines.add(trimmed);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return lines;
    }
}
