package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TeleopLite", group = "Pushbot")
public class TeleopLite extends OpMode {
    // final float DEADZONE = 1.1f;
    /* Declare OpMode members. */
    RobotHardware robot = new RobotHardware(); // use the class created to define a Pushbot's hardware

    double odometryLinitial;
    double odometryRinitial;

    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        /*
         * Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        // Send telemetry message to signify robot waiting
        telemetry.addData("Say", "Hello thomas");

        odometryLinitial = robot.encoderL.getCurrentPosition();
        odometryRinitial = robot.encoderR.getCurrentPosition();
    }

    // Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    @Override
    public void init_loop() {
    }

    // Code to run ONCE when the driver hits PLAY
    @Override
    public void start() {

    }

    final double joystickBaseSpeed = 0.3;

    // Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {
        // Final Robot Instructions
        double final_throttle = 0.0;
        double final_strafe = 0.0;
        double final_yaw = 0.0;
        
        double joystickMultiplier = joystickBaseSpeed + (1.0 - gamepad1.right_trigger);

        // Movement speed for IDCSC (Intelligent Dynamic Cruise Speed Control)
        double idcscSpeed = gamepad1.right_trigger;
        
        final_throttle += (gamepad1.left_stick_y * joystickMultiplier);
        final_strafe += (gamepad1.left_stick_x * joystickMultiplier);
        final_yaw += (gamepad1.right_stick_x * joystickMultiplier);

        // D-pad movement
        if (gamepad1.dpad_left) {
            final_strafe -= idcscSpeed;
        }
        if (gamepad1.dpad_right) {
            final_strafe += idcscSpeed;
        }
        if (gamepad1.dpad_down) {
            final_throttle += idcscSpeed;
        }
        if (gamepad1.dpad_up) {
            final_throttle -= idcscSpeed;
        }
        
        robot.lfDrive.setPower(final_throttle - final_strafe - final_yaw);
        robot.lbDrive.setPower(final_throttle + final_strafe - final_yaw);
        robot.rfDrive.setPower(-final_throttle - final_strafe - final_yaw);
        robot.rbDrive.setPower(-final_throttle + final_strafe - final_yaw);

        telemetry.addData("OdometryL: ", (odometryLinitial - robot.encoderL.getCurrentPosition()) / robot.COUNTS_PER_INCH);
        telemetry.addData("OdometryR: ", (odometryRinitial - robot.encoderR.getCurrentPosition()) / robot.COUNTS_PER_INCH);

        telemetry.update();
    }

    // Code to run ONCE after the driver hits STOP
    @Override
    public void stop() {
    }
}