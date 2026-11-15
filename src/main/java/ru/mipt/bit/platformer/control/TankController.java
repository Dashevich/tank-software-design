package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.input.Command;
import ru.mipt.bit.platformer.model.LogicalLevel;
import ru.mipt.bit.platformer.model.Tank;

import java.util.List;

public class TankController {

    private Tank tank;
    private ActionStrategy strategy;
    private LogicalLevel level;

    public TankController(Tank tank, ActionStrategy strategy, LogicalLevel level) {
        this.tank = tank;
        this.strategy = strategy;
        this.level = level;
    }

    public Tank getTank() {
        return tank;
    }

    public void collectAction(List<Command> commands) {
        if (!level.contains(tank)) {
            return;
        }
        Command command = strategy.createAction(tank, level);
        if (command != null) {
            commands.add(command);
        }
    }
}
