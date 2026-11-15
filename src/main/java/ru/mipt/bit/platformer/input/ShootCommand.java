package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.LevelEntity;
import ru.mipt.bit.platformer.model.LogicalLevel;
import ru.mipt.bit.platformer.model.Shootable;
import ru.mipt.bit.platformer.model.Tank;

/**
 * Application. Use-case: shoot.
 */
public class ShootCommand implements Command {

    private Shootable shootable;
    private LogicalLevel level;

    public ShootCommand(Shootable shootable, LogicalLevel level) {
        this.shootable = shootable;
        this.level = level;
    }

    @Override
    public void execute() {
        if (!isActive()) {
            return;
        }
        shootable.shoot(level);
    }

    private boolean isActive() {
        if (shootable instanceof LevelEntity && !level.contains((LevelEntity) shootable)) {
            return false;
        }
        if (shootable instanceof Tank && ((Tank) shootable).isDestroyed()) {
            return false;
        }
        return true;
    }
}
