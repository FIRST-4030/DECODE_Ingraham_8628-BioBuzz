package org.firstinspires.ftc.teamcode.OpModes;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.State;
import org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine;
import org.firstinspires.ftc.teamcode.BehaviorSystem.TaskState;
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

    StateMachine mainStateMachine = new StateMachine();

    @Override
    public void runOpMode() {
        controlHub = new ControlHub();
        chassis = new Chassis(hardwareMap);

        follower = controlHub.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, 0));

        pedroUtility = new PedroUtility(follower);
        buildPaths();

        mainStateMachine.enter();

        do {
            Blackboard.initLoopProcess(telemetry, gamepad1);

            telemetry.update();
        } while (opModeInInit());

        do {
            mainStateMachine.update();
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
}
