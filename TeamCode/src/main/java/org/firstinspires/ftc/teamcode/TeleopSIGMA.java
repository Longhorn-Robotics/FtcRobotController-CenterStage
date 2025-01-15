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
    static final double PINCH_OPEN = 0.18f;
    static final double PINCH_CLOSED = 0.28f;
    static final double EXTEND_IN = 0.42f;
    static final double EXTEND_OUT = 0.12f;
    static final double PIVOT_DOWN = 0.04f;
    static final double PIVOT_FLOAT = 0.20f;
    static final double PIVOT_BACK = 0.90f;
    // Tipped: 0.82
    // Hold: 0.68
    // Load: 0.54
    static final double BUCKET_DUMP = 0.82f;
    static final double BUCKET_HOLD = 0.82f;
    static final double BUCKET_PICK = 0.54f;
    static final double[] bucketPositions = new double[]{BUCKET_PICK, BUCKET_HOLD, BUCKET_DUMP};

    /* Declare OpMode members. */
    RobotHardwareSIGMA robot = new RobotHardwareSIGMA();
    double railPosition = RAIL_MIN;
    double extendPosition = EXTEND_IN;
    double pinchPosition = PINCH_OPEN;

    int pivotState = 0;
    int bucketState = 0;

    boolean slowmode = false;
    boolean clawOpen = true;

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
            new ButtonAction(() -> gamepad2.dpad_right, () -> pivotState--),
            new ButtonAction(() -> gamepad2.square, () -> {
                if (bucketState != 0) bucketState = 2;
                else bucketState = 0;
            }),
            new ButtonAction(() -> gamepad2.right_bumper, () -> {
                extendPosition = EXTEND_IN;
                pivotState = 2;
            }),
            new ButtonAction(() -> gamepad2.left_bumper, () -> {
                clawOpen = true;

                Utils.setTimeout(() -> {
                    extendPosition = EXTEND_IN - 0.3;

                    Utils.setTimeout(() -> {
                        bucketState = 1;
                    }, 200);
                }, 200);
            }),
    };

    // Another cool functional programming interface
    // This time for the common pattern of targeted motors

    @SuppressLint("DefaultLocale")
    private final TargetedMotor[] targetedMotors = {
            new TargetedMotor(RAIL_MIN, RAIL_MAX, () -> railPosition, a -> railPosition = a, a -> {
                robot.railMotors.apply(motor -> {
//                motor.setTargetPosition((int) railPosition);
                    if (motor.getCurrentPosition() > railPosition) motor.setPower(0.4);
                    else motor.setPower(0.8);
                });
                robot.bucketRailL.setTargetPosition((int) railPosition);
                robot.bucketRailR.setTargetPosition((int) railPosition + 20);
            }),
//            new TargetedMotor(EXTEND_OUT, EXTEND_IN, () -> extendPosition, a -> extendPosition = a, a -> robot.clawExtend.setPosition(a))
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
        double joystickMultiplier = !gamepad1.right_bumper ? 1.0f : 0.25f;


        final_throttle += (gamepad1.left_stick_y * joystickMultiplier) + (gamepad2.left_stick_y * 0.2);
        final_strafe += (gamepad1.left_stick_x * joystickMultiplier) + (gamepad2.left_stick_x * 0.2);
        final_yaw += (gamepad1.right_stick_x * joystickMultiplier) + (gamepad2.right_stick_x * 0.2);

        robot.lfDrive.setPower(final_throttle - final_strafe - final_yaw);
        robot.lbDrive.setPower(final_throttle + final_strafe - final_yaw);
        robot.rfDrive.setPower(-final_throttle - final_strafe - final_yaw);
        robot.rbDrive.setPower(-final_throttle + final_strafe - final_yaw);
    }

    @SuppressLint("DefaultLocale")
    public void rail() {
        // Handled by targetedMotors
        railPosition += (gamepad2.right_trigger - gamepad2.left_trigger) * 20.0f;
        telemetry.addLine(String.format("Target RAIL Position: %d", (int) railPosition));
    }

    @SuppressLint("DefaultLocale")
    public void pinch() {
//        if (gamepad1.dpad_up) pinchPosition -= 0.005;
//        if (gamepad1.dpad_down) pinchPosition += 0.005;

//        telemetry.addLine(String.format("Target CLAW Position: %f", pinchPosition));
//        robot.clawPinch.setPosition(pinchPosition);
        telemetry.addLine(String.format("Current CLAW Position: %f", robot.clawPinch.getPosition()));
    }


    @SuppressLint("DefaultLocale")
    public void extend() {
        if (gamepad2.dpad_up) extendPosition -= 0.002;
        if (gamepad2.dpad_down) extendPosition += 0.002;

        extendPosition = Math.min(Math.max(extendPosition, EXTEND_OUT), EXTEND_IN);
        robot.clawExtend.setPosition(extendPosition);

        telemetry.addLine(String.format("Target EXTEND Position: %f", extendPosition));
        telemetry.addLine(String.format("Real EXTEND Position: %f", robot.clawExtend.getPosition()));
    }

    @SuppressLint("DefaultLocale")
    public void pivot() {
        pivotState = Math.min(Math.max(pivotState, 0), 2);

        robot.clawPivot.setPosition(new double[]{PIVOT_DOWN, PIVOT_FLOAT, PIVOT_BACK}[pivotState]);
    }

    @SuppressLint("DefaultLocale")
    public void bucket() {
        robot.bucket.setPosition(bucketPositions[bucketState]);

//        if (gamepad1.dpad_up) bucketPosition += 0.02;
//        if (gamepad1.dpad_down) bucketPosition -= 0.02;

//        robot.bucket.setPosition(bucketPosition);
        telemetry.addLine(String.format("Real Bucket Position: %f", robot.bucket.getPosition()));
    }


    // Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
    @Override
    public void loop() {
        // Final Robot Instructions
        wheels();
        rail();
        bucket();
        extend();
        pinch();
        pivot();

        ButtonAction.doActions(buttonActions);
        TargetedMotor.runArray(targetedMotors);

        telemetry.update();
    }

    // Code to run ONCE after the driver hits STOP
    @Override
    public void stop() {
    }
}
