package org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;
import org.firstinspires.ftc.teamcode.Blackboard;

public class BlackboardInitLoopBehavior implements Behavior {
    Telemetry telemetry;
    Gamepad gamepad1;

    public BlackboardInitLoopBehavior(Telemetry telemetry, Gamepad gamepad1) {
        this.telemetry = telemetry;
        this.gamepad1 = gamepad1;
    }

    @Override
    public void enter() {

    }

    @Override
    public void update() {
        telemetry.addLine("--- BLACKBOARD ---");
        telemetry.addData("Alliance", Blackboard.getAllianceAsString());
        telemetry.addLine("^^^ RB + A: Blue,  RB + B: Red ^^^");
        telemetry.addLine();

        if (gamepad1.right_bumper) {
            if (gamepad1.aWasPressed()) {
                Blackboard.setAlliance(Blackboard.Alliance.BLUE);
            } else if (gamepad1.bWasPressed()) {
                Blackboard.setAlliance(Blackboard.Alliance.RED);
            }
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void exit() {

    }
}
