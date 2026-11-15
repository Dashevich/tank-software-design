package ru.mipt.bit.platformer.model;

/**
 * Domain. Reacts to a bullet hit.
 */
public interface CollidesWithBullet {

    void onHitBy(Bullet bullet, LogicalLevel level);
}
