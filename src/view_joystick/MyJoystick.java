package view_joystick;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import java.io.IOException;


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
