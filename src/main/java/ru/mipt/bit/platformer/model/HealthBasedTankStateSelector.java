package ru.mipt.bit.platformer.model;

/**
 * Domain. Picks state by HP percent.
 */
public final class HealthBasedTankStateSelector {

    private static final float LIGHT_RATIO = 0.7f;
    private static final float HEAVY_RATIO = 0.15f;

    private HealthBasedTankStateSelector() {
    }

    public static TankState select(float healthRatio) {
        if (healthRatio > LIGHT_RATIO) {
            return LightTankState.INSTANCE;
        }
        if (healthRatio >= HEAVY_RATIO) {
            return MediumTankState.INSTANCE;
        }
        return HeavyTankState.INSTANCE;
    }
}
