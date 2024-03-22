// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.VisionConstants;
import frc.robot.CustomTypes.Math.Vector2;
import frc.robot.commands.AutoCommands;
import frc.robot.commands.AutoOdometryDrive;
import frc.robot.commands.AutoVisionAmpShoot;
import frc.robot.commands.AutoVisionDrive;
import frc.robot.commands.AutoVisionSpeakerShoot;
import frc.robot.commands.AutoVisionStageShoot;
import frc.robot.commands.ClimberCommand;
import frc.robot.commands.IntakePivotCommand;
import frc.robot.commands.IntakeRunCommand;
import frc.robot.commands.ResetClimberCommand;
import frc.robot.commands.RotateAndLogCommand;
import frc.robot.commands.ShooterPivotCommand;
import frc.robot.commands.ShooterRunCommand;
import frc.robot.commands.TeleopJoystickDrive;
import frc.robot.commands.ToggleAutoTarget;
import frc.robot.commands.UseLimelightCommand;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.RGB;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Climber.ClimberMode;
import frc.robot.subsystems.DriveTrain.SwerveDrive;
import frc.robot.subsystems.Input.Input;
import frc.robot.subsystems.Intake.IntakePivotState;
import frc.robot.subsystems.Intake.IntakeRunState;
import frc.robot.subsystems.Shooter.ShooterPivotState;
import frc.robot.subsystems.Shooter.ShooterRunState;
import frc.robot.subsystems.Vision.AutoTargetStateManager;
import frc.robot.subsystems.Vision.Vision;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
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
  Climber climber;
  RGB rgb;

  Joystick driveStick;
  GenericHID buttonBoard;

  Input input;

  TeleopJoystickDrive joyDrive;

  SequentialCommandGroup autoCommands;

  SendableChooser<Command> autoChooser;

  public RobotContainer() {
    swerveDrive = new SwerveDrive();
    shooter = new Shooter();
    intake = new Intake();
    climber = new Climber();
    rgb = new RGB(shooter, intake, climber);

    driveStick = new Joystick(0);
    buttonBoard = new GenericHID(1);
    input = new Input(driveStick);
    vision = new Vision();
    joyDrive = new TeleopJoystickDrive(swerveDrive, driveStick, input, true);

    autoChooser = new SendableChooser<Command>();
    autoChooser.setDefaultOption("None", null);
    autoChooser.addOption("Vision Shoot Turn", AutoVisionSpeakerShoot.ShootAndStopCommand(shooter, swerveDrive, vision, intake));
    autoChooser.addOption("Vision Shoot No Turn", AutoVisionSpeakerShoot.ShootVisionCommandAutoFirstShot(shooter, intake, swerveDrive));
    autoChooser.addOption("4-Note LMR", AutoCommands.leftMiddleRightAutoCommand(swerveDrive, vision, shooter, intake));
    autoChooser.addOption("4-Note RML", AutoCommands.rightMiddleLeftAutoCommand(swerveDrive, vision, shooter, intake));
    autoChooser.addOption("2-Note L", AutoCommands.shootLeftShootAutoCommand(swerveDrive, vision, shooter, intake));
    autoChooser.addOption("2-Note R", AutoCommands.shootRightShootAutoCommand(swerveDrive, vision, shooter, intake));
    autoChooser.addOption("2-Note M", AutoCommands.shootMiddleShootAutoCommand(swerveDrive, vision, shooter, intake));
    autoChooser.addOption("Blue Taxi No Turn", AutoCommands.blueSideOneNoteTaxiNoTurn(swerveDrive, vision, shooter, intake));
    autoChooser.addOption("Red Taxi No Turn", AutoCommands.redSideOneNoteTaxiNoTurn(swerveDrive, vision, shooter, intake));
    autoChooser.addOption("Blue Taxi Turn", AutoCommands.blueSideOneNoteTaxiTurn(swerveDrive, vision, shooter, intake));
    autoChooser.addOption("Red Taxi Turn", AutoCommands.redSideOneNoteTaxiTurn(swerveDrive, vision, shooter, intake));
    SmartDashboard.putData(autoChooser);

    configureBindings();
  }

  private void configureBindings() {
    swerveDrive.setDefaultCommand(joyDrive);
    new JoystickButton(driveStick, 8).onTrue(new InstantCommand(swerveDrive::resetPigeon, swerveDrive));

    //.onTrue() calls command once per button press
    //.whileTrue() calls command while button is held or until command finishes
    //.toggleOnTrue() makes a toggle which runs when pressed and then stops when pressed again


    // new JoystickButton(driveStick, 6).onTrue(new IntakePivotCommand(intake, IntakePivotState.IN));
    // new JoystickButton(driveStick, 5).onTrue(new IntakePivotCommand(intake, IntakePivotState.OUT));

    // new JoystickButton(driveStick, 1).onTrue(new ParallelCommandGroup(new ShooterPivotCommand(shooter, ShooterPivotState.DEFAULT_SHOOT), new ShooterRunCommand(shooter, ShooterRunState.SHOOT)));
    // new JoystickButton(driveStick, 2).onTrue(new ParallelCommandGroup(new ShooterPivotCommand(shooter, ShooterPivotState.START), new ShooterRunCommand(shooter, ShooterRunState.NONE)));


    // new JoystickButton(driveStick, 9).onTrue(new ClimberCommand(climber, ClimberMode.EXTEND));
    // new JoystickButton(driveStick, 10).onTrue(new ClimberCommand(climber, ClimberMode.RETRACT));
    // new JoystickButton(driveStick, 8).whileTrue(new ClimberCommand(climber, ClimberMode.RESET));
  
    // Test Buttons

    new JoystickButton(driveStick, 5).onTrue(new SequentialCommandGroup(new IntakePivotCommand(intake, IntakePivotState.IN), new WaitCommand(0.5), new IntakeRunCommand(intake, IntakeRunState.NONE)));
    new JoystickButton(driveStick, 6).onTrue(new SequentialCommandGroup(new IntakePivotCommand(intake, IntakePivotState.OUT), new WaitCommand(0.5), new IntakeRunCommand(intake, IntakeRunState.INTAKE)));

    new JoystickButton(buttonBoard, 6).onTrue(new SequentialCommandGroup(new IntakeRunCommand(intake, IntakeRunState.OUTTAKE), new WaitCommand(0.5), new IntakeRunCommand(intake, IntakeRunState.NONE)));
    new JoystickButton(driveStick, 4).onTrue(new IntakeRunCommand(intake, IntakeRunState.NONE));

    new JoystickButton(buttonBoard,9).onTrue(new ShooterRunCommand(shooter, ShooterRunState.NONE));
    new JoystickButton(buttonBoard, 11).onTrue(new ShooterRunCommand(shooter, ShooterRunState.SHOOT));
    new JoystickButton(buttonBoard, 7).onTrue(new ShooterRunCommand(shooter, ShooterRunState.AMP));


    new JoystickButton(buttonBoard, 5).onTrue(new ShooterPivotCommand(shooter, ShooterPivotState.SHOOTER_45));
    new JoystickButton(buttonBoard, 3).onTrue(new ShooterPivotCommand(shooter, ShooterPivotState.DEFAULT_SHOOT));
    new JoystickButton(buttonBoard, 8).onTrue(new SequentialCommandGroup(new IntakePivotCommand(intake, IntakePivotState.OUT), new WaitCommand(0.5), new ShooterPivotCommand(shooter, ShooterPivotState.CLIMBING_POS)));
    new JoystickButton(buttonBoard, 4).onTrue(new ShooterPivotCommand(shooter, ShooterPivotState.AMP));
  
    new JoystickButton(driveStick, 1).onTrue(AutoVisionSpeakerShoot.ShootAndStopCommand(shooter, swerveDrive, vision, intake));
    new JoystickButton(driveStick, 11).onTrue(AutoVisionAmpShoot.GetCommand(swerveDrive, vision, shooter, intake).until(input.receivingJoystickInput));
    //new JoystickButton(driveStick, 11).onTrue(AutoVisionStageShoot.getCommand(shooter, intake, vision, swerveDrive).until(input.receivingJoystickInput));

    new JoystickButton(buttonBoard, 12).whileTrue(new ClimberCommand(climber, ClimberMode.EXTEND));
    new JoystickButton(buttonBoard, 10).whileTrue(new ClimberCommand(climber, ClimberMode.RETRACT));
    new JoystickButton(driveStick, 12).whileTrue(new ResetClimberCommand(climber));

    new JoystickButton(driveStick, 9).onTrue(new UseLimelightCommand(true));
    new JoystickButton(driveStick, 10).onTrue(new UseLimelightCommand(false));

    //new JoystickButton(driveStick, 2).onTrue(new RotateAndLogCommand(vision, swerveDrive));

    //new JoystickButton(driveStick, 2).onTrue(new ToggleAutoTarget());
  }

  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
