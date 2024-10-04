package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/*
 _____ _____ _____ ___  ___  ___  
/  ___|_   _|  __ \|  \/  | / _ \ 
\ `--.  | | | |  \/| .  . |/ /_\ \
 `--. \ | | | | __ | |\/| ||  _  |
/\__/ /_| |_| |_\ \| |  | || | | |
\____/ \___/ \____/\_|  |_/\_| |_/
  u      n      e      a      l
  p      t      n      n      g
  e      e      e      a      o
  r      g      r      g      r
         r      a      e      i
         a      l      m      t
         t             e      h
         e             n      m
         d             t
*/
@TeleOp(name = "TeleopSIGMA", group = "Pushbot")
public class TeleopSIGMA extends OpMode {

    static final double RAIL_MIN = 0.0f;
    static final double RAIL_MAX = 3000.0f;
    static final double CLAW_OPEN = 0.75f;
    static final double CLAW_CLOSED = 0.84f;
    static final double ARM_HIGH = 0.0f;
    static final double ARM_LOW = 0.0f;
    // static final double DEADZONE = 1.1f;

    final double joystickBaseSpeed = 0.3f;

    /* Declare OpMode members. */
    RobotHardwareSIGMA robot = new RobotHardwareSIGMA();
    double railPosition = 0.0f;
    double armPosition = 0.0f;
    boolean invert = false;

    boolean circleWasPressed = false;
    boolean clawOpen = true;
    double clawPosition = CLAW_OPEN;

    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        robot.init(hardwareMap);
        // Send telemetry message to signify robot waiting
        telemetry.addData("Say", "Hello thomas");
    }

    // Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    @Override
    public void init_loop() {}

    // Code to run ONCE when the driver hits PLAY
    @Override
    public void start() {}

    // Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {
        // Final Robot Instructions

        /* WHEELS */
        double final_throttle = 0.0f;
        double final_strafe = 0.0f;
        double final_yaw = 0.0f;

        double joystickMultiplier = joystickBaseSpeed + (1.0f - gamepad1.right_trigger);
        joystickMultiplier *= invert ? -1.0f : 1.0f;

        final_throttle += (gamepad1.left_stick_y * joystickMultiplier);
        final_strafe += (gamepad1.left_stick_x * joystickMultiplier);
        final_yaw += (gamepad1.right_stick_x * joystickMultiplier);

        robot.lfDrive.setPower(final_throttle - final_strafe - final_yaw);
        robot.lbDrive.setPower(final_throttle + final_strafe - final_yaw);
        robot.rfDrive.setPower(-final_throttle - final_strafe - final_yaw);
        robot.rbDrive.setPower(-final_throttle + final_strafe - final_yaw);

        /* WHEELS DONE*/

        railPosition += (gamepad1.right_trigger - gamepad1.left_trigger) * 20.0f;
        // Clamps railPos based on max and min values
        railPosition = Math.min(Math.max(railPosition, RAIL_MIN), RAIL_MAX);
        telemetry.addLine(String.format("Target RAIL Position: %d", (int) railPosition));
        robot.rail.setTargetPosition((int) railPosition);

        if (circleWasPressed && !gamepad1.circle) {
            circleWasPressed = false;
        } else if (!circleWasPressed && gamepad1.circle) {
            circleWasPressed = true;
            clawOpen = !clawOpen;
            robot.claw.setPosition(clawOpen ? CLAW_OPEN : CLAW_CLOSED);
        }

//        if (gamepad1.dpad_up) armPosition += 0.125;
//        if (gamepad1.dpad_down) armPosition -= 0.125;
//        if (gamepad1.dpad_up) clawPosition += 0.01;
//        if (gamepad1.dpad_down) clawPosition -= 0.01;

        telemetry.addLine(String.format("Target CLAW Position: %f", clawPosition));
//        robot.claw.setPosition(clawPosition);

        telemetry.addLine(String.format("Current ARM Position: %d", (int) robot.arm.getCurrentPosition()));
        telemetry.addLine(String.format("Target ARM Position: %d", (int) armPosition));
        robot.arm.setTargetPosition((int) armPosition);

        telemetry.update();
    }

    // Code to run ONCE after the driver hits STOP
    @Override
    public void stop() {
    }
}
