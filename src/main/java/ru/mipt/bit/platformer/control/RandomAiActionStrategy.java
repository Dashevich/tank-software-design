package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.input.Command;
import ru.mipt.bit.platformer.input.MoveCommand;
import ru.mipt.bit.platformer.input.ShootCommand;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.LogicalLevel;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Random;

public class RandomAiActionStrategy implements ActionStrategy {

    private static final Direction[] DIRECTIONS = Direction.values();

    private Random random;
    private float shootChance;

    public RandomAiActionStrategy(Random random, float shootChance) {
        this.random = random;
        this.shootChance = shootChance;
    }

    @Override
    public Command createAction(Tank tank, LogicalLevel level) {
        if (random.nextFloat() < shootChance) {
            return new ShootCommand(tank, level);
        }
        Direction direction = DIRECTIONS[random.nextInt(DIRECTIONS.length)];
        return new MoveCommand(tank, level, direction);
    }
}
