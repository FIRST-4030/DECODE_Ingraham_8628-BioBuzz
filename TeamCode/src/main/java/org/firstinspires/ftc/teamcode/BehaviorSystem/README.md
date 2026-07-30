# Behavior System

The Behavior System is a little modular framework for organizing robot logic
into reusable, composable pieces called `behavior`s.

## The `Behavior` interface

Every `behavior` implements the `Behavior` interface, and must implement the
following methods:

- `enter()`: Called once when the behavior starts.
- `update()`: Called repeatedly during the main loop.
- `exit()`: Called once when the behavior completes or is interrupted.
- `isComplete()`: Defines an "end condition" for the behavior.

Almost every class you will work with in the Behavior System with implements
`Behavior`, making them reusable and easy to work with. It's also easy to
define new capabilities for the robot that implement `Behavior`, giving custom
robot logic these benefits too.

## Parallel and Sequential behaviors

The Behavior System package includes two behaviors that are, themselves,
designed to organize groups of other `behavior`s. They are called
`ParallelBehavior` and `SequentialBehavior`.

A `parallelBehavior` executes multiple `behavior`s in a list at the same time.
For every `parallelBehavior`, you can specify how it decides it is complete by
picking one of three strategies:

1. ALL: This `parallelBehavior` is only complete once every behavior in the list
is complete.
2. ANY: This `parallelBehavior` is complete if any of the behaviors are
complete.
3. FIRST: This `parallelBehavior` is complete if the first behavior in the list
is complete.

---

A `sequentialBehavior` is given a list of behaviors and executes them one at a
time.

---