package ru.mipt.bit.platformer.input;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.Test;
import ru.mipt.bit.platformer.model.Bullet;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.LevelEntity;
import ru.mipt.bit.platformer.model.LogicalLevel;
import ru.mipt.bit.platformer.model.Tank;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ShootCommandTest {

    @Test
    public void executeMakesTankShoot() {
        LogicalLevel level = new LogicalLevel(new GameField(GameField.WIDTH, GameField.HEIGHT));
        Tank tank = new Tank(new GridPoint2(1, 1), 0.4f, 1, 1, 0.15f);
        level.add(tank);

        new ShootCommand(tank, level).execute();

        boolean hasBullet = false;
        for (LevelEntity entity : level.getEntities()) {
            if (entity instanceof Bullet) {
                hasBullet = true;
                assertEquals(new GridPoint2(2, 1), ((Bullet) entity).getCoordinates());
            }
        }
        assertTrue(hasBullet);
    }
}
