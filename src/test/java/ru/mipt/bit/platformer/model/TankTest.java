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
    private static final float MOVEMENT_SPEED = 0.4f;

    @Test
    public void startsIdleAtGivenCell() {
        Tank tank = new Tank(new GridPoint2(1, 1), MOVEMENT_SPEED);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getRotation(), 0.000001f);
        assertTrue(tank.hasFinishedMovement());
        assertTrue(tank.occupies(new GridPoint2(1, 1)));
        assertFalse(tank.occupies(new GridPoint2(2, 1)));
    }

    @Test
    public void tryMoveChangesDestinationAndRotation() {
        Tank tank = new Tank(new GridPoint2(1, 1), MOVEMENT_SPEED);

        tank.tryMove(Direction.UP, FIELD, Collections.<Obstacle>emptyList());

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(90f, tank.getRotation(), 0.000001f);
        assertFalse(tank.hasFinishedMovement());
        assertEquals(0f, tank.getMovementProgress(), 0.000001f);
    }

    @Test
    public void occupiesBothCellsWhileMoving() {
        Tank tank = new Tank(new GridPoint2(1, 1), MOVEMENT_SPEED);
        tank.tryMove(Direction.RIGHT, FIELD, Collections.<Obstacle>emptyList());

        assertTrue(tank.occupies(new GridPoint2(1, 1)));
        assertTrue(tank.occupies(new GridPoint2(2, 1)));
        assertFalse(tank.occupies(new GridPoint2(3, 1)));
    }

    @Test
    public void tryMoveOntoObstacleOnlyRotates() {
        Tank tank = new Tank(new GridPoint2(1, 1), MOVEMENT_SPEED);
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        obstacles.add(new OccupiedCell(new GridPoint2(1, 2)));

        tank.tryMove(Direction.UP, FIELD, obstacles);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(90f, tank.getRotation(), 0.000001f);
        assertTrue(tank.hasFinishedMovement());
    }

    @Test
    public void tryMoveOutsideFieldOnlyRotates() {
        Tank tank = new Tank(new GridPoint2(0, 1), MOVEMENT_SPEED);

        tank.tryMove(Direction.LEFT, FIELD, Collections.<Obstacle>emptyList());

        assertEquals(new GridPoint2(0, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(0, 1), tank.getDestinationCoordinates());
        assertEquals(-180f, tank.getRotation(), 0.000001f);
        assertTrue(tank.hasFinishedMovement());
    }

    @Test
    public void cannotEnterDestinationCellOfMovingTank() {
        Tank moving = new Tank(new GridPoint2(1, 1), MOVEMENT_SPEED);
        Tank other = new Tank(new GridPoint2(3, 1), MOVEMENT_SPEED);
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        obstacles.add(moving);
        obstacles.add(other);

        moving.tryMove(Direction.RIGHT, FIELD, obstacles);
        other.tryMove(Direction.LEFT, FIELD, obstacles);

        assertEquals(new GridPoint2(2, 1), moving.getDestinationCoordinates());
        assertEquals(new GridPoint2(3, 1), other.getDestinationCoordinates());
        assertTrue(other.hasFinishedMovement());
    }

    @Test
    public void cannotEnterStartCellOfMovingTank() {
        Tank moving = new Tank(new GridPoint2(2, 1), MOVEMENT_SPEED);
        Tank other = new Tank(new GridPoint2(1, 1), MOVEMENT_SPEED);
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        obstacles.add(moving);
        obstacles.add(other);

        moving.tryMove(Direction.RIGHT, FIELD, obstacles);
        other.tryMove(Direction.RIGHT, FIELD, obstacles);

        assertEquals(new GridPoint2(3, 1), moving.getDestinationCoordinates());
        assertEquals(new GridPoint2(1, 1), other.getDestinationCoordinates());
        assertTrue(other.hasFinishedMovement());
    }

    @Test
    public void tryMoveIgnoredWhileMoving() {
        Tank tank = new Tank(new GridPoint2(1, 1), MOVEMENT_SPEED);
        List<Obstacle> obstacles = Collections.emptyList();
        tank.tryMove(Direction.RIGHT, FIELD, obstacles);

        tank.tryMove(Direction.UP, FIELD, obstacles);

        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getRotation(), 0.000001f);
    }

    @Test
    public void updateAdvancesProgressAndFinishesMovement() {
        Tank tank = new Tank(new GridPoint2(1, 1), MOVEMENT_SPEED);
        tank.tryMove(Direction.RIGHT, FIELD, Collections.<Obstacle>emptyList());

        tank.update(0.2f);

        assertEquals(0.5f, tank.getMovementProgress(), 0.000001f);
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());

        tank.update(0.2f);

        assertTrue(tank.hasFinishedMovement());
        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
        assertTrue(tank.occupies(new GridPoint2(2, 1)));
        assertFalse(tank.occupies(new GridPoint2(1, 1)));
    }

    @Test
    public void slowerSpeedTakesLongerToFinishMovement() {
        Tank tank = new Tank(new GridPoint2(1, 1), 0.8f);
        tank.tryMove(Direction.RIGHT, FIELD, Collections.<Obstacle>emptyList());

        tank.update(0.2f);

        assertEquals(0.25f, tank.getMovementProgress(), 0.000001f);
        assertFalse(tank.hasFinishedMovement());
    }

    private static class OccupiedCell implements Obstacle {
        private GridPoint2 cell;

        private OccupiedCell(GridPoint2 cell) {
            this.cell = cell;
        }

        @Override
        public boolean occupies(GridPoint2 point) {
            return cell.equals(point);
        }
    }
}
