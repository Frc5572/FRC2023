package org.frc5572.robotools.checks;

import java.util.HashSet;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import org.frc5572.robotools.CompilationData;

/** Performs checks to see that Subsystems do not contain raw IO types. */
public class IOCheck implements Check {

    private static String[] ioInterfaces = {

        // WPILib types
        "edu.wpi.first.wpilibj.interfaces.Gyro", "edu.wpi.first.wpilibj.interfaces.Accelerometer",
        "edu.wpi.first.wpilibj.motorcontrol.MotorController",

        // REV types
        "com.revrobotics.MotorFeedbackSensor",

    };

    private static String[] ioClasses = {

        // WPILib types
        "edu.wpi.first.wpilibj.AddressableLED", "edu.wpi.first.wpilibj.AnalogEncoder",
        "edu.wpi.first.wpilibj.AnalogGyro", "edu.wpi.first.wpilibj.AnalogInput",
        "edu.wpi.first.wpilibj.AnalogOutput", "edu.wpi.first.wpilibj.AnalogPotentiometer",
        "edu.wpi.first.wpilibj.AnalogTrigger", "edu.wpi.first.wpilibj.AnalogTriggerOutput",
        "edu.wpi.first.wpilibj.DMA", "edu.wpi.first.wpilibj.DigitalSource",
        "edu.wpi.first.wpilibj.DoubleSolenoid", "edu.wpi.first.wpilibj.Encoder",
        "edu.wpi.first.wpilibj.I2C", "edu.wpi.first.wpilibj.PWM",
        "edu.wpi.first.wpilibj.PneumaticsBase", "edu.wpi.first.wpilibj.PowerDistribution",
        "edu.wpi.first.wpilibj.Relay", "edu.wpi.first.wpilibj.SPI",
        "edu.wpi.first.wpilibj.SerialPort", "edu.wpi.first.wpilibj.Solenoid",

        // CTRE types
        "com.ctre.phoenix.motorcontrol.can.TalonFX", "com.ctre.phoenix.sensors.CANCoder",

    };

    private void checkType(CompilationData data, Element warnPos, String path, TypeElement elem,
        Set<String> skip) {
        if (skip.contains(elem.getQualifiedName().toString())) {
            return;
        }
        skip.add(elem.getQualifiedName().toString());
        for (String iface : ioInterfaces) {
            if (data.implementsInterface(elem, iface)) {
                String[] parts = iface.split("\\.");
                data.warn(warnPos, path + " is a " + parts[parts.length - 1]
                    + " which performs IO. It should be in an IO class!");
                return;
            }
        }
        for (String clazz : ioClasses) {
            if (data.extendsClass(elem, clazz)) {
                String[] parts = clazz.split("\\.");
                data.warn(warnPos, path + " is a " + parts[parts.length - 1]
                    + " which performs IO. It should be in an IO class!");
                return;
            }
        }
        for (var item : elem.getEnclosedElements()) {
            if (item instanceof VariableElement) {
                VariableElement variable = (VariableElement) item;
                String path2 = new String(path);
                TypeMirror tm = variable.asType();
                while (tm instanceof ArrayType) {
                    tm = ((ArrayType) tm).getComponentType();
                    path2 += "[*]";
                }
                Element e = data.types.asElement(tm);
                if (e instanceof TypeElement) {
                    TypeElement type = (TypeElement) e;
                    checkType(data, warnPos, path2 + "." + variable.getSimpleName().toString(),
                        type, skip);
                }
            }
        }
    }

    /** Perform check. */
    @Override
    public boolean check(CompilationData data) {
        if (data.implementsInterface("edu.wpi.first.wpilibj2.command.Subsystem")) {
            for (var item : data.element.getEnclosedElements()) {
                if (item instanceof VariableElement) {
                    VariableElement variable = (VariableElement) item;
                    String path = variable.getSimpleName().toString();
                    TypeMirror tm = variable.asType();
                    while (tm instanceof ArrayType) {
                        tm = ((ArrayType) tm).getComponentType();
                        path += "[*]";
                    }
                    Element e = data.types.asElement(tm);
                    if (e instanceof TypeElement) {
                        TypeElement type = (TypeElement) e;
                        Set<String> skip = new HashSet<>();
                        checkType(data, item, path, type, skip);
                    }
                }
            }
        }
        return false;
    }

}
