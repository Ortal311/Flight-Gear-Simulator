package view_joystick;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyJoystick extends BorderPane {


    public DoubleProperty aileron, elevators, rudder, throttle;

    public MyJoystick() {

        super();
        FXMLLoader fxl = new FXMLLoader();
        try {
            BorderPane joy= fxl.load(getClass().getResource("MyJoystick.fxml").openStream());
            MyJoystickController mjc= fxl.getController();

            aileron=mjc.aileron;
            elevators=mjc.elevators;
            rudder=mjc.rudder.valueProperty();
            throttle=mjc.throttle.valueProperty();
            this.getChildren().add(joy);
        } catch (IOException e) {}

    }
}
