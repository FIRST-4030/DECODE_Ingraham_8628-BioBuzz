package org.firstinspires.ftc.teamcode.BehaviorSystem.Demos;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorBuilder;
import org.firstinspires.ftc.teamcode.BehaviorSystem.ParallelBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.TimerBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.WaitForConditionBehavior;
import org.firstinspires.ftc.teamcode.Blackboard;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;

/**
 * Stress test for the BehaviorSystem to verify recursion fixes, telemetry caching,
 * and complex nesting.
 */
@TeleOp(name="Behavior System Stress Test", group="Demos")
public class StressTestDemo extends OpMode {
    ControlHub controlHub;
    Chassis chassis;
    Behavior complexBehavior;

    @Override
    public void init() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        // This behavior is designed to stress every part of the system
        complexBehavior = BehaviorBuilder.create()
                .sequential("Main Stress Test")
                    .add(new TimerBehavior(2000, "1s Buffer"))

                    // 1. Test "Instant" transitions (recursion fix)
                    .sequential("Instant Chain (Should finish in 1 frame)")
                        .add(new WaitForConditionBehavior(() -> true, "Instant 1"))
                        .add(new WaitForConditionBehavior(() -> true, "Instant 2"))
                        .add(new WaitForConditionBehavior(() -> true, "Instant 3"))
                        .add(new WaitForConditionBehavior(() -> true, "Instant 4"))
                        .add(new WaitForConditionBehavior(() -> true, "Instant 5"))
                    .end()

                    .add(new TimerBehavior(1000, "1s Buffer"))

                    // 2. Test Parallel Caching (Event Stealing fix)
                    .parallel(ParallelBehavior.CompletionCondition.ANY, "Parallel Race")
                        .add(new TimerBehavior(5000, "5s Timer"))
                        .add(new WaitForConditionBehavior(() -> !gamepad1.a, "Don't press A yet!!"))
                        .add(new WaitForConditionBehavior(() -> gamepad1.a, "Press A to Skip Timer"))
                    .end()

                    // 3. Deep Nesting
                    .sequential("Deep Nest")
                        .sequential("Level 2")
                            .parallel("Level 3 Parallel")
                                .add(new TimerBehavior(2000, "Task A"))
                                .add(new TimerBehavior(3000, "Task B"))
                            .end()
                        .end()
                    .end()

                    .add(new WaitForConditionBehavior(() -> !gamepad1.a, "Don't press A yet!!"))
                    .add(new WaitForConditionBehavior(() -> gamepad1.a, "Final check: Press A to finish"))
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
        complexBehavior.enter();
    }

    @Override
    public void loop() {
        complexBehavior.update();
        complexBehavior.processTelemetry(telemetry, "");

        if (complexBehavior.isComplete()) {
            telemetry.addLine("!!! ALL TESTS PASSED !!!");
        }
        telemetry.update();
    }
}