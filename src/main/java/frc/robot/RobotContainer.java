// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.commands.AutoIntake;
import frc.robot.commands.ClimberCommand;
import frc.robot.commands.IntakePivotCommand;
//import frc.robot.commands.IntakeRunCommand;
import frc.robot.commands.TeleopJoystickDrive;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Climber.ClimberMode;
import frc.robot.subsystems.DriveTrain.SwerveDrive;
import frc.robot.subsystems.Input.Input;
import frc.robot.subsystems.Intake.IntakePivotState;
import frc.robot.subsystems.Vision.Vision;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
//import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  
  SwerveDrive swerveDrive;
  Shooter shooter;
  Intake intake;
  Vision vision;

  Joystick driveStick;
  GenericHID buttonBoard;

  Input input;

  TeleopJoystickDrive joyDrive;

  AutoIntake autoIntake;
  
  private Climber climber;

  SequentialCommandGroup autoCommands;
  public RobotContainer() {
    //swerveDrive = new SwerveDrive();
    //shooter = new Shooter();
    intake = new Intake(shooter);

    driveStick = new Joystick(0);
    //buttonBoard = new GenericHID(1);
    input = new Input(driveStick);
    vision = new Vision();
    climber = new Climber();
    //joyDrive = new TeleopJoystickDrive(swerveDrive, driveStick, input, true);
    //intakeIdle = new IntakeCommand(intake, IntakeState.IDLE);
    //intakeRest = new IntakeCommand(intake, IntakeState.REST);
    //intakeIntake = new IntakeCommand(intake, IntakeState.INTAKE);
    //intakeEject = new IntakeCommand(intake, IntakeState.EJECT);
    //intakeReady = new IntakeCommand(intake, IntakeState.READY_TO_FEED);
    //intakeFeed = new IntakeCommand(intake, IntakeState.FEED_SHOOTER);

    //autoIntake = new AutoIntake(intake);

    // shooterStart = new ShooterCommand(shooter, ShooterState.START);
    // shooterIdle = new ShooterCommand(shooter, ShooterState.IDLE);
    // shooterFire = new ShooterCommand(shooter, ShooterState.VISION_SHOOT);
    // shooterAmp = new ShooterCommand(shooter, ShooterState.AMP);

    configureBindings();

    //autoCommands = new SequentialCommandGroup(null);
  }

  private void configureBindings() {
    //swerveDrive.setDefaultCommand(joyDrive);
    //intake.setDefaultCommand(autoIntake);

    //.onTrue() calls command once per button press
    //.whileTrue() calls command while button is held or until command finishes
    //.toggleOnTrue() makes a toggle which runs when pressed and then stops when presse again

    //new JoystickButton(driveStick, 3).onTrue(new ShooterCommand(shooter, ShooterState.START));
    //new JoystickButton(buttonBoard, 3).onTrue(new RepeatCommand(shooterIdle));

    //new JoystickButton(buttonBoard, 4).onTrue(new RepeatCommand(intakeIdle));
    new JoystickButton(driveStick, 6).onTrue(new IntakePivotCommand(intake, IntakePivotState.IN));//.until(intake.autoIntake));
    new JoystickButton(driveStick, 5).onTrue(new IntakePivotCommand(intake, IntakePivotState.OUT));
    //new JoystickButton(buttonBoard, 7).onTrue(new RepeatCommand(intakeFeed));
    new JoystickButton(driveStick, 9).onTrue(new ClimberCommand(climber, ClimberMode.EXTEND));
    new JoystickButton(driveStick, 10).onTrue(new ClimberCommand(climber, ClimberMode.RETRACT));
    new JoystickButton(driveStick, 8).whileTrue(new ClimberCommand(climber, ClimberMode.RESET));

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    
    return null;
  }
}
