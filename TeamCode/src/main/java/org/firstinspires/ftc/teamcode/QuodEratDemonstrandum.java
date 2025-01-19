package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.RobotHardwareSIGMA;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

// >> Experimental

public class QuodEratDemonstrandum {
    private RobotHardwareSIGMA robot;
    private Callable<Boolean> opModeIsActive;
    private Telemetry telemetry;

    private int wheelBL = 0;
    private int wheelFL = 0;
    private int wheelBR = 0;
    private int wheelFR = 0;

    private int railPosition = 0;

    private boolean specimenGrabbing = true;
    static final double SPECIMEN_CLOSE = 0.068;
    static final double SPECIMEN_OPEN = 0.452;

    public QuodEratDemonstrandum(RobotHardwareSIGMA robot, Callable<Boolean> opModeIsActive, Telemetry telemetry) {
        this.robot = robot;
        this.opModeIsActive = opModeIsActive;
        this.telemetry = telemetry;
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
        robot.specimenGrabber.setPosition(specimenGrabbing ? SPECIMEN_CLOSE : SPECIMEN_OPEN);
    }

    public void $setAllTargets() {
        robot.lbDrive.setTargetPosition(wheelBL);
        robot.lfDrive.setTargetPosition(wheelFL);
        robot.rbDrive.setTargetPosition(wheelBR);
        robot.rfDrive.setTargetPosition(wheelFR);
        robot.bucketRailL.setTargetPosition(railPosition);
        robot.bucketRailR.setTargetPosition(railPosition);
        robot.specimenGrabber.setPosition(specimenGrabbing ? SPECIMEN_CLOSE : SPECIMEN_OPEN);
    }

    public void commit() {
        // set all targets
        $setAllTargets();

        try {
            while (
                    true && (
                            Math.abs(robot.lfDrive.getCurrentPosition() - wheelFL) > 10 || Math.abs(robot.lbDrive.getCurrentPosition() - wheelBL) > 10
                            || Math.abs(robot.rbDrive.getCurrentPosition() - wheelBR) > 10 || Math.abs(robot.rfDrive.getCurrentPosition() - wheelFR) > 10
                            || Math.abs(robot.bucketRailL.getCurrentPosition() - railPosition) > 10
                    )
            ) {
                // wait...

                $setAllTargets();

                telemetry.addLine(String.format("lfDrive: %f", robot.lfDrive.getCurrentPosition()));
                telemetry.addLine(String.format("lbDrive: %f", robot.lbDrive.getCurrentPosition()));
                telemetry.addLine(String.format("rfDrive: %f", robot.rfDrive.getCurrentPosition()));
                telemetry.addLine(String.format("rbDrive: %f", robot.rbDrive.getCurrentPosition()));
                telemetry.update();
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
//            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        });
    }
}
