package org.firstinspires.ftc.teamcode.OpModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorStep;
import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorStepSequence;
import org.firstinspires.ftc.teamcode.BehaviorSystem.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.FollowPathBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.RealTimeBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.WaitBehavior;
import org.firstinspires.ftc.teamcode.Blackboard;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;
import org.firstinspires.ftc.teamcode.Pedro.UserPoses;

@TeleOp(name="Step Sequence Runner Demo", group="Linear OpMode")
public class TeleOpDemo extends LinearOpMode {

    // --- PEDRO ---

    ControlHub controlHub;
    Chassis chassis;
    Follower follower;

    PathChain examplePathChain1, examplePathChain2;

    // --- BEHAVIOR ---

    RealTimeBehavior realTimeBehavior;

    BehaviorStep moveToOneSpot, moveToAnotherSpot;
    BehaviorStepSequence pedroStepSequence;

    BehaviorStep waitOneSecond, waitThreeSeconds;
    BehaviorStepSequence waitingStepSequence;

    // --- STATE MACHINE ---

    State realTimeState, pedroState, waitingState;

    StateMachine stateMachine = new StateMachine();

    // --- OPMODE ---

    @Override
    public void runOpMode() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);
        realTimeBehavior = new RealTimeBehavior(chassis, gamepad1);

        follower = controlHub.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0));
        buildPaths();



        moveToOneSpot = new BehaviorStep(
                new FollowPathBehavior(follower, examplePathChain1)
        );
        moveToAnotherSpot = new BehaviorStep(
                new FollowPathBehavior(follower, examplePathChain2)
        );

        pedroStepSequence = new BehaviorStepSequence(
                new BehaviorStep[]{
                        moveToOneSpot,
                        moveToAnotherSpot,
                        moveToOneSpot,
                        moveToAnotherSpot
                }
        );


        waitOneSecond = new BehaviorStep(
                new WaitBehavior(1000)
        );
        waitThreeSeconds = new BehaviorStep(
                BehaviorStep.StepType.FINISHED_ON_PRIMARY,
                new WaitBehavior(3000),
                new Behavior[] {
                        new RealTimeBehavior(chassis, gamepad1),
                }
        );

        waitingStepSequence = new BehaviorStepSequence(
                new BehaviorStep[] {
                        waitOneSecond,
                        waitThreeSeconds,
                        waitThreeSeconds,
                        waitOneSecond
                }
        );


        makeStates();
        stateMachine.init(realTimeState);

        // --- LOOPS ---

        do {
            Blackboard.initLoopProcess(telemetry, gamepad1);

            telemetry.update();
        } while (opModeInInit());

        do {
            stateMachine.processTelemetry(telemetry);
            stateMachine.update();

            telemetry.update();

        } while (opModeIsActive());
    }

    public void buildPaths() {
        examplePathChain1 = follower.pathBuilder()
                .addPath(new BezierLine(
                        UserPoses.examplePose1,
                        UserPoses.examplePose2
                ))
                .build();

        examplePathChain2 = follower.pathBuilder()
                .addPath(new BezierLine(
                        UserPoses.examplePose2,
                        UserPoses.examplePose1
                ))
                .build();
    }

    // --- STATE MACHINE ---

    public void realTimeStateEnter() {
        realTimeBehavior.enter();
    }
    public void realTimeStateUpdate() {
        realTimeBehavior.update();
        realTimeBehavior.processTelemetry(telemetry, "  ");
    }
    public State realTimeStateGetNextState() {
        if (gamepad1.dpadDownWasPressed()) {
            return pedroState;
        } else if (gamepad1.dpadUpWasPressed()) {
            return waitingState;
        }
        return realTimeState;
    }
    public void realTimeStateExit() {
        realTimeBehavior.exit();
    }


    public void pedroStateEnter() {
        pedroStepSequence.reset();
    }
    public void pedroStateUpdate() {
        pedroStepSequence.update();
        pedroStepSequence.processTelemetry(telemetry);
    }
    public State pedroStateGetNextState() {
        if (pedroStepSequence.isFinished()) {
            return realTimeState;
        }

        return pedroState;
    }

    public void waitingStateEnter() {
        waitingStepSequence.reset();
    }
    public void waitingStateUpdate() {
        waitingStepSequence.update();
        waitingStepSequence.processTelemetry(telemetry);
    }
    public State waitingStateGetNextState() {
        if (waitingStepSequence.isFinished()) {
            return realTimeState;
        }

        return waitingState;
    }

    public void makeStates() {
        realTimeState = new State(
                "Real time control",
                this::realTimeStateEnter,
                this::realTimeStateUpdate,
                this::realTimeStateGetNextState,
                this::realTimeStateExit
        );

        pedroState = new State(
                "Pedro pathing",
                this::pedroStateEnter,
                this::pedroStateUpdate,
                this::pedroStateGetNextState,
                () -> {}
        );

        waitingState = new State(
                "Waiting and real time control demo",
                this::waitingStateEnter,
                this::waitingStateUpdate,
                this::waitingStateGetNextState,
                () -> {}
        );
    }
}
