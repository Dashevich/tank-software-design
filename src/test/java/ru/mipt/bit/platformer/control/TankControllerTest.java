package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TankControllerTest {

    private static final GameField FIELD = new GameField(GameField.WIDTH, GameField.HEIGHT);
    private static final float SPEED = 0.4f;

    @Test
    public void playerStrategyDoesNotMoveTank() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        obstacles.add(tank);
        TankController controller = new TankController(tank, new PlayerMovementStrategy(), FIELD, obstacles);

        controller.act();

        assertTrue(tank.hasFinishedMovement());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
    }

    @Test
    public void aiStrategyMovesTankWithCommand() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        obstacles.add(tank);
        TankController controller = new TankController(
                tank,
                new FixedDirectionStrategy(Direction.RIGHT),
                FIELD,
                obstacles
        );

        controller.act();

        assertFalse(tank.hasFinishedMovement());
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
    }

    @Test
    public void updateDelegatesToTank() {
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED);
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        obstacles.add(tank);
        TankController controller = new TankController(
                tank,
                new FixedDirectionStrategy(Direction.RIGHT),
                FIELD,
                obstacles
        );

        controller.act();
        controller.update(0.4f);

        assertTrue(tank.hasFinishedMovement());
        assertEquals(new GridPoint2(2, 1), tank.getCoordinates());
    }

    private static class FixedDirectionStrategy implements MovementStrategy {
        private Direction direction;

        private FixedDirectionStrategy(Direction direction) {
            this.direction = direction;
        }

        @Override
        public Direction chooseDirection() {
            return direction;
        }
    }
}
