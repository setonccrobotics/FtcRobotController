package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "OneMotorDebug", group = "SCC")
public class OneMotorDebug extends LinearOpMode
{
    private DcMotor motor;

    @Override
    public void runOpMode() throws InterruptedException
    {
        motor = hardwareMap.dcMotor.get("motor");

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                motor.setPower(1.0);
            } else if (gamepad1.y) {
                motor.setPower(-1.0);
            } else {
                motor.setPower(0);
            }

            idle();
        }
    }
}