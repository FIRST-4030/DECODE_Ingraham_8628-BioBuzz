package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Interface for all Behavior classes. Behaviors are collections of logic organized in a
 * state-machine-like manner with enter(), update(), and exit() methods.
 * @author edsonjames
 */
public  interface Behavior {
    /**
     * Initializes the Behavior.
     */
    void enter();

    /**
     * The update logic of the Behavior.
     */
    void update();

    /**
     * Returns whether the Behavior is "complete." If a Behavior should not have an "end" state, this method
     * should always return 'false'.
     * @return Whether the Behavior is complete.
     */
    boolean isComplete();

    /**
     * The exit logic of the Behavior.
     */
    void exit();

    /**
     * Adds lines of information about the behavior to telemetry. Prefix is used for indentation,
     * where the convention is "    " (four spaces) for each indent.
     * @param telemetry The Telemetry instance to print to
     * @param prefix Characters to add to the beginning of each line printed
     */
    void processTelemetry(Telemetry telemetry, String prefix);

    String getLabel();
}
