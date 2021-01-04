package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "BigIron", group = "SCC")
public class BigIron extends LinearOpMode
{
    private DcMotor launchMotor;

    @Override
    public void runOpMode() throws InterruptedException
    {
        launchMotor = hardwareMap.dcMotor.get("launchMotor");

        waitForStart();

        while (opModeIsActive()) {
            if (gamepad1.a) {
                launchMotor.setPower(1.0);
            } else if (gamepad1.y) {
                launchMotor.setPower(-1.0);
            } else {
                launchMotor.setPower(0);
            }

            idle();
        }
    }
}