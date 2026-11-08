package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Bounds;
import ru.mipt.bit.platformer.model.Controllable;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Obstacle;

import java.util.List;

public class MoveCommand implements Command {

    private Controllable controllable;
    private Bounds bounds;
    private List<Obstacle> obstacles;
    private Direction direction;

    public MoveCommand(Controllable controllable, Bounds bounds, List<Obstacle> obstacles, Direction direction) {
        this.controllable = controllable;
        this.bounds = bounds;
        this.obstacles = obstacles;
        this.direction = direction;
    }

    @Override
    public void execute() {
        controllable.tryMove(direction, bounds, obstacles);
    }
}
