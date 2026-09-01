package org.firstinspires.ftc.teamcode.BehaviorSystem.Demos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorBuilder;
import org.firstinspires.ftc.teamcode.BehaviorSystem.ParallelBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.BaseState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.InterruptableTaskState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.TaskState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.GamepadDrive;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.InstantBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.WaitMS;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.WaitUntil;
import org.firstinspires.ftc.teamcode.Blackboard;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;

/**
 * Demo OpMode pretending to simulate some states that may have appeared during the DECODE season.
 * @author Edson James
 */
@TeleOp(name="Behavior System Complex Demo", group="Demos")
public class ComplexDemo extends OpMode {
    ControlHub controlHub;
    Chassis chassis;

    StateMachine mainStateMachine;
    State drivingState, releaseGateState, farShootState, nearShootState, parkState;

    StateMachine drivingStateMachine;
    State gamepadDrivingState, turnAroundState;

    @Override
    public void init() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        // -----------------------------------------------------------------------------------------
        // Driving State Machine
        // -----------------------------------------------------------------------------------------

        drivingStateMachine = new StateMachine("Driving State Machine");

        gamepadDrivingState = new BaseState(
                new GamepadDrive(chassis, gamepad1),
                () -> {
                    if (gamepad1.a) return turnAroundState;
                    return gamepadDrivingState;
                },
                () -> "[A: turn around]"
        );

        turnAroundState = new TaskState(
                BehaviorBuilder.create()
                        .parallel(ParallelBehavior.CompletionCondition.FIRST_IN_LIST, "Turning around")
                            .add(new WaitMS(3000, "3000 ms"))
                            .add(new WaitUntil(() -> false, "Ummm just pretend I'm turning around rn"))
                        .end()
                        .build(),
                () -> gamepadDrivingState
        );

        // -----------------------------------------------------------------------------------------
        // Main State Machine
        // -----------------------------------------------------------------------------------------

        mainStateMachine = new StateMachine("Main State Machine");

        drivingState = new BaseState(
                drivingStateMachine,
                () -> {
                    if (gamepad1.dpad_up) return nearShootState;
                    if (gamepad1.dpad_down) return farShootState;
                    if (gamepad1.dpad_right) return releaseGateState;
                    if (gamepad1.b) return parkState;
                    return drivingState;
                },
                () -> "[UP: near, DOWN: far, RIGHT: gate, B: park]");

        releaseGateState = new InterruptableTaskState(
                BehaviorBuilder.create()
                        .sequential("Release Gate")
                            .add(new WaitMS(3000, "Go to gate"))
                            .add(new WaitMS(2000, "Release"))
                        .end()
                        .build(),
                () -> drivingState,
                () -> {
                    if (gamepad1.dpad_left) return drivingState;
                    return releaseGateState;
                },
                () -> "[LEFT: interrupt -> driving]"
        );

        farShootState = new InterruptableTaskState(
                BehaviorBuilder.create()
                        .sequential("Far Shoot")
                            .parallel(ParallelBehavior.CompletionCondition.ALL, "Go to far + turning on shooter")
                                .add(new WaitMS(3000, "Go to far shoot"))
                                .add(new WaitUntil(() -> gamepad1.y, "Turning on shooter (press Y)"))
                            .end()
                            .parallel(ParallelBehavior.CompletionCondition.FIRST_IN_LIST, "Shooting + aiming")
                                .sequential("Shooting")
                                    .add(new WaitUntil(() -> gamepad1.right_bumper, "Shooting (press RB)"))
                                    .add(new WaitUntil(() -> gamepad1.left_bumper, "Shooting (press LB)"))
                                    .add(new WaitUntil(() -> gamepad1.right_bumper, "Shooting (press RB)"))
                                .end()
                                .add(new WaitMS(100000, "Continuous aiming"))
                            .end()
                            .add(new WaitMS(1000, "Turning off shooter"))
                        .end()
                        .build(),
                () -> drivingState,
                () -> {
                    if (gamepad1.dpad_left) return drivingState;
                    return farShootState;
                },
                () -> "[LEFT: interrupt -> driving]"
        );

        nearShootState = new InterruptableTaskState(
                BehaviorBuilder.create()
                        .sequential("Near Shoot")
                            .parallel(ParallelBehavior.CompletionCondition.ALL, "Go to near + turning on shooter")
                                .add(new WaitMS(3000, "Go to near shoot"))
                                .add(new WaitUntil(() -> gamepad1.y, "Turning on shooter (press Y)"))
                            .end()
                            .parallel(ParallelBehavior.CompletionCondition.FIRST_IN_LIST, "Shooting + aiming")
                                .sequential("Shooting")
                                    .add(new WaitUntil(() -> gamepad1.right_bumper, "Shooting (press RB)"))
                                    .add(new WaitUntil(() -> gamepad1.left_bumper, "Shooting (press LB)"))
                                    .add(new WaitUntil(() -> gamepad1.right_bumper, "Shooting (press RB)"))
                                .end()
                                .parallel(ParallelBehavior.CompletionCondition.NEVER, "Continuous aiming")
                                    .add(new WaitMS(100000, "Aiming things"))
                                    .add(new GamepadDrive(chassis, gamepad1, "More aiming things"))
                                .end()
                            .end()
                            .add(new WaitMS(1000, "Turning off shooter"))
                        .end()
                        .build(),
                () -> drivingState,
                () -> {
                    if (gamepad1.dpad_left) return drivingState;
                    return nearShootState;
                },
                () -> "[LEFT: interrupt -> driving]"
        );

        parkState = new BaseState(
                new GamepadDrive(chassis, gamepad1, "Driving in special pArKiNg mOdE"),
                () -> {
                    if (gamepad1.dpad_left) return drivingState;
                    return parkState;
                },
                () -> "[LEFT: driving]"
        );
    }

    @Override
    public void init_loop() {
        controlHub.processBotIdentificationTelemetry(telemetry);
        Blackboard.initLoopProcess(telemetry, gamepad1);
        telemetry.update();
    }

    @Override
    public void start() {
        drivingStateMachine.setInitialState(gamepadDrivingState);
        drivingStateMachine.enter();

        mainStateMachine.setInitialState(drivingState);
        mainStateMachine.enter();
    }

    @Override
    public void loop() {
        mainStateMachine.update();
        mainStateMachine.processTelemetry(telemetry, "");

        telemetry.update();
    }
}
