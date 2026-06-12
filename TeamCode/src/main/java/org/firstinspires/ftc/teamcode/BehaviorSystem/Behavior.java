package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Interface for all Behavior classes. Behaviors are collections of logic organized in a
 * state-machine-like manner with enter(), update(), and exit() methods. Behavior is optimized to be
 * used in BehaviorSteps and States.
 * @author edsonjames
 */
public  interface Behavior {
    /**
     * Initializes the Behavior. Executes when the BehaviorStep this Behavior is attached to first
     * becomes active.
     */
    void enter();

    /**
     * The update logic of the Behavior. Executes every frame that the BehaviorStep this Behavior is
     * attached to is active.
     */
    void update();

    /**
     * Returns whether the Behavior is "complete." Used in BehaviorStepSequencePerformer to decide when to
     * move to the next BehaviorStep. If a Behavior should not have an "end" state, this method
     * should always return 'false'.
     * @return Whether the Behavior is complete.
     */
    boolean isComplete();

    /**
     * The exit logic of the Behavior. Executes when the BehaviorStep this Behavior is attached to
     * is no longer active.
     */
    void exit();

    /**
     * Adds lines of information about the StateMachine to a Telemetry instance, including the
     * active State's simple class name.
     * @param telemetry The Telemetry instance to print to
     * @param prefix Characters to add to the beginning of each line printed
     */
    void processTelemetry(Telemetry telemetry, String prefix);
}
