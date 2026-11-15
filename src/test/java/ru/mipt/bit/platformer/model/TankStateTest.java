package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TankStateTest {

    private static final GameField FIELD = new GameField(GameField.WIDTH, GameField.HEIGHT);
    private static final float MOVEMENT_SPEED = 0.4f;
    private static final int MAX_HEALTH = 100;
    private static final int BULLET_DAMAGE = 1;
    private static final float BULLET_SPEED = 0.15f;

    @Test
    public void startsInLightState() {
        Tank tank = tankWithHealth(MAX_HEALTH);

        assertSame(LightTankState.INSTANCE, tank.getState());
        assertTrue(tank.getState().canShoot());
        assertEquals(1f, tank.getState().movementDurationFactor(), 0.000001f);
    }

    @Test
    public void becomesMediumBetweenFifteenAndSeventyPercent() {
        Tank tank = tankWithHealth(MAX_HEALTH);

        tank.takeDamage(30);
        assertSame(MediumTankState.INSTANCE, tank.getState());

        tank.takeDamage(55);
        assertEquals(15, tank.getHealth());
        assertSame(MediumTankState.INSTANCE, tank.getState());
    }

    @Test
    public void becomesHeavyBelowFifteenPercent() {
        Tank tank = tankWithHealth(MAX_HEALTH);

        tank.takeDamage(86);

        assertEquals(14, tank.getHealth());
        assertSame(HeavyTankState.INSTANCE, tank.getState());
        assertFalse(tank.getState().canShoot());
        assertEquals(3f, tank.getState().movementDurationFactor(), 0.000001f);
    }

    @Test
    public void mediumStateMovesTwiceAsSlow() {
        Tank tank = tankWithHealth(MAX_HEALTH);
        tank.takeDamage(40);
        tank.tryMove(Direction.RIGHT, FIELD, Collections.<Obstacle>emptyList());

        tank.live(0.2f);

        assertEquals(0.25f, tank.getMovementProgress(), 0.000001f);
    }

    @Test
    public void heavyStateMovesThreeTimesAsSlow() {
        Tank tank = tankWithHealth(MAX_HEALTH);
        tank.takeDamage(90);
        tank.tryMove(Direction.RIGHT, FIELD, Collections.<Obstacle>emptyList());

        tank.live(0.2f);

        assertEquals(1f / 6f, tank.getMovementProgress(), 0.000001f);
    }

    @Test
    public void heavyStateCannotShoot() {
        LogicalLevel level = new LogicalLevel(FIELD);
        Tank tank = tankWithHealth(MAX_HEALTH);
        level.add(tank);
        tank.takeDamage(90);

        tank.shoot(level);

        assertEquals(1, level.getEntities().size());
    }

    @Test
    public void customStateCanOverrideBehaviour() {
        LogicalLevel level = new LogicalLevel(FIELD);
        Tank tank = tankWithHealth(MAX_HEALTH);
        level.add(tank);
        tank.setState(new FastBoosterState());

        tank.tryMove(Direction.RIGHT, FIELD, Collections.<Obstacle>emptyList());
        tank.live(0.2f);

        assertEquals(1f, tank.getMovementProgress(), 0.000001f);
        tank.shoot(level);
        assertEquals(2, level.getEntities().size());
    }

    @Test
    public void selectorBoundaries() {
        assertSame(LightTankState.INSTANCE, HealthBasedTankStateSelector.select(0.71f));
        assertSame(MediumTankState.INSTANCE, HealthBasedTankStateSelector.select(0.7f));
        assertSame(MediumTankState.INSTANCE, HealthBasedTankStateSelector.select(0.15f));
        assertSame(HeavyTankState.INSTANCE, HealthBasedTankStateSelector.select(0.149f));
    }

    private static Tank tankWithHealth(int health) {
        return new Tank(new GridPoint2(1, 1), MOVEMENT_SPEED, health, BULLET_DAMAGE, BULLET_SPEED);
    }

    private static class FastBoosterState implements TankState {
        @Override
        public float movementDurationFactor() {
            return 0.5f;
        }

        @Override
        public boolean canShoot() {
            return true;
        }
    }
}
