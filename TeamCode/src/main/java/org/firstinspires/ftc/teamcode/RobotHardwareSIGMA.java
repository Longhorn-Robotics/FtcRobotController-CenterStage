package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
// import com.qualcomm.robotcore.util.ElapsedTime;

public class RobotHardwareSIGMA {
    HardwareMap hwMap;
    public DcMotor lbDrive;
    public DcMotor lfDrive;
    public DcMotor rbDrive;
    public DcMotor rfDrive;
    public DcMotor bucketRailL;
    public DcMotor bucketRailR;
    public Servo bucket;
    public Servo clawExtend;
    public Servo clawPivot;
    public Servo clawPinch;
    public Servo specimenGrabber;

    GroupMotor railMotors;
    GroupMotor wheels;

//    private ElapsedTime period = new ElapsedTime();

    public RobotHardwareSIGMA() {}

    public void init(HardwareMap ahwMap) {
        // Save reference to hardware map
        hwMap = ahwMap;

        // Initialize drive motors
        lfDrive = hwMap.get(DcMotor.class, "motorFL");
        lbDrive = hwMap.get(DcMotor.class, "motorBL");
        rfDrive = hwMap.get(DcMotor.class, "motorFR");
        rbDrive = hwMap.get(DcMotor.class, "motorBR");
        wheels = new GroupMotor(lfDrive, lbDrive, rfDrive, rbDrive);

        wheels.apply(dcMotor -> {
            dcMotor.setDirection(DcMotor.Direction.FORWARD);
            dcMotor.setPower(0);
            dcMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            dcMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        });

        // Initialize linear rail motor to run with encoder
        bucketRailL = hwMap.get(DcMotor.class, "railL");
        bucketRailR = hwMap.get(DcMotor.class, "railR");
        bucketRailL.setDirection(DcMotor.Direction.FORWARD);
        bucketRailR.setDirection(DcMotor.Direction.REVERSE);

        railMotors = new GroupMotor(bucketRailL, bucketRailR);

        railMotors.apply((DcMotor motor) -> {
            motor.setTargetPosition(0);
            motor.setPower(0.8);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        });


//        bucket = hwMap.get(Servo.class, "bucketServo");

        // Initialize claw servos
        clawExtend = hwMap.get(Servo.class, "extendEXTEND");
        clawPivot = hwMap.get(Servo.class, "pivotPIVOT");
        clawPinch = hwMap.get(Servo.class, "pinchPINCH");

        bucket = hwMap.get(Servo.class, "bucketL");

        specimenGrabber = hwMap.get(Servo.class, "specimenSPECIMEN");
    }
}
