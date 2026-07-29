package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Interface for all behaviors. Behaviors are collections of logic organized in a
 * state-machine-like manner with enter(), update(), exit(), and isComplete() methods.
 * @see org.firstinspires.ftc.teamcode.BehaviorSystem.User.UserBehaviorTemplate
 * @author Edson James
 */
public interface Behavior {
    /**
     * The initialization logic of the behavior.
     */
    void enter();

    /**
     * The update logic of the behavior.
     */
    void update();

    /**
     * Returns whether the behavior is "complete." If a behavior should not have an "end" state, this method
     * should always return false.
     * @return Whether the behavior is complete.
     */
    boolean isComplete();

    /**
     * The exit logic of the behavior.
     */
    void exit();

    /**
     * Adds lines of information about the behavior to telemetry. "prefix" is used for indentation,
     * where the convention is "    " (four spaces) for each indent. This allows for nice looking
     * nesting of behaviors in telemetry.
     * @param telemetry The telemetry instance to print to
     * @param prefix Characters to add to the beginning of each line printed
     */
    void processTelemetry(Telemetry telemetry, String prefix);

    /**
     * Returns a short descriptor/title for the behavior. Used in telemetry.
     * @return The title of the behavior.
     */
    String getLabel();
}
