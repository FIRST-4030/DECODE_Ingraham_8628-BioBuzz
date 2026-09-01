package org.firstinspires.ftc.teamcode.BehaviorSystem.Demos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorBuilder;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.TaskState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.InstantBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.LambdaBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.WaitMS;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.WaitUntil;
import org.firstinspires.ftc.teamcode.Blackboard;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;

@TeleOp(name="Cool", group="Demos")
public class CoolSequenceThingy extends OpMode {
    ControlHub controlHub;
    Chassis chassis;

    LambdaBehavior coolLambdaBehavior;

    StateMachine mainStateMachine;
    State mainState;

    @Override
    public void init() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        coolLambdaBehavior = new LambdaBehavior(
                () -> { chassis.resetZeroPowerBehavior(); },
                () -> {
                    chassis.drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
                },
                () -> gamepad1.a,
                () -> {},
                () -> "(Driving woohoo!)",
                "My amazing lambda behavior, press a to exit"
        );

        mainState = new TaskState(
                BehaviorBuilder.create()
                        .sequential()
                            .add(new WaitMS(5000))
                            .add(coolLambdaBehavior)
                            .add(new WaitMS(5000))
                            .add(new WaitUntil(() -> gamepad1.b, "Press B!"))
                            .add(new InstantBehavior(
                                    () -> { chassis.stopMotors(); }
                            ))
                            .add(new WaitMS(5000))
                        .end()
                        .build(),
                () -> null
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
        mainStateMachine.setInitialState(mainState);
        mainStateMachine.enter();
    }

    @Override
    public void loop() {
        mainStateMachine.update();
        mainStateMachine.processTelemetry(telemetry, "");

        telemetry.update();
    }
}
