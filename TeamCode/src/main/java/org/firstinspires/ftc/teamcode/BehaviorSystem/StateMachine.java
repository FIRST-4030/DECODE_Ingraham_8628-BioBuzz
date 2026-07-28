package org.firstinspires.ftc.teamcode.BehaviorSystem;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Behavior that handles updating and switching of States.
 * @author edsonjames
 */
public class StateMachine implements Behavior {
    private State activeState = null;
    private final String label;

    public StateMachine() {
        this("StateMachine");
    }

    public StateMachine(String label) {
        this.label = label;
    }

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
        telemetry.addLine(prefix + "--- FSM: " + getLabel() + " ---");

        if (isComplete()) {
            telemetry.addLine(prefix + "State machine completed");
            return;
        }

        telemetry.addData(prefix + "    Active State", activeState.getClass().getSimpleName());
        if (activeState != null) {
            activeState.processTelemetry(telemetry, prefix + "    ");
        }
    }

    @Override
    public String getLabel() {
        return label;
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
}
