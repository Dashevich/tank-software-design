package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.LogicalLevel;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.Assert.assertEquals;

public class MoveCommandTest {

    @Test
    public void executeMovesTankInBoundDirection() {
        LogicalLevel level = new LogicalLevel(new GameField(GameField.WIDTH, GameField.HEIGHT));
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f, 1, 1, 0.15f);
        level.add(tank);
        MoveCommand command = new MoveCommand(tank, level, Direction.LEFT);

        command.execute();

        assertEquals(new GridPoint2(0, 1), tank.getDestinationCoordinates());
        assertEquals(Direction.LEFT, tank.getOrientation());
    }
}
