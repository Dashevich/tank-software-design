package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BulletTest {

    private static final GameField FIELD = new GameField(GameField.WIDTH, GameField.HEIGHT);
    private static final float TANK_SPEED = 0.4f;
    private static final float BULLET_SPEED = 0.1f;

    @Test
    public void damagesAdjacentTankOnFirstTick() {
        LogicalLevel level = new LogicalLevel(FIELD);
        Tank shooter = new Tank(new GridPoint2(1, 1), TANK_SPEED, 1, 1, BULLET_SPEED);
        Tank target = new Tank(new GridPoint2(2, 1), TANK_SPEED, 1, 1, BULLET_SPEED);
        level.add(shooter);
        level.add(target);

        shooter.shoot(level);
        level.tick(0.01f);

        assertFalse(level.contains(target));
        assertFalse(hasBullet(level));
        assertTrue(level.contains(shooter));
    }

    @Test
    public void disappearsOnTreeCollision() {
        LogicalLevel level = new LogicalLevel(FIELD);
        Tank shooter = new Tank(new GridPoint2(1, 1), TANK_SPEED, 1, 1, BULLET_SPEED);
        Tree tree = new Tree(new GridPoint2(2, 1));
        level.add(shooter);
        level.add(tree);

        shooter.shoot(level);
        level.tick(0.01f);

        assertTrue(level.contains(tree));
        assertFalse(hasBullet(level));
    }

    @Test
    public void startsMovingTowardNextCellWhenPathIsClear() {
        LogicalLevel level = new LogicalLevel(FIELD);
        Tank shooter = new Tank(new GridPoint2(1, 1), TANK_SPEED, 1, 1, BULLET_SPEED);
        level.add(shooter);

        shooter.shoot(level);
        Bullet bullet = findBullet(level);
        level.tick(0.01f);

        assertEquals(new GridPoint2(2, 1), bullet.getCoordinates());
        assertEquals(new GridPoint2(3, 1), bullet.getDestinationCoordinates());
        assertFalse(bullet.getMovementProgress() == 1f);
        assertTrue(level.contains(bullet));
    }

    @Test
    public void disappearsOutsideField() {
        LogicalLevel level = new LogicalLevel(FIELD);
        Tank shooter = new Tank(new GridPoint2(GameField.WIDTH - 2, 1), TANK_SPEED, 1, 1, BULLET_SPEED);
        level.add(shooter);

        shooter.shoot(level);
        for (int i = 0; i < 30; i++) {
            level.tick(BULLET_SPEED);
        }

        assertFalse(hasBullet(level));
        assertTrue(level.contains(shooter));
    }

    @Test
    public void bulletsDestroyEachOtherOnSameCell() {
        LogicalLevel level = new LogicalLevel(FIELD);
        Tank left = new Tank(new GridPoint2(1, 1), TANK_SPEED, 1, 1, BULLET_SPEED);
        Tank right = new Tank(new GridPoint2(4, 1), TANK_SPEED, 1, 1, BULLET_SPEED);
        level.add(left);
        level.add(right);
        right.tryMove(Direction.LEFT, FIELD, Collections.<Obstacle>emptyList());

        left.shoot(level);
        right.shoot(level);

        Bullet leftBullet = null;
        Bullet rightBullet = null;
        for (LevelEntity entity : level.getEntities()) {
            if (entity instanceof Bullet) {
                Bullet bullet = (Bullet) entity;
                if (bullet.getOwner() == left) {
                    leftBullet = bullet;
                }
                if (bullet.getOwner() == right) {
                    rightBullet = bullet;
                }
            }
        }

        level.remove(leftBullet);
        level.remove(rightBullet);
        Bullet meeting = new Bullet(level, left, new GridPoint2(2, 1), Direction.RIGHT, 1, BULLET_SPEED);
        Bullet other = new Bullet(level, right, new GridPoint2(2, 1), Direction.LEFT, 1, BULLET_SPEED);
        level.add(meeting);
        level.add(other);
        level.tick(0.01f);

        assertFalse(hasBullet(level));
        assertTrue(level.contains(left));
        assertTrue(level.contains(right));
    }

    private static boolean hasBullet(LogicalLevel level) {
        for (LevelEntity entity : level.getEntities()) {
            if (entity instanceof Bullet) {
                return true;
            }
        }
        return false;
    }

    private static Bullet findBullet(LogicalLevel level) {
        for (LevelEntity entity : level.getEntities()) {
            if (entity instanceof Bullet) {
                return (Bullet) entity;
            }
        }
        throw new AssertionError("Bullet not found");
    }
}
