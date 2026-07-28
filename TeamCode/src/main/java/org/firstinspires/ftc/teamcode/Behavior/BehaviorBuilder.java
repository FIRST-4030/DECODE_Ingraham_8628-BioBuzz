package org.firstinspires.ftc.teamcode.Behavior;

import java.util.ArrayList;
import java.util.List;

/**
 * A fluent API for building complex nested Behaviors.
 * Handles Sequential and Parallel behaviors.
 */
public class BehaviorBuilder {
    private final List<Behavior> behaviors = new ArrayList<>();
    private final BehaviorBuilder parent;
    private final Type type;
    private ParallelBehavior.CompletionCondition parallelCondition;
    private String label;

    private enum Type { ROOT, SEQUENTIAL, PARALLEL }

    private BehaviorBuilder(BehaviorBuilder parent, Type type) {
        this.parent = parent;
        this.type = type;
    }

    /**
     * Starts a new BehaviorBuilder at the root.
     * @return A new BehaviorBuilder
     */
    public static BehaviorBuilder create() {
        return new BehaviorBuilder(null, Type.ROOT);
    }

    /**
     * Sets the label for the current behavior block.
     * @param label The label to set
     * @return This BehaviorBuilder
     */
    public BehaviorBuilder label(String label) {
        this.label = label;
        return this;
    }

    /**
     * Starts a sequential behavior block.
     * @return A new BehaviorBuilder for the sequential block
     */
    public BehaviorBuilder sequential() {
        return new BehaviorBuilder(this, Type.SEQUENTIAL);
    }

    /**
     * Starts a sequential behavior block with a label.
     * @param label The label for the sequential block
     * @return A new BehaviorBuilder for the sequential block
     */
    public BehaviorBuilder sequential(String label) {
        return sequential().label(label);
    }

    /**
     * Starts a parallel behavior block with the default CompletionCondition (ALL).
     * @return A new BehaviorBuilder for the parallel block
     */
    public BehaviorBuilder parallel() {
        return parallel(ParallelBehavior.CompletionCondition.ALL);
    }

    /**
     * Starts a parallel behavior block with a label and default CompletionCondition (ALL).
     * @param label The label for the parallel block
     * @return A new BehaviorBuilder for the parallel block
     */
    public BehaviorBuilder parallel(String label) {
        return parallel().label(label);
    }

    /**
     * Starts a parallel behavior block with a specific CompletionCondition.
     * @param condition The condition for the parallel block to complete
     * @return A new BehaviorBuilder for the parallel block
     */
    public BehaviorBuilder parallel(ParallelBehavior.CompletionCondition condition) {
        BehaviorBuilder child = new BehaviorBuilder(this, Type.PARALLEL);
        child.parallelCondition = condition;
        return child;
    }

    /**
     * Starts a parallel behavior block with a specific CompletionCondition and label.
     * @param condition The condition for the parallel block to complete
     * @param label The label for the parallel block
     * @return A new BehaviorBuilder for the parallel block
     */
    public BehaviorBuilder parallel(ParallelBehavior.CompletionCondition condition, String label) {
        return parallel(condition).label(label);
    }

    /**
     * Adds a behavior to the current block.
     * @param behavior The behavior to add
     * @return This BehaviorBuilder
     */
    public BehaviorBuilder add(Behavior behavior) {
        behaviors.add(behavior);
        return this;
    }

    /**
     * Ends the current sequential or parallel block and adds it to the parent block.
     * @return The parent BehaviorBuilder
     * @throws IllegalStateException if called on the root builder
     */
    public BehaviorBuilder end() {
        if (parent == null) {
            throw new IllegalStateException("Cannot call end() on root builder. Use build() instead.");
        }
        parent.add(this.internalBuild());
        return parent;
    }

    /**
     * Finalizes the build and returns the resulting behavior.
     * If multiple behaviors were added at the root level, they are wrapped in a SequentialBehavior.
     * @return The built Behavior
     */
    public Behavior build() {
        if (parent != null) {
            throw new IllegalStateException("Builder has unclosed blocks. Call end() before build().");
        }
        return internalBuild();
    }

    private Behavior internalBuild() {
        String finalLabel = (label != null) ? label : type.name();

        if (type == Type.PARALLEL) {
            return new ParallelBehavior(parallelCondition, behaviors, finalLabel);
        } else if (type == Type.SEQUENTIAL) {
            return new SequentialBehavior(behaviors, finalLabel);
        } else {
            // Root type
            if (behaviors.size() == 1 && label == null) {
                return behaviors.get(0);
            } else {
                return new SequentialBehavior(behaviors, finalLabel);
            }
        }
    }
}
