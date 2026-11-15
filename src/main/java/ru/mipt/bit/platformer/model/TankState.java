package ru.mipt.bit.platformer.model;

/**
 * Domain. Tank state (State pattern).
 */
public interface TankState {

    float movementDurationFactor();

    boolean canShoot();
}
