// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Input;

import java.util.function.BooleanSupplier;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.CustomTypes.Math.Vector2;

public class Input extends SubsystemBase {
  Joystick driveJoystick;
  XboxController controller;
  double controllerSpeed;
  public boolean usingJoystick;

  Vector2 rawJoystickInput = Vector2.zero;
  double rawJoystickTwist = 0;

  Vector2 joystickInput = Vector2.zero;
  double joystickTwist = 0;

  Vector2 rawControllerInput = Vector2.zero;
  double rawControllerTurn = 0;

  Vector2 controllerInput = Vector2.zero;
  double controllerTurn = 0;

  public BooleanSupplier receivingJoystickInput = new BooleanSupplier() {
    public boolean getAsBoolean() { return joystickInput.x != 0 || joystickInput.y != 0; }
  };

  public static double Throttle;

  public Input(GenericHID driveInput) {
    if (driveInput instanceof Joystick) {
      driveJoystick = (Joystick) driveInput;
      usingJoystick = true;
    } else {
      controller = (XboxController) driveInput;
      usingJoystick = false;
      controllerSpeed = 0.5;
    }
  }

  @Override
  public void periodic() {
    if (usingJoystick) {
      getRawJoystickInput();
      calculateJoystickInput();
    } else {
      getRawControllerInput();
      calculateControllerInput();
    }
  }

  void getRawJoystickInput()
  {
    rawJoystickInput = new Vector2(-driveJoystick.getY(), -driveJoystick.getX());
    rawJoystickTwist = -driveJoystick.getTwist();

    Throttle = (driveJoystick.getThrottle() / -2) + .5;
  }

  void calculateJoystickInput()
  {
    joystickInput = new Vector2(calculateInputWithDeadzone(rawJoystickInput.x, Constants.ControllerConstants.DEADZONE_DRIVE), calculateInputWithDeadzone(rawJoystickInput.y, Constants.ControllerConstants.DEADZONE_DRIVE));
    joystickTwist = calculateInputWithDeadzone(rawJoystickTwist, Constants.ControllerConstants.DEADZONE_STEER);
  }

  void getRawControllerInput() {
    rawControllerInput = new Vector2(-controller.getLeftY(), -controller.getLeftX());
    rawControllerTurn = -controller.getRightX();
  }

  void calculateControllerInput() {
    controllerInput = new Vector2(calculateInputWithDeadzone(rawControllerInput.x, Constants.ControllerConstants.DEADZONE_DRIVE), calculateInputWithDeadzone(rawControllerInput.y, Constants.ControllerConstants.DEADZONE_DRIVE));
    controllerTurn = calculateInputWithDeadzone(rawControllerTurn, Constants.ControllerConstants.DEADZONE_STEER);
  }

  public double getThrottle() {
    return driveJoystick.getThrottle();
  }

  public void incrementControllerSpeed() {
    if (controllerSpeed + 0.1 <= 1) { controllerSpeed += 0.1; }
  }

  public void decrementControllerSpeed() {
    if (controllerSpeed - 0.1 >= 0) { controllerSpeed -= 0.1; }
  }

  public double getControllerSpeed() {
    return controllerSpeed;
  }

  public Vector2 JoystickInput() { return joystickInput; }
  public double JoystickTwist() { return joystickTwist; }
  public Vector2 ControllerInput() {return controllerInput; }
  public double ControllerTurn() {return controllerTurn; }

  public double calculateInputWithDeadzone(double input, double deadZone) {
    if (Math.abs(input) < deadZone) {
        return 0;
    }

    if (input > 0) {
        return (input - deadZone) / (1 - deadZone);
    } else if (input < 0) {
        return (input + deadZone) / (1 - deadZone);
    }
    return 0;
}
}
