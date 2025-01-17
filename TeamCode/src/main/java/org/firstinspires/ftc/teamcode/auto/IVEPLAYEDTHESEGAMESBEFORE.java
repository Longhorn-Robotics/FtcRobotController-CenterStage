package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.QuodEratDemonstrandum;
import org.firstinspires.ftc.teamcode.RobotHardwareSIGMA;

@Autonomous(name="I'VE PLAYED THESE GAMES BEFORE", group="Robot")
public class IVEPLAYEDTHESEGAMESBEFORE extends LinearOpMode {

    private RobotHardwareSIGMA robot = new RobotHardwareSIGMA();
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        // Init
        robot.init(hardwareMap);
        QuodEratDemonstrandum commands = new QuodEratDemonstrandum(robot, () -> opModeIsActive());
        commands.init();
        telemetry.addLine("> Ready.");
        telemetry.update();

        // Wait for autonomous to start
        waitForStart();
        telemetry.addLine("> Running...");
        telemetry.update();

        // Position in front of submersible and raise linear rails
        commands.driveY(50);
        commands.driveX(-800);
        commands.railHeight(2000);
        commands.commit();

        // Drive into submersible
        commands.driveY(300);
        commands.commit();

        // Lower rails to hook specimen
        commands.railHeight(1800);
        commands.commit();

        // Release specimen
        commands.$toggleSpecimen();
        commands.sleep(100);

        // Reverse slightly
        commands.driveY(-50);
        commands.commit();

        // Lower rails
        commands.railHeight(0);
        commands.commit();

        telemetry.addLine("> Complete.");
        telemetry.update();
    }
}
