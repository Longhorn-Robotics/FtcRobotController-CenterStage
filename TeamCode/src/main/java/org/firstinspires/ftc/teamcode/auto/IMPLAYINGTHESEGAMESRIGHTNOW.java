package org.firstinspires.ftc.teamcode.auto;

import android.annotation.SuppressLint;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

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
        commands = new QuodEratDemonstrandum(robot, () -> true, telemetry);
        commands.init();

        telemetry.addLine("> Ready.");
        telemetry.addLine("> Programming mode [ Gamepad 2 ]");
        telemetry.addLine("> D-pad to control");
        telemetry.update();
    }

    @SuppressLint("DefaultLocale")
    public void loop() {
        // Handle reset
        if (gamepad2.x) {
            driveX = 0;
            driveY = 0;
            previousRailHeight = railHeight;
        }

        movementIncrement = gamepad2.right_bumper ? 50 : 10;

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
        if (driveX != 0) telemetry.addLine(String.format("--> commands.driveX(%d)", driveX));
        if (driveY != 0) telemetry.addLine(String.format("--> commands.driveY(%d)", driveY));
        if (railHeight != previousRailHeight) telemetry.addLine(String.format("--> commands.railHeight(%d)",(int) railHeight));

        telemetry.addLine("\n[ X button on the controller to reset ]");
        telemetry.update();
    }
}
