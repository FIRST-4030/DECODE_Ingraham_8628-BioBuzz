# The Behavior System

The Behavior System is a little modular framework for Ingraham Robotics for organizing robot logic
into reusable, composable pieces called `behavior`s.

This README documents the core functionality in the framework with examples, but each class
also has documentation in their source code. See the
[User Behavior Template](User/UserBehaviorTemplate.java) for instructions on making custom robot
capabilities using the framework, and the [Basic OpMode Demo](Demos/BasicOpMode.java) and
[Basic Sequence Demo](Demos/BasicSequenceOpMode.java).

## The [`Behavior`](Behavior.java) interface

Every `behavior` must implement the following methods:

- `enter()`: Called once when the behavior starts.
- `update()`: Called repeatedly during the main loop.
- `exit()`: Called once when the behavior completes or is interrupted.
- `isComplete()`: Defines an "end condition" for the behavior.

Almost every class you will work with in the Behavior System implements
`Behavior`. You can define new capabilities for the robot that implement `Behavior`, allowing
you to use your own custom robot functionality anywhere that a `Behavior` can be used.

Behaviors also must implement `processTelemetry()`, which is where information about
what the behavior is actively doing is printed to the driver station. Finally, behaviors
have a `getLabel()` method used to attach a short descriptor/title
to a behavior, useful in telemetry and debugging. However, most behaviors make it
optional to specify a label during instantiation.

### Example with GamepadDrivingBehavior

This opMode lets you drive the robot around using the `GamepadDrivingBehavior` behavior. The idea
is that all robot capabilities (driving, shooting, following a Pedro path, collecting, etc.) can
be handled inside decoupled behaviors. This makes all of your code very flexible and allows you
to iterate quickly.

```java
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.BehaviorSystem.Behavior;
import org.firstinspires.ftc.teamcode.BehaviorSystem.User.GamepadDrivingBehavior;
import org.firstinspires.ftc.teamcode.Chassis;

@TeleOp(name="Basic OpMode w/ the Behavior System", group="Demos")
public class BasicOpMode extends OpMode {
    Chassis chassis;
    Behavior gamepadDrivingBehavior;
    
    @Override
    public void init() {
        chassis = new Chassis(hardwareMap);
        gamepadDrivingBehavior = new GamepadDrivingBehavior(chassis, gamepad1);
    }

    @Override
    public void start() {
        gamepadDrivingBehavior.enter();
    }

    @Override
    public void loop() {
        gamepadDrivingBehavior.update();
        gamepadDrivingBehavior.processTelemetry(telemetry, "");

        telemetry.update();
    }
}
```

## `State` and `StateMachine`

The `State` interface and `StateMachine` class are great for building opModes with
different "modes" or "states" the robot can be in, such as driving, shooting, or parking.

### The `State` interface
States are just behaviors with opinions about what the next active state should be. States
do this by adding one new method to `Behavior`: `getNextState()`.

You can make a new implementation of `State` manually in your opMode if you really need to,
but the majority of the time it's faster to create states using the classes
`BaseState` or `TaskState`. These classes are useful because they let you take an
existing `Behavior` and attach it to a `State` without unnecessary boilerplate.

## `ParallelBehavior` and `SequentialBehavior`

This package includes two behaviors that are, themselves,
designed to work with lists of other behaviors. They are called
`ParallelBehavior` and `SequentialBehavior`.

### `ParallelBehavior`

A `ParallelBehavior` executes multiple `Behavior`s in a list at the same time.
For every `ParallelBehavior` you create, you can specify how it decides it is complete by
picking one of three strategies:

1. ALL: This `ParallelBehavior` is only complete once every behavior in the list
is complete.
2. ANY: This `ParallelBehavior` is complete if any of the behaviors in the list are
complete.
3. FIRST: This `ParallelBehavior` is complete if the first behavior in the list
is complete.

### `SequentialBehavior`

A `sequentialBehavior` is given a list of behaviors and executes them one at a time.

---