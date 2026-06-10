package org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

public class FollowPathBehavior implements Behavior {
    private final Follower follower;
    private final PathChain pathChain;

    public FollowPathBehavior(Follower follower, PathChain pathChain) {
        this.follower = follower;
        this.pathChain = pathChain;
    }

    @Override
    public void enter() {
        follower.followPath(pathChain);
    }

    @Override
    public void update() {
        follower.update();
    }

    @Override
    public boolean isFinished() {
        return !follower.isBusy();
    }

    @Override
    public void exit() {

    }

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        telemetry.addLine(prefix + "(Current T value: " + follower.getCurrentTValue() + ")");
    }
}