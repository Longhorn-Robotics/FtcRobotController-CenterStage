package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.function.Consumer;

// Note: Maybe pull this outside?
class GroupMotor {
    DcMotor[] motors;

    public GroupMotor(DcMotor... _motors) {
        motors = _motors;
    }

    public void apply(Consumer<DcMotor> function) {
        for (DcMotor motor : motors)
            function.accept(motor);
    }
}
