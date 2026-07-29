package org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

/**
 * States are just Behaviors that have a getNextState() method. They are intended for use in a StateMachine.
 * By default, a state will "complete" when getNextState() does not return the state itself.
 * @author edsonjames
 */
public interface State extends Behavior {
    /**
     * Should return the next State that should be active in the StateMachine and handle the logic
     * to decide that. If the active State should not change, this method should return its own State.
     * @return The next State that should be active.
     */
    State getNextState();

    @Override
    default boolean isComplete() {
        return getNextState() != this;
    }
}
