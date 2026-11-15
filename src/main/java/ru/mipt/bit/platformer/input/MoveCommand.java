package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Controllable;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.LevelEntity;
import ru.mipt.bit.platformer.model.LogicalLevel;
import ru.mipt.bit.platformer.model.Tank;

/**
 * Application. Use-case: move a tank.
 */
public class MoveCommand implements Command {

    private Controllable controllable;
    private LogicalLevel level;
    private Direction direction;

    public MoveCommand(Controllable controllable, LogicalLevel level, Direction direction) {
        this.controllable = controllable;
        this.level = level;
        this.direction = direction;
    }

    @Override
    public void execute() {
        if (!isActive()) {
            return;
        }
        controllable.tryMove(direction, level.getBounds(), level.getObstacles());
    }

    private boolean isActive() {
        if (controllable instanceof LevelEntity && !level.contains((LevelEntity) controllable)) {
            return false;
        }
        if (controllable instanceof Tank && ((Tank) controllable).isDestroyed()) {
            return false;
        }
        return true;
    }
}
