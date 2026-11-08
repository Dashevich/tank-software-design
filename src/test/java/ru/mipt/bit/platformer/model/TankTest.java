package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TankTest {

    private static final GameField FIELD = new GameField(GameField.WIDTH, GameField.HEIGHT);

    @Test
    public void startsIdleAtGivenCell() {
        Tank tank = new Tank(new GridPoint2(1, 1));

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getRotation(), 0.000001f);
        assertTrue(tank.hasFinishedMovement());
    }

    @Test
    public void tryMoveChangesDestinationAndRotation() {
        Tank tank = new Tank(new GridPoint2(1, 1));

        tank.tryMove(Direction.UP, FIELD, Collections.<Tree>emptyList());

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(90f, tank.getRotation(), 0.000001f);
        assertFalse(tank.hasFinishedMovement());
        assertEquals(0f, tank.getMovementProgress(), 0.000001f);
    }

    @Test
    public void tryMoveOntoTreeOnlyRotates() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        List<Tree> trees = new ArrayList<Tree>();
        trees.add(new Tree(new GridPoint2(1, 2)));

        tank.tryMove(Direction.UP, FIELD, trees);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(90f, tank.getRotation(), 0.000001f);
        assertTrue(tank.hasFinishedMovement());
    }

    @Test
    public void tryMoveOutsideFieldOnlyRotates() {
        Tank tank = new Tank(new GridPoint2(0, 1));

        tank.tryMove(Direction.LEFT, FIELD, Collections.<Tree>emptyList());

        assertEquals(new GridPoint2(0, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(0, 1), tank.getDestinationCoordinates());
        assertEquals(-180f, tank.getRotation(), 0.000001f);
        assertTrue(tank.hasFinishedMovement());
    }

    @Test
    public void tryMoveIgnoredWhileMoving() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        List<Tree> trees = Collections.emptyList();
        tank.tryMove(Direction.RIGHT, FIELD, trees);

        tank.tryMove(Direction.UP, FIELD, trees);

        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getRotation(), 0.000001f);
    }

    @Test
    public void updateAdvancesProgressAndFinishesMovement() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        tank.tryMove(Direction.RIGHT, FIELD, Collections.<Tree>emptyList());

        tank.update(0.2f);

        assertEquals(0.5f, tank.getMovementProgress(), 0.000001f);
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());

        tank.update(0.2f);

        assertTrue(tank.hasFinishedMovement());
        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
    }
}
