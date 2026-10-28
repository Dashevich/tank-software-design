package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.input.MoveCommand;
import ru.mipt.bit.platformer.model.Bounds;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Obstacle;
import ru.mipt.bit.platformer.model.Tank;

import java.util.List;

public class TankController {

    private Tank tank;
    private MovementStrategy strategy;
    private Bounds bounds;
    private List<Obstacle> obstacles;

    public TankController(Tank tank, MovementStrategy strategy, Bounds bounds, List<Obstacle> obstacles) {
        this.tank = tank;
        this.strategy = strategy;
        this.bounds = bounds;
        this.obstacles = obstacles;
    }

    public Tank getTank() {
        return tank;
    }

    public void act() {
        Direction direction = strategy.chooseDirection();
        if (direction != null) {
            new MoveCommand(tank, bounds, obstacles, direction).execute();
        }
    }

    public void update(float deltaTime) {
        tank.update(deltaTime);
    }
}
