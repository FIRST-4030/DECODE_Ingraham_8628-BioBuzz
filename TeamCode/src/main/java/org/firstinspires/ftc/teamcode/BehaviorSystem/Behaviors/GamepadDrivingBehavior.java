package org.firstinspires.ftc.teamcode.BehaviorSystem.Behaviors;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;
import org.firstinspires.ftc.teamcode.Chassis;

/**
 * Behavior that drives the robot's driving motors using a gamepad. Never completes.
 */
public class GamepadDrivingBehavior implements Behavior {
    private final Chassis chassis;
    private final Gamepad gamepad;
    private final String label;

    public GamepadDrivingBehavior(Chassis chassis, Gamepad gamepad) {
        this(chassis, gamepad, "GamepadDrivingBehavior");
    }

    public GamepadDrivingBehavior(Chassis chassis, Gamepad gamepad1, String label) {
        this.chassis = chassis;
        this.gamepad = gamepad1;
        this.label = label;
    }

    /**
     * Turns breaking on for the driving motors.
     */
    @Override
    public void enter() {
        chassis.resetZeroPowerBehavior();
    }

    /**
     * Drives the robot using gamepad 1's left stick and right stick
     */
    @Override
    public void update() {
        chassis.drive(-gamepad.left_stick_y, gamepad.left_stick_x, gamepad.right_stick_x);
    }

    /**
     * Always returns false; this Behavior does not have a "completed" state.
     * @return false.
     */
    @Override
    public boolean isComplete() {
        return false;
    }

    /**
     * Stops driving motors.
     */
    @Override
    public void exit() {
        chassis.stopMotors();
    }

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        telemetry.addLine(prefix + "(Gamepad driving...)");
    }

    @Override
    public String getLabel() {
        return label;
    }
}
