package org.firstinspires.ftc.teamcode.BehaviorSystem.Demos;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine.StateMachine;
import org.firstinspires.ftc.teamcode.Blackboard;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;

@Disabled
@TeleOp(name="OpMode Template", group="Demos")
public class OpModeTemplate extends OpMode {
    ControlHub controlHub;
    Chassis chassis;

    // State state1, state2 ...
    StateMachine mainStateMachine;

    @Override
    public void init() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

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
        // mainStateMachine.setState( ... );
        mainStateMachine.enter();
    }

    @Override
    public void loop() {
        mainStateMachine.update();
        mainStateMachine.processTelemetry(telemetry, "");

        telemetry.update();
    }
}
