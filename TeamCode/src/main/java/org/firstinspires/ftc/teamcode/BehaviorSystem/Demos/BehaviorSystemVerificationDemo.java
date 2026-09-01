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
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.GamepadDrivingBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.TimerBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.WaitForConditionBehavior;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;

/**
 * Verification OpMode for the BehaviorSystem.
 * Tests:
 * 1. Sequential Loop (recursion fix)
 * 2. Parallel Lifecycle (immediate exit fix)
 * 3. Parallel Caching (event stealing fix)
 * 4. processSimpleTelemetry (recursive instructions)
 * 5. InterruptableTaskState (cancellation)
 */
@TeleOp(name="Behavior System Verification", group="Demos")
public class BehaviorSystemVerificationDemo extends OpMode {
    ControlHub controlHub;
    Chassis chassis;
    StateMachine mainFlow;
    
    State setupState, runningState, interruptedState, finishedState;

    @Override
    public void init() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        // State 1: Setup with complex sequence
        setupState = new TaskState(
            BehaviorBuilder.create()
                    .sequential("Startup Sequence")
                        .add(new TimerBehavior(1000, "Initializing..."))
                        .sequential("Instant Chain (Check for recursion bug)")
                            .add(new WaitForConditionBehavior(() -> true, "Check 1"))
                            .add(new WaitForConditionBehavior(() -> true, "Check 2"))
                            .add(new WaitForConditionBehavior(() -> true, "Check 3"))
                            .add(new WaitForConditionBehavior(() -> true, "Check 4"))
                            .add(new WaitForConditionBehavior(() -> true, "Check 5"))
                        .end()
                    .end()
                    .build(),
            () -> runningState,
            () -> "Setting up..."
        );

        // State 2: Main running state with parallel race and interruption
        runningState = new InterruptableTaskState(
            BehaviorBuilder.create()
                    .parallel(ParallelBehavior.CompletionCondition.FIRST_IN_LIST, "Race: Time vs Manual")
                        .add(new TimerBehavior(10000, "10s Timer"))
                        .parallel(ParallelBehavior.CompletionCondition.FIRST_IN_LIST)
                            .add(new TimerBehavior(1000))
                            .add(new GamepadDrivingBehavior(chassis, gamepad1))
                        .end()
                    .end()
                    .build(),
            () -> finishedState,
            () -> {
                if (gamepad1.b) return interruptedState;
                return runningState;
            },
            () -> "[A: Finish Task | B: Interrupt Flow]"
        );

        interruptedState = new TaskState(
            new TimerBehavior(2000, "Interrupted! Waiting 2s..."),
            () -> finishedState
        );

        finishedState = new BaseState(
            new WaitForConditionBehavior(() -> false, "All tests done."),
            () -> finishedState
        );

        mainFlow = new StateMachine("Main Verification Flow");
        mainFlow.setInitialState(setupState);
    }

    @Override
    public void start() {
        mainFlow.enter();
    }

    @Override
    public void loop() {
        mainFlow.update();

        telemetry.addLine("=== FULL TELEMETRY ===");
        mainFlow.processTelemetry(telemetry, "");
        
        telemetry.addLine("\n=== SIMPLE TELEMETRY (Instructions) ===");
        mainFlow.processSimpleTelemetry(telemetry, "");

        telemetry.update();
    }
}
