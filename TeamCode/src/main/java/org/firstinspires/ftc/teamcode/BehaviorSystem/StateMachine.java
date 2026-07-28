package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Behavior that handles updating and switching of States.
 * @author edsonjames
 */
public class StateMachine implements Behavior {
    private State activeState = null;

    @Override
    public void enter() {
        if (activeState != null) activeState.enter();
    }

    /**
     * Calls update() on the active State. Switches the active State if the current active
     * State returns another State from State.getNextState().
     */
    public void update() {
        if (isComplete()) { return; }

        activeState.update();

        State nextState = activeState.getNextState();

        if (nextState != activeState) {
            setState(nextState);
        }
    }

    @Override
    public boolean isComplete() {
        return activeState == null;
    }

    @Override
    public void exit() {
        if (activeState != null) activeState.exit();
    }

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        telemetry.addLine(prefix + "--- STATE MACHINE ---");

        if (isComplete()) {
            telemetry.addLine(prefix + "State machine completed");
            return;
        }

        telemetry.addData(prefix + "|  Active State", activeState.getClass().getSimpleName());
        if (activeState != null) {
            activeState.processTelemetry(telemetry, prefix + "|  ");
        }
    }

    /**
     * Manually sets the active State. Calls exit() on the old State (if there is one) and enter()
     * on the new State.
     * @param state The State to set as the active State
     */
    public void setState(State state) {
        if (activeState != null) {
            activeState.exit();
        }
        activeState = state;
        activeState.enter();
    }

    /**
     * Adds lines of information about the StateMachine to a Telemetry instance, including the
     * active State's simple class name.
     * @param telemetry The Telemetry instance to print to
     */
    public void processTelemetry(Telemetry telemetry) {
        telemetry.addLine("--- STATE MACHINE ---");

        if (activeState == null) {
            telemetry.addLine("No active state");
        } else {
            telemetry.addData("Active state", activeState.getClass().getSimpleName());
        }
    }
}
