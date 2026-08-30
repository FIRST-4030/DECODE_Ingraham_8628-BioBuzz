package org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

/**
 * States are just behaviors that have a getNextState() method. They are intended for use in a StateMachine.
 * @author Edson James
 */
public interface State extends Behavior {
    /**
     * Should return the next State that should be active in the StateMachine and handle the logic
     * to decide that. If the active State should not change, this method should return its own State.
     * This method should NOT have side effects.
     * @return The next State that should be active.
     */
    State getNextState();

    /**
     * By default, a state will "complete" when getNextState() does not return the state itself. However,
     * there aren't really many cases where a state's "completion" status will be useful, because
     * StateMachine handles the switching of states without using the isComplete() method.
     * @return Whether the state is complete.
     */
    @Override
    default boolean isComplete() {
        return getNextState() != this;
    }
}
