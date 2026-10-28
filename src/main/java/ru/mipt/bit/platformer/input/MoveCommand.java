package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Controllable;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.LogicalLevel;

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
        controllable.tryMove(direction, level.getBounds(), level.getObstacles());
    }
}
