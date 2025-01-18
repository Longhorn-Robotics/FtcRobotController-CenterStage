package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.QuodEratDemonstrandum;
import org.firstinspires.ftc.teamcode.hardware.RobotHardwareSIGMA;

@TeleOp(name = "I'M PLAYING THESE GAMES RIGHT NOW", group = "Pushbot")
public class IMPLAYINGTHESEGAMESRIGHTNOW extends OpMode {

    RobotHardwareSIGMA robot = new RobotHardwareSIGMA();
    QuodEratDemonstrandum commands;

    int movementIncrement = 10;

    double previousRailHeight = 0;
    double railHeight = 0;
    int driveX = 0;
    int driveY = 0;

    public void init() {
        robot.init(hardwareMap);
        commands = new QuodEratDemonstrandum(robot, () -> true);
        commands.init();

        telemetry.addLine("> Ready.");
        telemetry.addLine("> Programming mode [ Gamepad 2 ]");
        telemetry.update();
    }

    public void loop() {
        // Handle reset
        if (gamepad2.x) {
            driveX = 0;
            driveY = 0;
            previousRailHeight = railHeight;
        }

        if (gamepad2.dpad_up) {
            commands.driveY(movementIncrement);
            driveY += movementIncrement;
        }
        if (gamepad2.dpad_down) {
            commands.driveY(-movementIncrement);
            driveY -= movementIncrement;
        }
        if (gamepad2.dpad_right) {
            commands.driveX(movementIncrement);
            driveX += movementIncrement;
        }
        if (gamepad2.dpad_left) {
            commands.driveX(-movementIncrement);
            driveX -= movementIncrement;
        }

        railHeight += (gamepad2.right_trigger - gamepad2.left_trigger) * 20.0f;

        commands.railHeight((int) railHeight);
        commands.$setAllTargets();



        // Telemetry

        telemetry.addLine("> Current commands:");

        if (driveX != 0) telemetry.addData("--> commands.driveX(%d)", driveX);
        if (driveY != 0) telemetry.addData("--> commands.driveY(%d)", driveY);
        if (railHeight != previousRailHeight) telemetry.addData("--> commands.railHeight(%d)", (int) railHeight);

        telemetry.addLine("--> (use the d-pad driving commands will start to appear here)");
        telemetry.addLine("\n[ X button on the controller to reset ]");
        telemetry.update();
    }
}
