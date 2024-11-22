package org.firstinspires.ftc.teamcode;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

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

    static final double RAIL_MIN = 10.0f;
    static final double RAIL_MAX = 3000.0f;
    static final double PINCH_OPEN = 0.3f;
    static final double PINCH_CLOSED = 0.42f;
    static final double EXTEND_IN = 0.52f;
    static final double EXTEND_OUT = 0.91f;
    static final double PIVOT_DOWN = 0.04f;
    static final double PIVOT_FLOAT = 0.20f;
    static final double PIVOT_BACK = 0.90f;

    /* Declare OpMode members. */
    RobotHardwareSIGMA robot = new RobotHardwareSIGMA();
    double railPosition = 0.0f;
    double extendPosition = 0.0f;
    boolean slowmode = false;
    boolean clawOpen = true;
    double pinchPosition = PINCH_OPEN;
    int pivotState = 0;

    // Debounce Stuff - by Teo
    // It would be a good idea to make this a separate class or something
    // especially given the entire point of this is because it's supposed
    // to be better programming practices. But that's boring.
    // TODO: Find better acronyms
    private final ButtonAction[] buttonActions = {
            new ButtonAction(() -> gamepad1.right_bumper, () -> slowmode = !slowmode),
            new ButtonAction(() -> gamepad2.triangle, () -> {
                if (extendPosition == EXTEND_IN) extendPosition = EXTEND_OUT;
                else extendPosition = EXTEND_IN;
            }),
            new ButtonAction(() -> gamepad2.left_bumper, () -> {
                if (railPosition == RAIL_MIN) railPosition = RAIL_MAX;
                else railPosition = RAIL_MIN;
            }),
            new ButtonAction(() -> gamepad2.circle, () -> {
                clawOpen = !clawOpen;
                robot.clawPinch.setPosition(clawOpen ? PINCH_CLOSED : PINCH_OPEN);
            }),
            new ButtonAction(() -> gamepad2.dpad_left, () -> pivotState++),
            new ButtonAction(() -> gamepad2.dpad_right, () -> pivotState--)
    };

    // Another cool functional programming interface
    // This time for the common pattern of targeted motors

    @SuppressLint("DefaultLocale")
    private final TargetedMotor[] targetedMotors = {
            new TargetedMotor(RAIL_MIN, RAIL_MAX, () -> railPosition, a -> railPosition = a, a -> robot.railMotors.apply(motor -> {motor.setTargetPosition((int) railPosition);
                    telemetry.addLine(String.format("Target RAIL Position: %d", (int) railPosition));
                    if (motor.getCurrentPosition() > railPosition) motor.setPower(0.4);
                    else motor.setPower(0.8);})),
            new TargetedMotor(EXTEND_IN, EXTEND_OUT, () -> extendPosition, a -> extendPosition = a, a -> robot.clawExtend.setPosition(a))
    };

    // Code to run ONCE when the driver hits INIT
    @Override
    public void init() {
        robot.init(hardwareMap);
        // Send telemetry message to signify robot waiting
        telemetry.addData("Say", "Hello thomas");

        robot.railMotors.apply((dcMotor -> {
            dcMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            dcMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            dcMotor.setTargetPosition((int) RAIL_MIN);
        }));
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
        railPosition += (gamepad2.right_trigger - gamepad2.left_trigger) * 20.0f;
        // Handled by targetedMotors
    }

    @SuppressLint("DefaultLocale")
    public void pinch() {
        telemetry.addLine(String.format("Target CLAW Position: %f", pinchPosition));
//        robot.clawPinch.setPosition(pinchPosition);
        telemetry.addLine(String.format("Current CLAW Position: %f", robot.clawPinch.getPosition()));
    }

    @SuppressLint("DefaultLocale")
    public void extend() {
//
        if (gamepad2.dpad_up) extendPosition -= 2.5;
        if (gamepad2.dpad_down) extendPosition += 2.5;


        extendPosition = Math.min(Math.max(extendPosition, EXTEND_IN), EXTEND_OUT);
        robot.clawExtend.setPosition(extendPosition);

        telemetry.addLine(String.format("Target EXTEND Position: %f", extendPosition));
        telemetry.addLine(String.format("Current EXTEND Position: %f", robot.clawExtend.getPosition()));
    }

    @SuppressLint("DefaultLocale")
    public void pivot() {

        pivotState = Math.min(Math.max(pivotState, 0), 1);

        robot.clawPivot.setPosition(new double[]{PIVOT_BACK, PIVOT_DOWN, PIVOT_FLOAT}[pivotState]);
    }


    // Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {

        ButtonAction.doActions(buttonActions);
        TargetedMotor.runArray(targetedMotors);

        // Final Robot Instructions
        wheels();
        rail();
//        dump();
        extend();
        pinch();
        pivot();

        telemetry.update();
    }

    // Code to run ONCE after the driver hits STOP
    @Override
    public void stop() {
    }
}
