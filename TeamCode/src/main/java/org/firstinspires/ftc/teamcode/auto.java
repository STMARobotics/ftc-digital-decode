package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

 import java.util.Timer;

@Autonomous
public class auto extends LinearOpMode {

    public void runOpMode() {
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor rearRight = hardwareMap.get(DcMotor.class, "rearRight");
        DcMotor Launcher = hardwareMap.get(DcMotor.class, "Launcher");
        CRServo RightServo = hardwareMap.get(CRServo.class, "RightServo");
        CRServo LeftServo = hardwareMap.get(CRServo.class,"LeftServo");


        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        rearLeft.setDirection(DcMotorSimple.Direction.REVERSE);


        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        waitForStart();
        while (opModeIsActive()) {
            while (timer.time() < 1) {
            frontLeft.setPower(1);
            frontRight.setPower(1);
            rearRight.setPower(1);
            rearLeft.setPower(1);
            }
        if (timer.time() > 1) {
            frontLeft.setPower(0);
            frontRight.setPower(0);
            rearRight.setPower(0);
            rearLeft.setPower(0);
        }
        }
    }
}
