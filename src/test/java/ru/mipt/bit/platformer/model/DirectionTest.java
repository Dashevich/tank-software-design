package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DirectionTest {

    @Test
    public void upHasVectorAndRotation() {
        assertEquals(new GridPoint2(0, 1), Direction.UP.getVector());
        assertEquals(90f, Direction.UP.getRotation(), 0.000001f);
    }

    @Test
    public void leftHasVectorAndRotation() {
        assertEquals(new GridPoint2(-1, 0), Direction.LEFT.getVector());
        assertEquals(-180f, Direction.LEFT.getRotation(), 0.000001f);
    }

    @Test
    public void downHasVectorAndRotation() {
        assertEquals(new GridPoint2(0, -1), Direction.DOWN.getVector());
        assertEquals(-90f, Direction.DOWN.getRotation(), 0.000001f);
    }

    @Test
    public void rightHasVectorAndRotation() {
        assertEquals(new GridPoint2(1, 0), Direction.RIGHT.getVector());
        assertEquals(0f, Direction.RIGHT.getRotation(), 0.000001f);
    }

    @Test
    public void nextCoordinatesAddsDirectionVector() {
        GridPoint2 start = new GridPoint2(2, 3);

        assertEquals(new GridPoint2(2, 4), Direction.UP.nextCoordinates(start));
        assertEquals(new GridPoint2(1, 3), Direction.LEFT.nextCoordinates(start));
        assertEquals(new GridPoint2(2, 2), Direction.DOWN.nextCoordinates(start));
        assertEquals(new GridPoint2(3, 3), Direction.RIGHT.nextCoordinates(start));
        assertEquals(new GridPoint2(2, 3), start);
    }

    @Test
    public void getVectorReturnsCopy() {
        GridPoint2 vector = Direction.UP.getVector();
        vector.add(5, 5);

        assertEquals(new GridPoint2(0, 1), Direction.UP.getVector());
    }
}
