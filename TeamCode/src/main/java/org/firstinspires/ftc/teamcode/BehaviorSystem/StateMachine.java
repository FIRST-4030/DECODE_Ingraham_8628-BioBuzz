package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Handles updating and switching of States.
 * @author edsonjames
 */
public class StateMachine {
    private State activeState = null;

    public StateMachine() {
    }

    /**
     * Calls update() on the active State. Handles switching to the next active State.
     */
    public void update() {
        if (activeState == null) {
            return;
        }

        activeState.update();

        State nextState = activeState.getNextState();

        if (nextState != activeState) {
            setState(nextState);
        }
    }

    /**
     * Manually sets the active State. Calls exit() on the old State and enter() on the new State.
     * @param state The State to set as the active State
     */
    public void setState(State state) {
        activeState.exit();
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
        telemetry.addData("Active state", activeState.getClass().getSimpleName());
        telemetry.addLine();
    }
}
