package org.firstinspires.ftc.teamcode.OpModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorStep;
import org.firstinspires.ftc.teamcode.BehaviorSystem.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors.FollowPathBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorStepSequence;
import org.firstinspires.ftc.teamcode.Pedro.Constants;
import org.firstinspires.ftc.teamcode.Pedro.ConstantsCompetition;
import org.firstinspires.ftc.teamcode.Pedro.UserPoses;

@Autonomous(name="Step Sequence Runner Demo", group="Linear OpMode")
public class StepSequenceRunnerDemo extends LinearOpMode {
    // --- PEDRO ---

    Constants constants = new ConstantsCompetition();
    Follower follower = constants.createFollower(hardwareMap);

    PathChain examplePathChain1;
    PathChain examplePathChain2;

    // --- BEHAVIOR ---

    BehaviorStep moveToOneSpot = new BehaviorStep(
            new FollowPathBehavior(follower, examplePathChain1)
    );

    BehaviorStep moveToAnotherSpot = new BehaviorStep(
            new FollowPathBehavior(follower, examplePathChain2)
    );

    BehaviorStepSequence farAuto = new BehaviorStepSequence(
            new BehaviorStep[]{
                    moveToOneSpot,
                    moveToAnotherSpot,
                    moveToOneSpot,
                    moveToAnotherSpot
            }
    );

    // --- STATE MACHINE ---

    State teleState, pedroState;

    StateMachine stateMachine = new StateMachine(telemetry);

    // --- OPMODE ---

    @Override
    public void runOpMode() {
        buildPaths();
        makeStates();
        stateMachine.init(teleState);

        do {

        } while (opModeInInit());

        do {
            stateMachine.update();

            telemetry.update();

        } while (opModeIsActive());
    }

    public void drive() {

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

    public void teleStateUpdate() {
        drive();
    }
    public State teleStateGetNextStateOrNull() {
        if (gamepad1.dpadDownWasPressed()) {
            return pedroState;
        }
        return null;
    }


    public void pedroStateInit() {
        farAuto.reset();
    }
    public void pedroStateUpdate() {
        farAuto.update();
    }
    public State pedroStateGetNextStateOrNull() {
        if (farAuto.isFinished()) {
            return teleState;
        }

        return null;
    }


    public void makeStates() {
        teleState = new State(
                () -> {},
                this::teleStateUpdate,
                this::teleStateGetNextStateOrNull,
                () -> {}
        );

        pedroState = new State(
                this::pedroStateInit,
                this::pedroStateUpdate,
                this::pedroStateGetNextStateOrNull,
                () -> {}
        );
    }
}
