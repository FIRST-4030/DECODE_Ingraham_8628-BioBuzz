package org.firstinspires.ftc.teamcode.BehaviorSystem.Examples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.BaseState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.GamepadDrivingBehavior;
import org.firstinspires.ftc.teamcode.Blackboard;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;

@TeleOp(name="Basic OpMode w/ the Behavior System", group="Demos")
public class BasicOpMode extends OpMode {
    ControlHub controlHub;
    Chassis chassis;

    Behavior gamepadDrivingBehavior;

    StateMachine mainStateMachine;
    State gamepadDrivingState;

    @Override
    public void init() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        gamepadDrivingBehavior = new GamepadDrivingBehavior(chassis, gamepad1);
        gamepadDrivingState = new BaseState(
                gamepadDrivingBehavior,
                () -> (gamepadDrivingState)
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
        mainStateMachine.setState(gamepadDrivingState);
        mainStateMachine.enter();
    }

    @Override
    public void loop() {
        mainStateMachine.update();
        mainStateMachine.processTelemetry(telemetry, "");

        telemetry.update();
    }
}
