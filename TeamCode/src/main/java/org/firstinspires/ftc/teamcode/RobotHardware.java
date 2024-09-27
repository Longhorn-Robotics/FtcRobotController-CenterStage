package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
    public int zero_position;


    private ElapsedTime period = new ElapsedTime();

    public RobotHardware() {}

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

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


        rail.setDirection(DcMotor.Direction.REVERSE);
        rail.setTargetPosition(0);
        rail.setPower(0.5);
        rail.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rail.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rail.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rail.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        // Set all motors to zero power
        lfDrive.setPower(0);
        lbDrive.setPower(0);
        rfDrive.setPower(0);
        rbDrive.setPower(0);
//        rail.setTargetPosition(zero_position);

        lfDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lbDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rfDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rbDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lfDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lbDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rfDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rbDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
}
