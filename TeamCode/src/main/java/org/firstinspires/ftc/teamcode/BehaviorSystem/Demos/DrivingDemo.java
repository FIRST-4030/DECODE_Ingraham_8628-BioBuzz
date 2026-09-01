package org.firstinspires.ftc.teamcode.BehaviorSystem.Demos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorBuilder;
import org.firstinspires.ftc.teamcode.BehaviorSystem.ParallelBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.BaseState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.TaskState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.GamepadDrivingBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.TimerBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.TurnLeftForeverBehavior;
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
                new GamepadDrivingBehavior(chassis, gamepad1),
                () -> {
                    if (gamepad1.a) return turnAroundState;
                    return gamepadDrivingState;
                },
                () -> "[A: turn around]"
        );

        turnAroundState = new TaskState(
                BehaviorBuilder.create()
                        .parallel(ParallelBehavior.CompletionCondition.FIRST_IN_LIST, "Turn left for 750 ms")
                            .add(new TimerBehavior(750, "750 ms"))
                            .add(new TurnLeftForeverBehavior(chassis))
                        .end()
                        .build(),
                () -> gamepadDrivingState
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
