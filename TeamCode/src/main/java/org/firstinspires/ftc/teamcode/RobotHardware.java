package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class RobotHardware {
    HardwareMap hwMap = null;

    public DcMotor lbDrive;
    public DcMotor lfDrive;
    public DcMotor rbDrive;
    public DcMotor rfDrive;
    public DcMotor rail;

    // For Odometry
    // 35mm omniwheel = 1.37795 inches
    public final double COUNTS_PER_ROTATION = 1024;
    public final double ROTATION_PER_INCH = 1.0 / (1.37795 * Math.PI);
    public final double COUNTS_PER_INCH = COUNTS_PER_ROTATION * ROTATION_PER_INCH; // CPR * RPI
    public DcMotorEx encoderL;
    public DcMotorEx encoderR;

    private ElapsedTime period = new ElapsedTime();

    public RobotHardware() {}

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Initialize Odometry Hardware
        encoderL = hwMap.get(DcMotorEx.class, "encoderL");
        encoderR = hwMap.get(DcMotorEx.class, "encoderR");

        // Define and Initialize Motors
        lfDrive = hwMap.get(DcMotor.class, "motorFL");
        lbDrive = hwMap.get(DcMotor.class, "motorBL");
        rfDrive = hwMap.get(DcMotor.class, "motorFR");
        rbDrive = hwMap.get(DcMotor.class, "motorBR");
        rail = hwMap.get(DcMotor.class, "railRAIL");

        lfDrive.setDirection(DcMotor.Direction.FORWARD);
        lbDrive.setDirection(DcMotor.Direction.FORWARD);
        rfDrive.setDirection(DcMotor.Direction.FORWARD);
        rbDrive.setDirection(DcMotor.Direction.FORWARD);
        rail.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        lfDrive.setPower(0);
        lbDrive.setPower(0);
        rfDrive.setPower(0);
        rbDrive.setPower(0);
        rail.setTargetPosition(0);

        lfDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lbDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rfDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rbDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rail.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        lfDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lbDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rfDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rbDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}
