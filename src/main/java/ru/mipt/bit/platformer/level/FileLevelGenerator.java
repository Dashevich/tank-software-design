package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adapter. Reads map from a file.
 */
public class FileLevelGenerator implements LevelGenerator {

    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char EMPTY = '_';

    private Reader source;
    private int width;
    private int height;

    public FileLevelGenerator(Reader source, int width, int height) {
        this.source = source;
        this.width = width;
        this.height = height;
    }

    @Override
    public Level generate() {
        List<String> lines = readLines();
        validateSize(lines);
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

        return new Level(playerStart, treeCoordinates, Collections.<GridPoint2>emptyList());
    }

    private void validateSize(List<String> lines) {
        if (lines.size() > height) {
            throw new IllegalArgumentException("Level is too tall");
        }
        for (String line : lines) {
            if (line.length() > width) {
                throw new IllegalArgumentException("Level is too wide");
            }
        }
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
