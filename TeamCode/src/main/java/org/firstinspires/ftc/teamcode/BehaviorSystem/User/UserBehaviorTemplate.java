package org.firstinspires.ftc.teamcode.BehaviorSystem.User;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

import java.util.Objects;

/**
 * Template for easily creating new behaviors. You can duplicate this class, rename it, and modify it
 * to your liking.
 * @author Edson James
 */
public class UserBehaviorTemplate implements Behavior {

    // Custom label variable so users can label each instance of the behavior differently.
    private final String label;

    // You want this first constructor to take everything the full constructor takes except for the label.
    // Then, this constructor can just call your full constructor with your hard-coded default label.
    public UserBehaviorTemplate() {
        this("User behavior");
    }

    // Full constructor.
    public UserBehaviorTemplate(String label) {
        this.label = label;
    }

    // enter() is called once when the behavior starts. It is where you store all the logic for when
    // your behavior first becomes "active."
    @Override
    public void enter() {

    }

    // update() is called repeatedly in the main loop when the behavior is active. It is where you
    // store all the logic for what your behavior should do every frame.
    @Override
    public void update() {

    }

    // isComplete() returns a boolean (true or false) value. Some behaviors, such as a timer, have well-defined
    // "completed" states. Other behaviors, such as a gamepad driving behavior, don't really have "completed" states
    // and thus should always return false here.
    @Override
    public boolean isComplete() {
        return false;
    }

    // exit() is where you store all the logic for when your behavior "ends." Cleaning up. For example,
    // GamepadDrivingBehavior stops all driving motors when exiting.
    @Override
    public void exit() {

    }

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        // ALWAYS make sure to add prefix to the beginning of new lines you print to telemetry.
        // The prefix parameter is used to nest behavior telemetry messages on the screen using
        // indentation, so it makes things look broken if you forget to add it to the beginning.
        telemetry.addLine(prefix + "(Template telemetry)");
    }

    // Probably don't touch this; this method is what allows users to only optionally specify a custom
    // label for instances of this behavior.
    @Override
    public String getLabel() {
        return label;
    }
}
