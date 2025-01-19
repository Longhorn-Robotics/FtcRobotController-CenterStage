package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.QuodEratDemonstrandum;
import org.firstinspires.ftc.teamcode.hardware.RobotHardwareSIGMA;

@Autonomous(name="I'VE PLAYED THESE GAMES BEFORE", group="Robot")
public class IVEPLAYEDTHESEGAMESBEFORE extends LinearOpMode {

    final private RobotHardwareSIGMA robot = new RobotHardwareSIGMA();
    final private ElapsedTime runtime = new ElapsedTime();
    QuodEratDemonstrandum commands;

    @Override
    public void runOpMode() {
        // Init
        robot.init(hardwareMap);
        commands = new QuodEratDemonstrandum(robot, () -> opModeIsActive(), telemetry);
        commands.init();
        telemetry.addLine("> Ready?");
        telemetry.update();

        // Wait for autonomous to start
        waitForStart();
        telemetry.addLine("> Running...");
        telemetry.update();

        // Position in front of submersible and raise linear rails
        commands.driveY(1050);
        commands.driveX(-620);
        commands.railHeight(1575);
        commands.$setAllTargets();
//        commands.commit();
        sleep(3500);

//        commands.railHeight(1575);
////        robot.railMotors.apply(motor -> {
////            motor.setTargetPosition(1575);
////        });
//        commands.$setAllTargets();
//        commands.commit();
//        sleep(1500);

        // Drive into submersible
        commands.driveY(380);
//        commands.commit();
        commands.$setAllTargets();
        sleep(1000);

        // Lower rails to hook specimen
        commands.railHeight(1150);
//        commands.commit();
        commands.$setAllTargets();
        sleep(1000);

        // Release specimen
        commands.$toggleSpecimen();
//        commands.sleep(100);
        sleep(100);

        // Reverse slightly
        commands.driveY(-350);
//        commands.commit();
        commands.$setAllTargets();
        sleep(500);

        // Lower rails
        commands.railHeight(0);
        commands.commit();

        telemetry.addLine("> Complete.");
        telemetry.update();
    }
}
