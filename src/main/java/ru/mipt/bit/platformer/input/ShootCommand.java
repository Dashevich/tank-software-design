package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.LogicalLevel;
import ru.mipt.bit.platformer.model.Shootable;

public class ShootCommand implements Command {

    private Shootable shootable;
    private LogicalLevel level;

    public ShootCommand(Shootable shootable, LogicalLevel level) {
        this.shootable = shootable;
        this.level = level;
    }

    @Override
    public void execute() {
        shootable.shoot(level);
    }
}
