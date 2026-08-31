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
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.WaitForConditionBehavior;
import org.firstinspires.ftc.teamcode.Blackboard;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;

//@Disabled
@TeleOp(name="Behavior System Complex Demo", group="Demos")
public class BasicSequenceDemo extends OpMode {
    ControlHub controlHub;
    Chassis chassis;

    Behavior waitForAPressBehavior;
    Behavior gamepadDrivingBehavior, complexBehavior, nestedComplexBehavior;

    StateMachine mainStateMachine;
    State gamepadDrivingState, taskState;

    @Override
    public void init() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        waitForAPressBehavior = new WaitForConditionBehavior(() -> (gamepad1.a), "Wait until A pressed");
        gamepadDrivingBehavior = new GamepadDrivingBehavior(chassis, gamepad1, "Gamepad driving behavior");
        complexBehavior = BehaviorBuilder.create()
                .sequential("Example sequence")
                    .add(new TimerBehavior(2000, "Wait 2 sec"))
                    .parallel(ParallelBehavior.CompletionCondition.FIRST_IN_LIST, "Drive while waiting for A button")
                        .add(waitForAPressBehavior)
                        .add(gamepadDrivingBehavior)
                    .end()
                .end()
                .build();

        nestedComplexBehavior = BehaviorBuilder.create()
                .sequential("Nested Complex Behavior")
                    .add(complexBehavior)
                    .add(new TimerBehavior(2000))
                .end()
                .build();

        gamepadDrivingState = new BaseState(
                gamepadDrivingBehavior,
                () -> {
                    if (gamepad1.dpadUpWasPressed()) {
                        return taskState;
                    } else {
                        return gamepadDrivingState;
                    }
                },
                () -> ("[Press UP on the dpad to enter the complex state.]")
        );
        taskState = new TaskState(nestedComplexBehavior, () -> gamepadDrivingState);

        mainStateMachine = new StateMachine("Main State Machine");
        mainStateMachine.setState(gamepadDrivingState);
        mainStateMachine.enter();
    }

    @Override
    public void init_loop() {
        controlHub.processBotIdentificationTelemetry(telemetry);
        Blackboard.initLoopProcess(telemetry, gamepad1);
        telemetry.update();
    }

    @Override
    public void loop() {
        mainStateMachine.update();
        mainStateMachine.processTelemetry(telemetry, "");

        telemetry.update();
    }
}
