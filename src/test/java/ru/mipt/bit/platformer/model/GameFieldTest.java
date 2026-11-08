package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameFieldTest {

    @Test
    public void storesSize() {
        GameField field = new GameField(GameField.WIDTH, GameField.HEIGHT);

        assertEquals(GameField.WIDTH, field.getWidth());
        assertEquals(GameField.HEIGHT, field.getHeight());
    }

    @Test
    public void containsCellsInsideBounds() {
        GameField field = new GameField(GameField.WIDTH, GameField.HEIGHT);

        assertTrue(field.contains(new GridPoint2(0, 0)));
        assertTrue(field.contains(new GridPoint2(GameField.WIDTH - 1, GameField.HEIGHT - 1)));
        assertTrue(field.contains(new GridPoint2(1, 3)));
    }

    @Test
    public void doesNotContainCellsOutsideBounds() {
        GameField field = new GameField(GameField.WIDTH, GameField.HEIGHT);

        assertFalse(field.contains(new GridPoint2(-1, 0)));
        assertFalse(field.contains(new GridPoint2(0, -1)));
        assertFalse(field.contains(new GridPoint2(GameField.WIDTH, 0)));
        assertFalse(field.contains(new GridPoint2(0, GameField.HEIGHT)));
    }
}
