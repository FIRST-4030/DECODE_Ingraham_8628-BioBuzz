package org.firstinspires.ftc.teamcode.BehaviorSystem.UserBehaviors;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

/**
 * Behavior that uses a Pedro Follower to follow a given pathChain. Completes when the follower is no
 * longer busy.
 */
public class FollowPathBehavior implements Behavior {
    private final Follower follower;
    private final PathChain pathChain;

    /**
     * Passes the Follower and PathChain to follower to this Behavior.
     * @param follower The Pedro Follower to use.
     * @param pathChain The Pedro PathChain to follow.
     */
    public FollowPathBehavior(Follower follower, PathChain pathChain) {
        this.follower = follower;
        this.pathChain = pathChain;
    }

    /**
     * Follows the PathChain using follower.followPath().
     */
    @Override
    public void enter() {
        follower.followPath(pathChain);
    }

    /**
     * Updates the Follower using follower.update().
     */
    @Override
    public void update() {
        follower.update();
    }

    /**
     * Returns true if the Follower is no longer busy.
     * @return Whether the Follower is not busy.
     */
    @Override
    public boolean isComplete() {
        return !follower.isBusy();
    }

    /**
     * Does nothing.
     */
    @Override
    public void exit() {}

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        telemetry.addLine(prefix + "(Current T value: " + follower.getCurrentTValue() + ")");
    }
}