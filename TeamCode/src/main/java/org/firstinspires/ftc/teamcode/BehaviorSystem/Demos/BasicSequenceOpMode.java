package org.firstinspires.ftc.teamcode.BehaviorSystem.Demos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorBuilder;
import org.firstinspires.ftc.teamcode.BehaviorSystem.ParallelBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.BaseState;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.GamepadDrivingBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.TimerBehavior;
import org.firstinspires.ftc.teamcode.Blackboard;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;

@TeleOp(name="Basic Sequence OpMode", group="Demos")
public class BasicSequenceOpMode extends OpMode {
    ControlHub controlHub;
    Chassis chassis;

    Behavior coolSequence;

    @Override
    public void init() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        coolSequence = BehaviorBuilder.create()
                .sequential()
                    .add(new TimerBehavior(2000))
                    .add(new TimerBehavior(2000))
                    .add(new TimerBehavior(2000))
                    .parallel(ParallelBehavior.CompletionCondition.ALL)
                        .add(new TimerBehavior(4000))
                        .add(new TimerBehavior(2000))
                        .add(new TimerBehavior(3000))
                        .add(new TimerBehavior(6000))
                    .end()
                    .add(new TimerBehavior(7000))
                .end()
                .build();
    }

    @Override
    public void init_loop() {
        controlHub.processBotIdentificationTelemetry(telemetry);
        Blackboard.initLoopProcess(telemetry, gamepad1);
        telemetry.update();
    }

    @Override
    public void start() {
        coolSequence.enter();
    }

    @Override
    public void loop() {
        telemetry.addLine(coolSequence.getLabel());
        coolSequence.update();
        coolSequence.processTelemetry(telemetry, "    ");

        telemetry.update();
    }
}
