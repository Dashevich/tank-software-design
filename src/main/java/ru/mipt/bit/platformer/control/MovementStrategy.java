package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;

public interface MovementStrategy {

    Direction chooseDirection();
}
