package org.firstinspires.ftc.teamcode.StepRunner;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;

public class FollowPathBehavior extends Behavior {
    private final Follower follower;
    private final PathChain pathChain;

    public FollowPathBehavior(Follower follower, PathChain pathChain) {
        this.follower = follower;
        this.pathChain = pathChain;
    }

    @Override
    public void init() {
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
}