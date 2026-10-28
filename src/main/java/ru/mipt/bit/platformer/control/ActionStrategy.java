package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.input.Command;
import ru.mipt.bit.platformer.model.LogicalLevel;
import ru.mipt.bit.platformer.model.Tank;

public interface ActionStrategy {

    Command createAction(Tank tank, LogicalLevel level);
}
