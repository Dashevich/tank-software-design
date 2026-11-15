package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.graphics.HealthBarsToggle;

public class ToggleHealthBarsCommand implements Command {

    private HealthBarsToggle toggle;

    public ToggleHealthBarsCommand(HealthBarsToggle toggle) {
        this.toggle = toggle;
    }

    @Override
    public void execute() {
        toggle.toggle();
    }
}
