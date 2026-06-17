package org.firstinspires.ftc.teamcode.OpModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorStep;
import org.firstinspires.ftc.teamcode.BehaviorSystem.BehaviorStepSequencePerformer;
import org.firstinspires.ftc.teamcode.BehaviorSystem.CommonBehaviorStepsFactory;
import org.firstinspires.ftc.teamcode.BehaviorSystem.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behaviors.RealTimeBehavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviorStepsFactory;
import org.firstinspires.ftc.teamcode.Blackboard;
import org.firstinspires.ftc.teamcode.Chassis;
import org.firstinspires.ftc.teamcode.ControlHub;
import org.firstinspires.ftc.teamcode.Pedro.PedroUtility;
import org.firstinspires.ftc.teamcode.Pedro.UserPoses;

@TeleOp(name="Step Sequence Runner Demo", group="Linear OpMode")
public class TeleOpDemo extends LinearOpMode {
    ControlHub controlHub;
    Chassis chassis;
    Follower follower;

    PedroUtility pedroUtility;
    PathChain examplePathChain1, examplePathChain2;

    BehaviorStepSequencePerformer pedroStepSequencePerformer;
    BehaviorStepSequencePerformer waitingStepSequencePerformer;

    RealTimeBehavior realTimeBehavior;

    State realTimeState, pedroState, waitingState;
    StateMachine stateMachine = new StateMachine();

    @Override
    public void runOpMode() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        follower = controlHub.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));

        pedroUtility = new PedroUtility(follower);
        buildPaths();

        makeBehaviorStepSequencePerformers();

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
        examplePathChain1 = pedroUtility.makeCommonTwoPosePathChain(
                UserPoses.examplePose1,
                UserPoses.examplePose2
        );

        examplePathChain2 = pedroUtility.makeCommonTwoPosePathChain(
                UserPoses.examplePose2,
                UserPoses.examplePose1
        );
    }

    public void makeBehaviorStepSequencePerformers() {
        waitingStepSequencePerformer = new BehaviorStepSequencePerformer(
                new BehaviorStep[] {
                        CommonBehaviorStepsFactory.makeWaitBehaviorStep(1000),
                        UserBehaviorStepsFactory.makeTimedRealTimeStep(chassis, gamepad1, 3000),
                        CommonBehaviorStepsFactory.makeWaitBehaviorStep(1000)
                }
        );

        pedroStepSequencePerformer = new BehaviorStepSequencePerformer(
                new BehaviorStep[]{
                        CommonBehaviorStepsFactory.makeFollowPathBehaviorStep(follower, examplePathChain1),
                        CommonBehaviorStepsFactory.makeFollowPathBehaviorStep(follower, examplePathChain2),
                        CommonBehaviorStepsFactory.makeFollowPathBehaviorStep(follower, examplePathChain1),
                        CommonBehaviorStepsFactory.makeFollowPathBehaviorStep(follower, examplePathChain2)
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

            return this;
        }

        @Override
        public void exit() {
            realTimeBehavior.exit();
        }
    }

    public class PedroState implements State {
        @Override
        public void enter() {
            pedroStepSequencePerformer.reset();
        }

        @Override
        public void update() {
            pedroStepSequencePerformer.update();
            pedroStepSequencePerformer.processTelemetry(telemetry);
        }

        @Override
        public State getNextState() {
            if (pedroStepSequencePerformer.isComplete()) {
                return realTimeState;
            }

            return this;
        }

        @Override
        public void exit() {

        }
    }

    public class WaitingState implements State {
        @Override
        public void enter() {
            waitingStepSequencePerformer.reset();
        }

        @Override
        public void update() {
            waitingStepSequencePerformer.update();
            waitingStepSequencePerformer.processTelemetry(telemetry);
        }

        @Override
        public State getNextState() {
            if (waitingStepSequencePerformer.isComplete()) {
                return realTimeState;
            }

            return this;
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
