package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

// S uper
// I ntegrated
// G eneral
// M anagement
// A lgorithm

@TeleOp(name = "TeleopSIGMA", group = "Pushbot")
public class TeleopSIGMA extends OpMode {
    // final float DEADZONE = 1.1f;
    /* Declare OpMode members. */
    RobotHardware robot = new RobotHardware();

    double railPos = 0.0f;
    static double RAIL_MIN = 0.0f;
    static double RAIL_MAX = 3000.0f;
    boolean invert = false;

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
        telemetry.addLine(String.format("Zero Position: %d", robot.rail.getCurrentPosition()));

        // Initial rail position
        //  railPos = 0;
    }

    // Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    @Override
    public void init_loop() {
    }

    // Code to run ONCE when the driver hits PLAY
    @Override
    public void start() {

    }

    final double joystickBaseSpeed = 0.3f;

    // Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {
        // Final Robot Instructions
        double final_throttle = 0.0f;
        double final_strafe = 0.0f;
        double final_yaw = 0.0f;

        double joystickMultiplier = joystickBaseSpeed + (1.0f - gamepad1.right_trigger);
        joystickMultiplier *= invert ? -1.0f : 1.0f;

        // Movement speed for IDCSC (Intelligent Dynamic Cruise Speed Control)
//        double idcscSpeed = gamepad1.right_trigger;


        final_throttle += (gamepad1.left_stick_y * joystickMultiplier);
        final_strafe += (gamepad1.left_stick_x * joystickMultiplier);
        final_yaw += (gamepad1.right_stick_x * joystickMultiplier);

//        // D-pad movement
//        if (gamepad1.dpad_left) {
//            final_strafe -= idcscSpeed;
//        }
//        if (gamepad1.dpad_right) {
//            final_strafe += idcscSpeed;
//        }
//        if (gamepad1.dpad_down) {
//            final_throttle += idcscSpeed;
//        }
//        if (gamepad1.dpad_up) {
//            final_throttle -= idcscSpeed;
//        }
        
        robot.lfDrive.setPower(final_throttle - final_strafe - final_yaw);
        robot.lbDrive.setPower(final_throttle + final_strafe - final_yaw);
        robot.rfDrive.setPower(-final_throttle - final_strafe - final_yaw);
        robot.rbDrive.setPower(-final_throttle + final_strafe - final_yaw);

        telemetry.addLine(String.format("Right Trigger: %6.2f", gamepad1.right_trigger));

        railPos += (gamepad1.right_trigger - gamepad1.left_trigger) * 20.0f;

        // Clamps railPos based on max and min values
        railPos = Math.min(Math.max(railPos, RAIL_MIN), RAIL_MAX);
//        if (railPos < RAIL_MIN) railPos = RAIL_MIN
//        else if (railPos > RAIL_MAX) railPos = RAIL_MAX;

        telemetry.addLine(String.format("Rail Position: %d", robot.rail.getCurrentPosition()));
        telemetry.addLine(String.format("Target Position: %d", (int) railPos));

        robot.rail.setTargetPosition((int) railPos);

        telemetry.update();
    }

    // Code to run ONCE after the driver hits STOP
    @Override
    public void stop() {
    }
}