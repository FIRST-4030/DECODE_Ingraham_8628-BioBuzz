package org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;
import org.firstinspires.ftc.teamcode.Chassis;

public class RealTimeBehavior implements Behavior {
    private final Chassis chassis;
    private final Gamepad gamepad1;

    public RealTimeBehavior(Chassis chassis, Gamepad gamepad1) {
        this.chassis = chassis;
        this.gamepad1 = gamepad1;
    }

    @Override
    public void enter() {

    }

    @Override
    public void update() {
        chassis.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void exit() {

    }
}
