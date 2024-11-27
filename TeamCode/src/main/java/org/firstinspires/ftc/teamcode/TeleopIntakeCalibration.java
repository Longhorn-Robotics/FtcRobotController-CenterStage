package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.function.BooleanSupplier;

@TeleOp(name = "TeleopIntakeCalibration", group = "Pushbot")
public class TeleopIntakeCalibration extends OpMode {

    static final double RAIL_MIN = 10.0f;
    static final double RAIL_MAX = 3000.0f;
    static final double CLAW_OPEN = 0.0f;
    static final double CLAW_CLOSED = 0.75f;
    static final double ARM_DOWN = 5.0f;
    static final double ARM_UP = 673.0f;

    /* Declare OpMode members. */
    RobotHardwareIntakeCalibration robot = new RobotHardwareIntakeCalibration();
    double pinchPosition = 0.0f;
    double extendPosition = 0.0f;
    double pivotPosition = 0.0f;

    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        robot.init(hardwareMap);
        // Send telemetry message to signify robot waiting
        telemetry.addData("Say", "Hello thomas");

        pinchPosition = robot.pincher.getPosition();
        extendPosition = robot.extender.getPosition();
        pivotPosition = robot.pivot.getPosition();
    }

    // Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    @Override
    public void init_loop() {
    }

    // Code to run ONCE when the driver hits PLAY
    @Override
    public void start() {
    }

    // Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {
        if (gamepad1.dpad_right) pinchPosition += 0.002;
        if (gamepad1.dpad_left) pinchPosition -= 0.002;

        if (gamepad1.dpad_up) extendPosition += 0.002;
        if (gamepad1.dpad_down) extendPosition -= 0.002;

        if (gamepad1.left_bumper) pivotPosition += 0.002;
        if (gamepad1.right_bumper) pivotPosition -= 0.002;

        telemetry.addLine(String.format("Pinch Position: %6.2f", pinchPosition));
        telemetry.addLine(String.format("Extend Position: %6.2f", extendPosition));
        telemetry.addLine(String.format("Pivot Position: %6.2f", pivotPosition));

        // pivot down: 0.04
        // pivot floating: 0.20
        // pivot back: 0.90

        // pinch open: 0.30
        // pinch closed: 0.42

        // extend out: 0.52
        // extend in: 0.91

        robot.extender.setPosition(extendPosition);
        robot.pincher.setPosition(pinchPosition);
        robot.pivot.setPosition(pivotPosition);

        telemetry.update();
    }

    // Code to run ONCE after the driver hits STOP
    @Override
    public void stop() {
    }
}
