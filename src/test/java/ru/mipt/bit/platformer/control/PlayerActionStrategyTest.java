package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.LogicalLevel;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.Assert.assertNull;

public class PlayerActionStrategyTest {

    @Test
    public void createsNoAction() {
        assertNull(new PlayerActionStrategy().createAction(
                new Tank(new GridPoint2(1, 1), 0.4f, 1, 1, 0.15f),
                new LogicalLevel(new GameField(GameField.WIDTH, GameField.HEIGHT))
        ));
    }
}
