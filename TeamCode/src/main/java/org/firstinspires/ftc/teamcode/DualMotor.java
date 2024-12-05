package org.firstinspires.ftc.teamcode;

import java.util.function.Consumer;

class DualMotor<MotorType> {
    MotorType motor1;
    MotorType motor2;

    public DualMotor(MotorType _motor1, MotorType _motor2) {
        motor1 = _motor1;
        motor2 = _motor2;
    }

    public void apply(Consumer<MotorType> function) {
        function.accept(motor1);
        function.accept(motor2);
    }
}
