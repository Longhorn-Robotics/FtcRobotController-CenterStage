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
    public DcMotor bucketRail1;
    public DcMotor bucketRail2;
    public Servo bucket1;
    public Servo bucket2;
    public Servo clawExtend;
    public Servo clawPivot;
    public Servo clawPinch;

    DualMotor<DcMotor> railMotors;

    // TODO: Make these scaleRange to the right values, e.t.c.
    DualMotor<Servo> bucketServos;

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
        lfDrive.setDirection(DcMotor.Direction.FORWARD);
        lbDrive.setDirection(DcMotor.Direction.FORWARD);
        rfDrive.setDirection(DcMotor.Direction.FORWARD);
        rbDrive.setDirection(DcMotor.Direction.FORWARD);
        lfDrive.setPower(0);
        lbDrive.setPower(0);
        rfDrive.setPower(0);
        rbDrive.setPower(0);
        lfDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lbDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rfDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rbDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lfDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lbDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rfDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rbDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize linear rail motor to run with encoder
        bucketRail1 = hwMap.get(DcMotor.class, "railRAIL");
        bucketRail2 = hwMap.get(DcMotor.class, "railRAIL");

        railMotors = new DualMotor<>(bucketRail1, bucketRail2);

        railMotors.apply(motor -> {
            motor.setDirection(DcMotor.Direction.REVERSE);
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


        // Initialize claw servos
        bucket1 = hwMap.get(Servo.class, "bucket1");
        bucket2 = hwMap.get(Servo.class, "bucket2");
        bucket1.setDirection(Servo.Direction.FORWARD);
        bucket2.setDirection(Servo.Direction.REVERSE);

        bucketServos = new DualMotor<>(bucket1, bucket2);
    }
}
