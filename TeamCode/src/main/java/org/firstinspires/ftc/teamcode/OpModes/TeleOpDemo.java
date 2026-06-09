package org.firstinspires.ftc.teamcode.OpModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorStep;
import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorStepSequence;
import org.firstinspires.ftc.teamcode.BehaviorSystem.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.FollowPathBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.RealTimeBehavior;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;
import org.firstinspires.ftc.teamcode.Pedro.UserPoses;

@TeleOp(name="Step Sequence Runner Demo", group="Linear OpMode")
public class TeleOpDemo extends LinearOpMode {

    // --- PEDRO ---

    ControlHub controlHub = new ControlHub();
    Chassis chassis = new Chassis(hardwareMap);
    Follower follower = controlHub.createFollower(hardwareMap);

    PathChain examplePathChain1;
    PathChain examplePathChain2;

    // --- BEHAVIOR ---

    RealTimeBehavior realTimeBehavior = new RealTimeBehavior(chassis, gamepad1);

    BehaviorStep moveToOneSpot = new BehaviorStep(
            new FollowPathBehavior(follower, examplePathChain1)
    );
    BehaviorStep moveToAnotherSpot = new BehaviorStep(
            new FollowPathBehavior(follower, examplePathChain2)
    );

    BehaviorStepSequence pedroStepSequence = new BehaviorStepSequence(
            new BehaviorStep[]{
                    moveToOneSpot,
                    moveToAnotherSpot,
                    moveToOneSpot,
                    moveToAnotherSpot
            }
    );

    // --- STATE MACHINE ---

    State realTimeState, pedroState;

    StateMachine stateMachine = new StateMachine();

    // --- OPMODE ---

    @Override
    public void runOpMode() {
        buildPaths();
        makeStates();
        stateMachine.init(realTimeState);

        do {

        } while (opModeInInit());

        do {
            stateMachine.update();

            stateMachine.processTelemetry(telemetry);
            pedroStepSequence.processTelemetry(telemetry);

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
    }
    public State realTimeStateGetNextState() {
        if (gamepad1.dpadDownWasPressed()) {
            return pedroState;
        }
        return realTimeState;
    }


    public void pedroStateEnter() {
        pedroStepSequence.reset();
    }
    public void pedroStateUpdate() {
        pedroStepSequence.update();
    }
    public State pedroStateGetNextState() {
        if (pedroStepSequence.isFinished()) {
            return realTimeState;
        }

        return pedroState;
    }


    public void makeStates() {
        realTimeState = new State(
                this::realTimeStateEnter,
                this::realTimeStateUpdate,
                this::realTimeStateGetNextState,
                () -> {}
        );

        pedroState = new State(
                this::pedroStateEnter,
                this::pedroStateUpdate,
                this::pedroStateGetNextState,
                () -> {}
        );
    }
}
