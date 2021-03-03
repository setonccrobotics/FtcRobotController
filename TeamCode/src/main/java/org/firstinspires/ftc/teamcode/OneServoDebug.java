package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "OneServoDebug", group = "SCC")
public class OneServoDebug extends LinearOpMode
{
    private Servo servo;
    private Servo servoTwo;

    double tempServoPos = 0.0;

    @Override
    public void runOpMode() throws InterruptedException
    {
        servo = hardwareMap.servo.get("servo");
        servoTwo = hardwareMap.servo.get("servoTwo");

        servo.setPosition(0.0); // Zero
        servoTwo.setPosition(0.0); // Zero

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.y && tempServoPos <= 0.45) {
                // Debug servo
                tempServoPos += 0.05;
            } else if (gamepad1.a && tempServoPos >= 0.05) {
                tempServoPos -= 0.05;
            } else if (gamepad1.b && tempServoPos <= 0.75) {
                // Debug servo
                tempServoPos += 0.25;
            } else if (gamepad1.x && tempServoPos >= 0.75) {
                tempServoPos -= 0.25;
            } else if (gamepad1.dpad_left) {
                tempServoPos = 0.0;
            } else if (gamepad1.dpad_right) {
                tempServoPos = 1.0;
            }
            servo.setPosition(tempServoPos);
            servoTwo.setPosition(tempServoPos);
            sleep(200); //Poorman's debounce
            telemetry.addData("ServoPos", tempServoPos);
            telemetry.update();

            idle();
        }
    }
}