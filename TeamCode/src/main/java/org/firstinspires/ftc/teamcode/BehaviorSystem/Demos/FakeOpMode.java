package org.firstinspires.ftc.teamcode.BehaviorSystem.Demos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorBuilder;
import org.firstinspires.ftc.teamcode.BehaviorSystem.ParallelBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.BaseState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.InterruptableTaskState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.GamepadDrivingBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.TimerBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.WaitForConditionBehavior;
import org.firstinspires.ftc.teamcode.Blackboard;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;

@TeleOp(name="Fake OpMode", group="Demos")
public class FakeOpMode extends OpMode {
    ControlHub controlHub;
    Chassis chassis;

    State drivingState, releaseGateState, farShootState, nearShootState, parkState;
    StateMachine mainStateMachine;

    @Override
    public void init() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        drivingState = new BaseState(
                new GamepadDrivingBehavior(chassis, gamepad1),
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
                            .add(new TimerBehavior(3000, "Go to gate"))
                            .add(new TimerBehavior(2000, "Release"))
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
                            .parallel(ParallelBehavior.CompletionCondition.ALL)
                                .add(new TimerBehavior(3000, "Go to far shoot"))
                                .add(new WaitForConditionBehavior(() -> gamepad1.y, "Turning on shooter (press Y)"))
                            .end()
                            .parallel(ParallelBehavior.CompletionCondition.FIRST_IN_LIST, "Shooting + aiming")
                                .sequential()
                                    .add(new WaitForConditionBehavior(() -> gamepad1.right_bumper, "Shooting (press RB)"))
                                    .add(new WaitForConditionBehavior(() -> gamepad1.left_bumper, "Shooting (press LB)"))
                                    .add(new WaitForConditionBehavior(() -> gamepad1.right_bumper, "Shooting (press RB)"))
                                .end()
                                .add(new TimerBehavior(100000, "Continuous aiming"))
                            .end()
                            .add(new TimerBehavior(1000, "Turning off shooter"))
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
                            .parallel(ParallelBehavior.CompletionCondition.ALL)
                                .add(new TimerBehavior(3000, "Go to near shoot"))
                                .add(new WaitForConditionBehavior(() -> gamepad1.y, "Turning on shooter (press Y)"))
                            .end()
                            .parallel(ParallelBehavior.CompletionCondition.FIRST_IN_LIST, "Shooting + aiming")
                                .sequential()
                                    .add(new WaitForConditionBehavior(() -> gamepad1.right_bumper, "Shooting (press RB)"))
                                    .add(new WaitForConditionBehavior(() -> gamepad1.left_bumper, "Shooting (press LB)"))
                                    .add(new WaitForConditionBehavior(() -> gamepad1.right_bumper, "Shooting (press RB)"))
                                .end()
                                .parallel(ParallelBehavior.CompletionCondition.NEVER, "Continuous aiming")
                                    .add(new TimerBehavior(100000, "Aiming things"))
                                    .add(new GamepadDrivingBehavior(chassis, gamepad1, "More aiming things"))
                                .end()
                            .end()
                            .add(new TimerBehavior(1000, "Turning off shooter"))
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
                new GamepadDrivingBehavior(chassis, gamepad1, "Driving in special pArKiNg mOdE"),
                () -> {
                    if (gamepad1.dpad_left) return drivingState;
                    return parkState;
                },
                () -> "[LEFT: driving]"
        );

        mainStateMachine = new StateMachine("Main State Machine");

        telemetry.setMsTransmissionInterval(50);
    }

    @Override
    public void init_loop() {
        controlHub.processBotIdentificationTelemetry(telemetry);
        Blackboard.initLoopProcess(telemetry, gamepad1);
        telemetry.update();
    }

    @Override
    public void start() {
        mainStateMachine.setState(drivingState);
        mainStateMachine.enter();
    }

    @Override
    public void loop() {
        mainStateMachine.update();
        mainStateMachine.processTelemetry(telemetry, "");

        telemetry.update();
    }
}
