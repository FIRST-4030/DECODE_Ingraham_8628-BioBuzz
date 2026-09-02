package org.firstinspires.ftc.teamcode.BehaviorSystem.Demos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.GroupBuilder;
import org.firstinspires.ftc.teamcode.BehaviorSystem.ParallelGroup;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.BaseState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.TaskState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.GamepadDrive;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.WaitMS;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.WaitUntil;
import org.firstinspires.ftc.teamcode.Blackboard;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;

@TeleOp(name="Behavior System Driving Demo", group="Demos")
public class DrivingDemo extends OpMode {
    ControlHub controlHub;
    Chassis chassis;

    StateMachine mainStateMachine;
    State gamepadDrivingState, turnAroundState;

    @Override
    public void init() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        gamepadDrivingState = new BaseState(
                new GamepadDrive(chassis, gamepad1),
                () -> {
                    if (gamepad1.a) return turnAroundState;
                    return gamepadDrivingState;
                },
                () -> "[A: turn around]"
        );

        turnAroundState = new TaskState(
                GroupBuilder.create()
                        .parallel(ParallelGroup.CompletionCondition.ANY, "Turning around")
                            .add(new WaitMS(3000, "3000 ms"))
                            .add(new WaitUntil(() -> gamepad1.b, "Cancel on B press"))
                            .add(new WaitUntil(() -> false, "Ummm just pretend I'm turning around rn"))
                        .end()
                        .build(),
                () -> gamepadDrivingState,
                () -> "[B: cancel]"
        );

        mainStateMachine = new StateMachine("Main State Machine");
    }

    @Override
    public void init_loop() {
        controlHub.processBotIdentificationTelemetry(telemetry);
        Blackboard.initLoopProcess(telemetry, gamepad1);
        telemetry.update();
    }

    @Override
    public void start() {
        mainStateMachine.setInitialState(gamepadDrivingState);
        mainStateMachine.enter();
    }

    @Override
    public void loop() {
        mainStateMachine.update();
        mainStateMachine.processTelemetry(telemetry, "");

        telemetry.update();
    }
}
