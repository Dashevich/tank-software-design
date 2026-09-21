package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.List;

public class MoveCommand implements Command {

    private Tank tank;
    private GameField field;
    private List<Tree> trees;
    private Direction direction;

    public MoveCommand(Tank tank, GameField field, List<Tree> trees, Direction direction) {
        this.tank = tank;
        this.field = field;
        this.trees = trees;
        this.direction = direction;
    }

    @Override
    public void execute() {
        tank.tryMove(direction, field, trees);
    }
}
