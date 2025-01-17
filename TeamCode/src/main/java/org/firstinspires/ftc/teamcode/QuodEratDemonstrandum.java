package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

// >> Experimental

public class QuodEratDemonstrandum {
    private RobotHardwareSIGMA robot;
    private Callable<Boolean> opModeIsActive;

    private int wheelBL = 0;
    private int wheelFL = 0;
    private int wheelBR = 0;
    private int wheelFR = 0;

    private int railPosition = 0;

    private boolean specimenGrabbing = true;

    public QuodEratDemonstrandum(RobotHardwareSIGMA robot, Callable<Boolean> opModeIsActive) {
        this.robot = robot;
        this.opModeIsActive = opModeIsActive;
    }

    public void driveY(int value) {
        wheelBL += value;
        wheelFL += value;
        wheelBR -= value;
        wheelFR -= value;
    }

    public void driveX(int value) {
        wheelBL += value;
        wheelFL -= value;
        wheelBR += value;
        wheelFR -= value;
    }

    public void rotate(int degrees) {
        wheelBL += (int) (degrees * 0.5);
        wheelFL += (int) (degrees * 0.5);
        wheelBR += (int) (degrees * 0.5);
        wheelFR += (int) (degrees * 0.5);
    }

    public void railHeight(int value) {
        railPosition = value;
    }

    public void $toggleSpecimen() {
        specimenGrabbing = !specimenGrabbing;
        robot.specimenGrabber.setPosition(specimenGrabbing ? 1 : 0);
    }

    public void commit() {
        // set all targets
        robot.lbDrive.setTargetPosition(wheelBL);
        robot.lfDrive.setTargetPosition(wheelFL);
        robot.rbDrive.setTargetPosition(wheelBR);
        robot.rfDrive.setTargetPosition(wheelFR);
        robot.bucketRailL.setTargetPosition(railPosition);
        robot.bucketRailR.setTargetPosition(railPosition);

        try {
            while (
                    opModeIsActive.call() && (
                            Math.abs(robot.lfDrive.getCurrentPosition() - wheelFL) > 5 || Math.abs(robot.lbDrive.getCurrentPosition() - wheelBL) > 5
                            || Math.abs(robot.rbDrive.getCurrentPosition() - wheelBR) > 5 || Math.abs(robot.rfDrive.getCurrentPosition() - wheelFR) > 5
                            || Math.abs(robot.bucketRailL.getCurrentPosition() - railPosition) > 5 || Math.abs(robot.bucketRailR.getCurrentPosition() - railPosition) > 5
                    )
            ) {
                // wait...
            }
        } catch (Exception e) {}

        return;
    }

    public void sleep(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch(InterruptedException e) {}
    }

    public void init() {
        robot.wheels.apply(motor -> {
            motor.setTargetPosition(0);
            motor.setPower(0.5);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        });
    }
}
