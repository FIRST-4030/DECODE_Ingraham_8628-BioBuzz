package org.firstinspires.ftc.teamcode.OpModes;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.BaseState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.GamepadDrivingBehavior;
import org.firstinspires.ftc.teamcode.Blackboard;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;
import org.firstinspires.ftc.teamcode.Pedro.PedroUtility;

@TeleOp(name="Basic OpMode", group="Demos")
public class BasicOpMode extends LinearOpMode {
    ControlHub controlHub;
    Chassis chassis;

    Behavior gamepadDrivingBehavior;

    StateMachine mainStateMachine;
    State gamepadDrivingState;

    @Override
    public void runOpMode() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        gamepadDrivingBehavior = new GamepadDrivingBehavior(chassis, gamepad1);
        gamepadDrivingState = new BaseState(
                gamepadDrivingBehavior,
                () -> (gamepadDrivingState)
        );

        mainStateMachine = new StateMachine("Main State Machine");

        while (opModeInInit()) {
            controlHub.processBotIdentificationTelemetry(telemetry);
            Blackboard.initLoopProcess(telemetry, gamepad1);
            telemetry.update();
        }

        mainStateMachine.setState(gamepadDrivingState);
        mainStateMachine.enter();

        while (opModeIsActive()) {
            mainStateMachine.update();
            mainStateMachine.processTelemetry(telemetry, "");

            telemetry.update();
        }
    }
}
