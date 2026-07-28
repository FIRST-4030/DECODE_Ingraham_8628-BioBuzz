package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorBuilder;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behaviors.RealTimeBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behaviors.WaitBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.ParallelBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.TaskState;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;

import kotlinx.coroutines.scheduling.Task;

@TeleOp(name="BehaviorBuilder Demo", group="Demos")
public class BehaviorBuilderDemo extends LinearOpMode {
    ControlHub controlHub;
    Chassis chassis;

    Behavior realTimeBehavior, complexBehavior;

    StateMachine mainStateMachine;
    State realTimeState, complexState;

    @Override
    public void runOpMode() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        realTimeBehavior = new RealTimeBehavior(chassis, gamepad1);
        complexBehavior = BehaviorBuilder.create()
                .sequential()
                    .add(new WaitBehavior(10000))
                    .parallel(ParallelBehavior.CompletionCondition.FIRST)
                        .add(new WaitBehavior(20000))
                        .add(new WaitBehavior(15000))
                        .add(realTimeBehavior)
                    .end()
                    .sequential()
                        .add(new WaitBehavior(5000))
                        .add(new WaitBehavior(5000))
                    .end()
                .end()
                .build();

        realTimeState = new RealTimeState();
        complexState = new TaskState(complexBehavior, () -> realTimeState);

        mainStateMachine = new StateMachine();
        mainStateMachine.setState(realTimeState);

        telemetry.addLine("BehaviorBuilder Demo Initialized");
        telemetry.update();

        mainStateMachine.enter();

        waitForStart();

        while (opModeIsActive()) {
            mainStateMachine.update();
            mainStateMachine.processTelemetry(telemetry, "");

            telemetry.update();
        }

        complexBehavior.exit();
        telemetry.addLine("Behavior Complete");
        telemetry.update();
        
        sleep(2000);
    }

    class RealTimeState implements State {
        @Override
        public State getNextState() {
            if (gamepad1.dpadUpWasPressed()) {
                return complexState;
            }
            return this;
        }

        @Override
        public void enter() {
            realTimeBehavior.enter();
        }

        @Override
        public void update() {
            realTimeBehavior.update();
        }

        @Override
        public void exit() {
            realTimeBehavior.exit();
        }

        @Override
        public void processTelemetry(Telemetry telemetry, String prefix) {
            realTimeBehavior.processTelemetry(telemetry, prefix);
        }

        @Override
        public String getLabel() {
            return "RealTimeState";
        }
    }
}
