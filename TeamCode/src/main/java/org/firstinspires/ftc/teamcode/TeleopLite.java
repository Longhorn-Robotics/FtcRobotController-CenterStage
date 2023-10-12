package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "TeleopLite", group = "Pushbot")
public class TeleopLite extends OpMode {
    // final float DEADZONE = 1.1f;
    /* Declare OpMode members. */
    RobotHardware robot = new RobotHardware(); // use the class created to define a Pushbot's hardware

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        /*
         * Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        // Send telemetry message to signify robot waiting
        telemetry.addData("Say", "Hello thomas");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * This is Jonathan's code for fine movement
     * If the robot isn't working try deleting this
     * Update: this code is perfect and deleting it is a sin
     */
    double movemult = 1.0;

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // Gamepad Inputs
        float gp1_throttle = gamepad1.left_stick_y;
        float gp1_strafe = gamepad1.left_stick_x;
        float gp1_yaw = gamepad1.right_stick_x;

        boolean dpL = gamepad1.dpad_left;
        boolean dpR = gamepad1.dpad_right;
        boolean dpU = gamepad1.dpad_up;
        boolean dpD = gamepad1.dpad_down;

        // Final Robot Instructions
        double final_throttle = -gp1_throttle;
        double final_strafe = gp1_strafe;
        float final_yaw = gp1_yaw;
        
        // D-pad movement
        if (dpL) {
            final_strafe -= 0.5; 
        }
        if (dpR) {
            final_strafe += 0.5; 
        }
        if (dpU) {
            final_throttle -= 0.5; 
        }
        if (dpD) {
            final_throttle += 0.5; 
        }
        
        movemult = 0.5 + gamepad1.right_trigger;

        robot.lfDrive.setPower((final_throttle - final_strafe - final_yaw) * 0.7 * movemult);
        robot.lbDrive.setPower((final_throttle + final_strafe - final_yaw) * 0.7 * movemult);
        robot.rfDrive.setPower((-final_throttle - final_strafe - final_yaw) * 0.7 * movemult);
        robot.rbDrive.setPower((-final_throttle + final_strafe - final_yaw) * 0.7 * movemult);

        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}