package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class MoveCommandTest {

    @Test
    public void executeMovesTankInBoundDirection() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        GameField field = new GameField(GameField.WIDTH, GameField.HEIGHT);
        MoveCommand command = new MoveCommand(tank, field, Collections.<Tree>emptyList(), Direction.LEFT);

        command.execute();

        assertEquals(new GridPoint2(0, 1), tank.getDestinationCoordinates());
        assertEquals(-180f, tank.getRotation(), 0.000001f);
    }
}
