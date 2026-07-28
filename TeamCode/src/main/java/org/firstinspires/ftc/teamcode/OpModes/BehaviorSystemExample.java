package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.BaseState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorBuilder;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behaviors.GamepadDrivingBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behaviors.WaitBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behaviors.WaitForConditionBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.ParallelBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.TaskState;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;

@TeleOp(name="BehaviorBuilder Demo", group="Demos")
public class BehaviorSystemExample extends LinearOpMode {
    ControlHub controlHub;
    Chassis chassis;

    Behavior gamepadDrivingBehavior, complexBehavior;

    StateMachine mainStateMachine;
    State gamepadDrivingState, complexState;

    @Override
    public void runOpMode() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        gamepadDrivingBehavior = new GamepadDrivingBehavior(chassis, gamepad1, "Gamepad driving behavior");
        complexBehavior = BehaviorBuilder.create()
                .sequential("Example sequence:")
                    .add(new WaitForConditionBehavior(
                            () -> (gamepad1.a),
                            "Press A to continue!"
                    ))
                    .add(new WaitForConditionBehavior(
                            () -> (gamepad1.y),
                            "Press Y to continue!"
                    ))
                    .add(new WaitForConditionBehavior(
                            () -> (gamepad1.a),
                            "Press A to continue!"
                    ))
                    .add(new WaitForConditionBehavior(
                            () -> (gamepad1.b),
                            "Press B to continue!"
                    ))
                    .add(new WaitBehavior(10000, "Waiting..."))
                    .parallel(ParallelBehavior.CompletionCondition.FIRST, "Example parallel behaviors:")
                        .add(new WaitBehavior(20000, "wait!"))
                        .add(new WaitBehavior(15000, "wait!!!"))
                        .add(gamepadDrivingBehavior)
                    .end()
                    .parallel(ParallelBehavior.CompletionCondition.ANY, "Different parallel behaviors:")
                        .add(new WaitForConditionBehavior(
                                () -> (gamepad1.a),
                                "Press A to continue!"
                        ))
                        .add(new WaitForConditionBehavior(
                                () -> (gamepad1.b),
                                "Press B to continue!"
                        ))
                    .end()
                    .sequential("Yet another behavior sequence")
                        .add(new WaitForConditionBehavior(
                                () -> (gamepad1.a),
                                "Press A to continue!"
                        ))
                        .add(new WaitForConditionBehavior(
                                () -> (gamepad1.b),
                                "Press B to continue!"
                        ))
                        .add(new WaitBehavior(5000, "4"))
                    .end()
                .add(new WaitForConditionBehavior(
                        () -> (gamepad1.x),
                        "Press X to return to driving"
                ))
                .end()
                .build();

        gamepadDrivingState = new BaseState(gamepadDrivingBehavior, () -> {
            if (gamepad1.dpadUpWasPressed()) {
                return complexState;
            } else {
                return gamepadDrivingState;
            }
        }, () -> ("Press UP on the dpad to switch to the example complex state."));
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
