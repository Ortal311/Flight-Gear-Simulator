package view_joystick;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Slider;
import javafx.scene.shape.Circle;

public class MyJoystickController {

    @FXML
    Slider rudder;

    @FXML
    Slider throttle;

    @FXML
    Circle joystick;

    public DoubleProperty aileron, elevators;
    private double jx,jy;
    private double mx,my;

    public MyJoystickController() {
        jx=70; jy=80;
        aileron=new SimpleDoubleProperty();
        elevators= new SimpleDoubleProperty();

    }
/*
    public void paint()
    {
        GraphicsContext gc= joystick.getGraphicsContext2D();
        mx= joystick.getWidth()/2;
        my=joystick.getHeight()/2;
        gc.clearRect(0,0,joystick.getWidth(),joystick.getHeight());
        gc.strokeOval(jx-50, jy-50, 100,100);

    }

 */
}
