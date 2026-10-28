package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.input.Command;
import ru.mipt.bit.platformer.input.MoveCommand;
import ru.mipt.bit.platformer.input.ShootCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.LogicalLevel;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TankControllerTest {

    private static final float SPEED = 0.4f;

    @Test
    public void playerStrategyAddsNoCommands() {
        LogicalLevel level = new LogicalLevel(new GameField(GameField.WIDTH, GameField.HEIGHT));
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED, 1, 1, 0.15f);
        level.add(tank);
        TankController controller = new TankController(tank, new PlayerActionStrategy(), level);
        List<Command> commands = new ArrayList<Command>();

        controller.collectAction(commands);

        assertTrue(commands.isEmpty());
    }

    @Test
    public void fixedMoveStrategyCreatesMoveCommand() {
        LogicalLevel level = new LogicalLevel(new GameField(GameField.WIDTH, GameField.HEIGHT));
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED, 1, 1, 0.15f);
        level.add(tank);
        TankController controller = new TankController(
                tank,
                new FixedMoveStrategy(Direction.RIGHT),
                level
        );
        List<Command> commands = new ArrayList<Command>();

        controller.collectAction(commands);
        commands.get(0).execute();

        assertEquals(1, commands.size());
        assertTrue(commands.get(0) instanceof MoveCommand);
        assertFalse(tank.hasFinishedMovement());
        assertEquals(new GridPoint2(2, 1), tank.getDestinationCoordinates());
    }

    @Test
    public void ignoresRemovedTank() {
        LogicalLevel level = new LogicalLevel(new GameField(GameField.WIDTH, GameField.HEIGHT));
        Tank tank = new Tank(new GridPoint2(1, 1), SPEED, 1, 1, 0.15f);
        level.add(tank);
        TankController controller = new TankController(
                tank,
                new FixedMoveStrategy(Direction.RIGHT),
                level
        );
        level.remove(tank);
        List<Command> commands = new ArrayList<Command>();

        controller.collectAction(commands);

        assertTrue(commands.isEmpty());
    }

    private static class FixedMoveStrategy implements ActionStrategy {
        private Direction direction;

        private FixedMoveStrategy(Direction direction) {
            this.direction = direction;
        }

        @Override
        public Command createAction(Tank tank, LogicalLevel level) {
            return new MoveCommand(tank, level, direction);
        }
    }
}
