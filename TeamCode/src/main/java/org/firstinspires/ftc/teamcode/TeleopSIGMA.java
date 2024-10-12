package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.util.List;
import java.util.function.BooleanSupplier;

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
    static final double ARM_LOW = 673.0f;
    // static final double DEADZONE = 1.1f;

    final double joystickBaseSpeed = 0.3f;

    /* Declare OpMode members. */
    RobotHardwareSIGMA robot = new RobotHardwareSIGMA();
    double railPosition = 0.0f;
    double armPosition = 0.0f;
    boolean invert = false;
    boolean slowmode = false;
    boolean clawOpen = true;
    double clawPosition = CLAW_OPEN;

    // Debounce Stuff - by Teo
    // It would be a good idea to make this a seperate class or something
    // especially given the entire point of this is because it's supposed
    // to be better programming practices. But that's boring.
    private final static int maxButtons = 20;
    private int buttons = 0;
    private final BooleanSupplier[] buttonConditions = new BooleanSupplier[maxButtons];
    private final Boolean[] wasPressed = new Boolean[maxButtons];
    private final Runnable[] buttonActions = new Runnable[maxButtons];

    private int addButton(BooleanSupplier buttonCondition, Runnable action) {
        if (buttons >= maxButtons) return 1;
        buttonConditions[buttons] = buttonCondition;
        buttonActions[buttons] = action;
        wasPressed[buttons] = false;
        buttons++;
        return 0;
    }

    private void doButtonPresses() {
        for (int i = 0; i < buttons; i++) {
            boolean was = wasPressed[i];
            boolean is = buttonConditions[i].getAsBoolean();
            if (!was && is) {
                buttonActions[i].run();
            }
            wasPressed[i] = is;
        }
    }

    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        robot.init(hardwareMap);
        // Send telemetry message to signify robot waiting
        telemetry.addData("Say", "Hello thomas");

        addButton(() -> gamepad1.right_bumper, () -> slowmode = !slowmode);
        addButton(() -> gamepad1.left_bumper, () -> {
            if (railPosition == RAIL_MIN) railPosition = RAIL_MAX;
            else railPosition = RAIL_MIN;
        });
        addButton(() -> gamepad1.triangle,
                () -> {
                    if (armPosition == ARM_LOW) armPosition = ARM_HIGH;
                    else armPosition = ARM_LOW;
        });
        addButton(() -> gamepad1.circle, () -> {
            clawOpen = !clawOpen;
            robot.claw.setPosition(clawOpen ? CLAW_OPEN : CLAW_CLOSED);
        });
    }

    // Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
    @Override
    public void init_loop() {
    }

    // Code to run ONCE when the driver hits PLAY
    @Override
    public void start() {
    }

    public void wheels() {

        double final_throttle = 0.0f;
        double final_strafe = 0.0f;
        double final_yaw = 0.0f;
        double joystickMultiplier = !slowmode ? 1.0f : 0.25f;

//        double joystickMultiplier = joystickBaseSpeed + (1.0f - gamepad1.right_trigger);
//        joystickMultiplier *= invert ? -1.0f : 1.0f;

        final_throttle += (gamepad1.left_stick_y * joystickMultiplier);
        final_strafe += (gamepad1.left_stick_x * joystickMultiplier);
        final_yaw += (gamepad1.right_stick_x * joystickMultiplier);

        robot.lfDrive.setPower(final_throttle - final_strafe - final_yaw);
        robot.lbDrive.setPower(final_throttle + final_strafe - final_yaw);
        robot.rfDrive.setPower(-final_throttle - final_strafe - final_yaw);
        robot.rbDrive.setPower(-final_throttle + final_strafe - final_yaw);
    }

    @SuppressLint("DefaultLocale")
    public void rail() {
        railPosition += (gamepad1.right_trigger - gamepad1.left_trigger) * 20.0f;
        // Clamps rail position based on max and min values
        railPosition = Math.min(Math.max(railPosition, RAIL_MIN), RAIL_MAX);

        telemetry.addLine(String.format("Target RAIL Position: %d", (int) railPosition));
        if (robot.rail.getCurrentPosition() > railPosition) robot.rail.setPower(0.4);
        else robot.rail.setPower(0.8);

        robot.rail.setTargetPosition((int) railPosition);
    }

    @SuppressLint("DefaultLocale")
    public void arm() {

        if (gamepad1.dpad_up) armPosition -= 2.5;
        if (gamepad1.dpad_down) armPosition += 2.5;
//        if (gamepad1.dpad_up) clawPosition += 0.01;
//        if (gamepad1.dpad_down) clawPosition -= 0.01;

        telemetry.addLine(String.format("Current ARM Position: %d", (int) robot.arm.getCurrentPosition()));
        telemetry.addLine(String.format("Target ARM Position: %d", (int) armPosition));
        robot.arm.setTargetPosition((int) armPosition);
    }

    @SuppressLint("DefaultLocale")
    public void claw() {

        telemetry.addLine(String.format("Target CLAW Position: %f", clawPosition));
//        robot.claw.setPosition(clawPosition);
    }

    // Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {
        // Final Robot Instructions
        wheels();
        arm();
        rail();
        claw();

        doButtonPresses();

        telemetry.update();
    }

    // Code to run ONCE after the driver hits STOP
    @Override
    public void stop() {
    }
}
