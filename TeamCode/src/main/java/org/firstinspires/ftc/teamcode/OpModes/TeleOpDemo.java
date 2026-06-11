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
    ControlHub controlHub;
    Chassis chassis;
    Follower follower;


    PathChain examplePathChain1, examplePathChain2;
    BehaviorStep moveToOneSpot, moveToAnotherSpot;
    BehaviorStepSequence pedroStepSequence;

    BehaviorStep waitOneSecond, waitThreeSeconds;
    BehaviorStepSequence waitingStepSequence;


    RealTimeBehavior realTimeBehavior;

    State realTimeState, pedroState, waitingState;
    StateMachine stateMachine = new StateMachine();

    @Override
    public void runOpMode() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        follower = controlHub.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0));

        buildPaths();

        makeBehaviorSteps();
        makeBehaviorStepSequences();

        realTimeBehavior = new RealTimeBehavior(chassis, gamepad1);

        makeStates();
        stateMachine.setState(realTimeState);

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

    public void makeBehaviorSteps() {
        moveToOneSpot = new BehaviorStep(
                new FollowPathBehavior(follower, examplePathChain1)
        );

        moveToAnotherSpot = new BehaviorStep(
                new FollowPathBehavior(follower, examplePathChain2)
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
    }

    public void makeBehaviorStepSequences() {
        waitingStepSequence = new BehaviorStepSequence(
                new BehaviorStep[] {
                        waitOneSecond,
                        waitThreeSeconds,
                        waitThreeSeconds,
                        waitOneSecond
                }
        );

        pedroStepSequence = new BehaviorStepSequence(
                new BehaviorStep[]{
                        moveToOneSpot,
                        moveToAnotherSpot,
                        moveToOneSpot,
                        moveToAnotherSpot
                }
        );
    }

    public class RealTimeState implements State {
        @Override
        public void enter() {
            realTimeBehavior.enter();
        }

        @Override
        public void update() {
            realTimeBehavior.update();
            realTimeBehavior.processTelemetry(telemetry, "  ");
        }

        @Override
        public State getNextState() {
            if (gamepad1.dpadDownWasPressed()) {
                return pedroState;
            } else if (gamepad1.dpadUpWasPressed()) {
                return waitingState;
            }
            return realTimeState;
        }

        @Override
        public void exit() {
            realTimeBehavior.exit();
        }
    }

    public class PedroState implements State {
        @Override
        public void enter() {
            pedroStepSequence.reset();
        }

        @Override
        public void update() {
            pedroStepSequence.update();
            pedroStepSequence.processTelemetry(telemetry);
        }

        @Override
        public State getNextState() {
            if (pedroStepSequence.isFinished()) {
                return realTimeState;
            }

            return pedroState;
        }

        @Override
        public void exit() {

        }
    }

    public class WaitingState implements State {
        @Override
        public void enter() {
            waitingStepSequence.reset();
        }

        @Override
        public void update() {
            waitingStepSequence.update();
            waitingStepSequence.processTelemetry(telemetry);
        }

        @Override
        public State getNextState() {
            if (waitingStepSequence.isFinished()) {
                return realTimeState;
            }

            return waitingState;
        }

        @Override
        public void exit() {

        }
    }

    public void makeStates() {
        realTimeState = new RealTimeState();
        pedroState = new PedroState();
        waitingState = new WaitingState();
    }
}
