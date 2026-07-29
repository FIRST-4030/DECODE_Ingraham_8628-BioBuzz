package org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

/**
 * States are just behaviors that have a getNextState() method. They are intended for use in a StateMachine.
 * By default, a state will "complete" when getNextState() does not return the state itself. However,
 * there aren't really many cases where a state's "completion" status would be useful, as the state
 * itself defines what the next state to switch to should be.
 * @author Edson James
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
