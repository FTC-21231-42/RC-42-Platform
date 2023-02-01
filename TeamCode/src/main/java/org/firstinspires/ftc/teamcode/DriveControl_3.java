package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.RobotMap.*;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "DriveControl_new", group = "1")
public class DriveControl_3 extends OpMode {
    //TODO initialize variables
    //TODO KEEP EVERYTHING IN VARIABLES
    //define variables
    double ovaPowerModify = 1.0;
    double basePower = 0;
    double leftFrontPower = 0, rightFrontPower = 0, leftBotPower = 0, rightBotPower = 0;

    float stickRightX1, stickRightY1, stickLeftX1, stickLeftY1;

    @Override
    public void init() {
        //TODO initialization part
        //init hardware
        initRobot(hardwareMap);

        //update log
        telemetry.addData("Status", "Initialized");
        telemetry.update();

    }

    @Override
    public void loop() {
        //TODO: Basic Mechanic Wheel Driving Program
        //TODO: this part is looping as running
        //get the input from the game pad
        stickRightX1 = this.gamepad1.right_stick_x;
        stickRightY1 = this.gamepad1.right_stick_y;
        stickLeftX1 = this.gamepad1.left_stick_x;
        stickLeftY1 = this.gamepad1.left_stick_y;

        //use left trigger 1 and right trigger 1 to control the speed, push to set faster
        basePower = (((gamepad1.left_trigger + gamepad1.right_trigger) * 0.325) + 0.35) * ovaPowerModify;
        basePower *= basePower;

        //set the power of motor
        //FINAL POWER = power * modify

        leftFrontPower = (-stickLeftX1 + (stickRightY1 - stickRightX1)) * basePower;
        rightFrontPower = (stickLeftX1 + (stickRightY1 + stickRightX1)) * basePower;
        leftBotPower = (-stickLeftX1 + (stickRightY1 + stickRightX1)) * basePower;
        rightBotPower = (stickLeftX1 + (stickRightY1 - stickRightX1)) * basePower;

        leftFrontMotor.setPower(leftFrontPower);
        rightFrontMotor.setPower(-rightFrontPower);
        leftBotMotor.setPower(leftBotPower);
        rightBotMotor.setPower(-rightBotPower);

        //logging
        telemetry.addData("stickRightX1", stickRightX1);
        telemetry.addData("stickRightY1", stickRightY1);
        telemetry.addData("left_stick_x;", gamepad1.left_stick_x);
        telemetry.addData("left_stick_y;", gamepad1.left_stick_y);
        telemetry.addData("lTrigger", gamepad1.left_trigger);
        telemetry.addData("rTrigger", gamepad1.right_trigger);
        telemetry.addData("bPower", basePower);
        telemetry.update();

    }

    @Override
    public void stop() {
        super.stop();
    }

}
