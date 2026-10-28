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
    private static final int HEALTH = 1;
    private static final int BULLET_DAMAGE = 1;
    private static final float BULLET_SPEED = 0.15f;

    @Test
    public void startsIdleAtGivenCell() {
        Tank tank = tankAt(1, 1);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(0f, tank.getRotation(), 0.000001f);
        assertTrue(tank.hasFinishedMovement());
        assertTrue(tank.occupies(new GridPoint2(1, 1)));
        assertFalse(tank.occupies(new GridPoint2(2, 1)));
        assertEquals(HEALTH, tank.getHealth());
    }

    @Test
    public void tryMoveChangesDestinationAndOrientation() {
        Tank tank = tankAt(1, 1);

        tank.tryMove(Direction.UP, FIELD, Collections.<Obstacle>emptyList());

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(Direction.UP, tank.getOrientation());
        assertFalse(tank.hasFinishedMovement());
    }

    @Test
    public void occupiesBothCellsWhileMoving() {
        Tank tank = tankAt(1, 1);
        tank.tryMove(Direction.RIGHT, FIELD, Collections.<Obstacle>emptyList());

        assertTrue(tank.occupies(new GridPoint2(1, 1)));
        assertTrue(tank.occupies(new GridPoint2(2, 1)));
        assertFalse(tank.occupies(new GridPoint2(3, 1)));
    }

    @Test
    public void tryMoveOntoObstacleOnlyRotates() {
        Tank tank = tankAt(1, 1);
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        obstacles.add(new OccupiedCell(new GridPoint2(1, 2)));

        tank.tryMove(Direction.UP, FIELD, obstacles);

        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertEquals(Direction.UP, tank.getOrientation());
        assertTrue(tank.hasFinishedMovement());
    }

    @Test
    public void tryMoveOutsideFieldOnlyRotates() {
        Tank tank = tankAt(0, 1);

        tank.tryMove(Direction.LEFT, FIELD, Collections.<Obstacle>emptyList());

        assertEquals(new GridPoint2(0, 1), tank.getDestinationCoordinates());
        assertEquals(Direction.LEFT, tank.getOrientation());
        assertTrue(tank.hasFinishedMovement());
    }

    @Test
    public void cannotEnterDestinationCellOfMovingTank() {
        Tank moving = tankAt(1, 1);
        Tank other = tankAt(3, 1);
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
        Tank moving = tankAt(2, 1);
        Tank other = tankAt(1, 1);
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
    public void liveAdvancesProgressAndFinishesMovement() {
        Tank tank = tankAt(1, 1);
        tank.tryMove(Direction.RIGHT, FIELD, Collections.<Obstacle>emptyList());

        tank.live(0.2f);
        assertEquals(0.5f, tank.getMovementProgress(), 0.000001f);

        tank.live(0.2f);
        assertTrue(tank.hasFinishedMovement());
        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
    }

    @Test
    public void takeDamageDestroysTankAtZeroOrBelow() {
        Tank tank = tankAt(1, 1);

        tank.takeDamage(1);

        assertEquals(0, tank.getHealth());
        assertTrue(tank.isDestroyed());
    }

    @Test
    public void shootAddsBulletInFrontOfTank() {
        LogicalLevel level = new LogicalLevel(FIELD);
        Tank tank = tankAt(1, 1);
        level.add(tank);

        tank.shoot(level);

        assertEquals(2, level.getEntities().size());
        Bullet bullet = findBullet(level);
        assertEquals(new GridPoint2(2, 1), bullet.getCoordinates());
        assertEquals(Direction.RIGHT, bullet.getDirection());
        assertEquals(tank, bullet.getOwner());
    }

    @Test
    public void shootDoesNothingAtFieldEdge() {
        LogicalLevel level = new LogicalLevel(FIELD);
        Tank tank = tankAt(GameField.WIDTH - 1, 1);
        level.add(tank);

        tank.shoot(level);

        assertEquals(1, level.getEntities().size());
    }

    private static Tank tankAt(int x, int y) {
        return new Tank(new GridPoint2(x, y), MOVEMENT_SPEED, HEALTH, BULLET_DAMAGE, BULLET_SPEED);
    }

    private static Bullet findBullet(LogicalLevel level) {
        for (LevelEntity entity : level.getEntities()) {
            if (entity instanceof Bullet) {
                return (Bullet) entity;
            }
        }
        throw new AssertionError("Bullet not found");
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
