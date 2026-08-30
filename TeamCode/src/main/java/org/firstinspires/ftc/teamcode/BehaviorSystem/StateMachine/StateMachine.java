package org.firstinspires.ftc.teamcode.BehaviorSystem.StateMachine;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;

/**
 * Behavior that handles updating and switching of states.
 * @author Edson James
 */
public class StateMachine implements Behavior {
    private State activeState = null;
    private final String label;

    public StateMachine() {
        this("State Machine");
    }

    public StateMachine(String label) {
        this.label = label;
    }

    @Override
    public void enter() {
        if (activeState != null) activeState.enter();
    }

    /**
     * Calls update() on the active state. Switches the active state if the current active
     * state returns another state from state.getNextState().
     */
    public void update() {
        if (isComplete()) { return; }

        activeState.update();

        State nextState = activeState.getNextState();

        if (nextState != activeState) {
            // Unlike SequentialBehavior.update(), I'm NOT doing this recursively. This is to avoid
            // freezing if there is a circular chain of states.
            setState(nextState);
        }
    }

    /**
     * Completes when there is no active state.
     * @return Whether there is no active state.
     */
    @Override
    public boolean isComplete() {
        return activeState == null;
    }

    /**
     * Exits the active state, is there is one. Then, makes it so there is no active state.
     */
    @Override
    public void exit() {
        if (activeState != null) activeState.exit();
        activeState = null;
    }

    @Override
    public void processTelemetry(Telemetry telemetry, String prefix) {
        telemetry.addLine(prefix + "--- FSM: " + getLabel() + " ---");

        if (isComplete()) {
            telemetry.addLine(prefix + "State machine completed");
            telemetry.addLine("");
            return;
        }

        telemetry.addData(prefix + "    Active state", activeState.getLabel());
        telemetry.addLine("");

        if (activeState != null) {
            activeState.processTelemetry(telemetry, prefix + "    ");
        }
    }

    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Manually sets the active state. Calls exit() on the old state (if there is one) and enter()
     * on the new state.
     * @param state The state to set as the new active state.
     */
    public void setState(State state) {
        if (activeState != null) {
            activeState.exit();
        }
        activeState = state;

        if (activeState != null) {
            activeState.enter();

            // Call getNextState() getter when first entering the new state, because some
            // conditions (like gamepad functions ending in -WasPressed) only become false once
            // they've been called. Calling getNextState() here makes sure these values are all
            // accurate and don't bleed over from the previous state.
            activeState.getNextState();
        }
    }
}
