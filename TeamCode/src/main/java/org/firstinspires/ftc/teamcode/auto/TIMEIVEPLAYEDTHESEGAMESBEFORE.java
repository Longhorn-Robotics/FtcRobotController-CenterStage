package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.QuodEratDemonstrandum;
import org.firstinspires.ftc.teamcode.hardware.RobotHardwareSIGMA;

@Autonomous(name="TIME BASED I'VE PLAYED THESE GAMES BEFORE", group="Robot")
public class TIMEIVEPLAYEDTHESEGAMESBEFORE extends LinearOpMode {

    final private RobotHardwareSIGMA robot = new RobotHardwareSIGMA();
    final private ElapsedTime runtime = new ElapsedTime();

    static final double SPECIMEN_CLOSE = 0.068;
    static final double SPECIMEN_OPEN = 0.452;
    static final double EXTEND_IN = 0.44;
    static final double EXTEND_OUT = 0.12;

    double final_throttle = 0.0f;
    double final_strafe = 0.0f;
    double final_yaw = 0.0f;

    @Override
    public void runOpMode() {
        // Init
        robot.init(hardwareMap);
        telemetry.addLine("> Ready?");
        telemetry.update();

        // Wait for autonomous to start
        waitForStart();
        telemetry.addLine("> Running...");
        telemetry.update();

        final_strafe = -0.5;
//        robot.clawExtend.setPosition(EXTEND_IN + (EXTEND_OUT - EXTEND_IN) * 0.75);
        robot.railMotors.apply(motor -> {
            motor.setTargetPosition(1575);
        });
        $updateMotorPowers();
        sleep(500);

        $resetMotorPowers();

        sleep(500);

        final_throttle = -0.5;
        $updateMotorPowers();
        sleep(1250);

        $resetMotorPowers();
        robot.railMotors.apply(motor -> {
            motor.setTargetPosition(1075);
        });
        sleep(750);

        robot.specimenGrabber.setPosition(SPECIMEN_OPEN);
        final_throttle = -0.3;
        $updateMotorPowers();
        sleep(550);

        $resetMotorPowers();
        robot.railMotors.apply(motor -> {
            motor.setTargetPosition(0);
        });

        sleep(3000);

        telemetry.addLine("> Complete.");
        telemetry.update();
    }

    public void $updateMotorPowers() {
        robot.lfDrive.setPower(final_throttle - final_strafe - final_yaw);
        robot.lbDrive.setPower(final_throttle + final_strafe - final_yaw);
        robot.rfDrive.setPower(-final_throttle - final_strafe - final_yaw);
        robot.rbDrive.setPower(-final_throttle + final_strafe - final_yaw);
    }

    public void $resetMotorPowers() {
        final_throttle = 0;
        final_strafe = 0;
        final_yaw = 0;

        $updateMotorPowers();
    }
}
