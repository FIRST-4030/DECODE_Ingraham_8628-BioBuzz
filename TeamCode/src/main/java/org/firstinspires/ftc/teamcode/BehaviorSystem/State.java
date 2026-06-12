package org.firstinspires.ftc.teamcode.BehaviorSystem;

import java.util.function.Supplier;

/**
 * Interface for all State class. States define stuff that should happen for a given state the robot is
 * in, as well as telling the StateMachine which States should become active and when.
 * @author edsonjames
 */
public interface State {
    /**
     * Executes when this State first becomes active.
     */
    void enter();

    /**
     * Executes every frame this State is active.
     */
    void update();

    /**
     * Should return the next State that should be active in the StateMachine and handle the logic
     * to decide that. If the active State should not change, this method should return its own
     * class.
     * @return The next State that should be active.
     */
    State getNextState();

    /**
     * Executes when this State is first no longer active.
     */
    void exit();
}
