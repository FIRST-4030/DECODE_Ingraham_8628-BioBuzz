package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.BaseState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorBuilder;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.GamepadDrivingBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.WaitBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.WaitForConditionBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.TaskState;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;

@TeleOp(name="Behavior System Messing Around", group="Demos")
public class BehaviorSystemMessingAround extends LinearOpMode {
    ControlHub controlHub;
    Chassis chassis;

    Behavior waitForAPressBehavior;
    Behavior gamepadDrivingBehavior, complexBehavior;

    StateMachine mainStateMachine;
    State gamepadDrivingState, complexState;

    @Override
    public void runOpMode() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        waitForAPressBehavior = new WaitForConditionBehavior(() -> (gamepad1.a), "Wait until A pressed");
        gamepadDrivingBehavior = new GamepadDrivingBehavior(chassis, gamepad1, "Gamepad driving behavior");
        complexBehavior = BehaviorBuilder.create()
                .sequential("Example sequence")
                    .add(new WaitBehavior(2000, "Wait 2 sec"))
                    .add(waitForAPressBehavior)
                .end()
                .build();

        gamepadDrivingState = new BaseState(gamepadDrivingBehavior, () -> {
            if (gamepad1.dpadUpWasPressed()) {
                return complexState;
            } else {
                return gamepadDrivingState;
            }
        });
        complexState = new TaskState(complexBehavior, () -> gamepadDrivingState);

        mainStateMachine = new StateMachine("Main State Machine");
        mainStateMachine.setState(gamepadDrivingState);

        telemetry.addLine("BehaviorSystem example initialized");
        telemetry.update();

        mainStateMachine.enter();

        waitForStart();

        while (opModeIsActive()) {
            mainStateMachine.update();
            mainStateMachine.processTelemetry(telemetry, "");

            telemetry.update();
        }
    }
}
