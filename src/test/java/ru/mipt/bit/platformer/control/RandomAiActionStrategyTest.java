package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.input.Command;
import ru.mipt.bit.platformer.input.MoveCommand;
import ru.mipt.bit.platformer.input.ShootCommand;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.LogicalLevel;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Random;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RandomAiActionStrategyTest {

    @Test
    public void canCreateMoveCommands() {
        RandomAiActionStrategy strategy = new RandomAiActionStrategy(new Random(1L), 0f);
        LogicalLevel level = new LogicalLevel(new GameField(GameField.WIDTH, GameField.HEIGHT));
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f, 1, 1, 0.15f);

        Command command = strategy.createAction(tank, level);

        assertTrue(command instanceof MoveCommand);
    }

    @Test
    public void canCreateShootCommands() {
        RandomAiActionStrategy strategy = new RandomAiActionStrategy(new Random(1L), 1f);
        LogicalLevel level = new LogicalLevel(new GameField(GameField.WIDTH, GameField.HEIGHT));
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f, 1, 1, 0.15f);

        Command command = strategy.createAction(tank, level);

        assertTrue(command instanceof ShootCommand);
    }

    @Test
    public void alwaysReturnsSomeCommand() {
        RandomAiActionStrategy strategy = new RandomAiActionStrategy(new Random(7L), 0.5f);
        LogicalLevel level = new LogicalLevel(new GameField(GameField.WIDTH, GameField.HEIGHT));
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f, 1, 1, 0.15f);

        for (int i = 0; i < 20; i++) {
            assertNotNull(strategy.createAction(tank, level));
        }
    }
}
