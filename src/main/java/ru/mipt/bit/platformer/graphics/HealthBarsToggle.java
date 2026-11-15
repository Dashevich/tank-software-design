package ru.mipt.bit.platformer.graphics;

public class HealthBarsToggle {

    private boolean visible;

    public void toggle() {
        visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
