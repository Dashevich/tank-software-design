package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class KeyboardHandlerTest {

    @Test
    public void executesBoundCommandWhenKeyPressed() {
        FakeKeyboard keyboard = new FakeKeyboard();
        RecordingCommand command = new RecordingCommand();
        KeyboardHandler handler = new KeyboardHandler(keyboard);
        handler.bind(command, Input.Keys.W, Input.Keys.UP);

        keyboard.press(Input.Keys.W);
        handler.handle();

        assertEquals(1, command.calls);
    }

    @Test
    public void doesNotExecuteCommandWhenKeyIsNotPressed() {
        FakeKeyboard keyboard = new FakeKeyboard();
        RecordingCommand command = new RecordingCommand();
        KeyboardHandler handler = new KeyboardHandler(keyboard);
        handler.bind(command, Input.Keys.SPACE);

        handler.handle();

        assertEquals(0, command.calls);
    }

    @Test
    public void canBindAdditionalCommandsWithoutChangingExistingOnes() {
        FakeKeyboard keyboard = new FakeKeyboard();
        RecordingCommand moveCommand = new RecordingCommand();
        RecordingCommand shootCommand = new RecordingCommand();
        KeyboardHandler handler = new KeyboardHandler(keyboard);
        handler.bind(moveCommand, Input.Keys.W);
        handler.bind(shootCommand, Input.Keys.SPACE);

        keyboard.press(Input.Keys.SPACE);
        handler.handle();

        assertEquals(0, moveCommand.calls);
        assertEquals(1, shootCommand.calls);
    }

    @Test
    public void movesTankOnArrowKey() {
        FakeKeyboard keyboard = new FakeKeyboard();
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f);
        KeyboardHandler handler = new KeyboardHandler(keyboard);
        handler.bind(new MoveCommand(tank, new GameField(GameField.WIDTH, GameField.HEIGHT), Collections.<Obstacle>emptyList(), Direction.DOWN), Input.Keys.DOWN, Input.Keys.S);

        keyboard.press(Input.Keys.DOWN);
        handler.handle();

        assertEquals(new GridPoint2(1, 0), tank.getDestinationCoordinates());
        assertFalse(tank.hasFinishedMovement());
        assertEquals(-90f, tank.getRotation(), 0.000001f);
    }

    private static class RecordingCommand implements Command {
        private int calls;

        @Override
        public void execute() {
            calls++;
        }
    }
}
