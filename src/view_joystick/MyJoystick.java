package view_joystick;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import viewModel.ViewModelController;

import java.io.IOException;


public class MyJoystick extends BorderPane {


    public DoubleProperty aileron, elevators;
    MyJoystickController mjc;

    public MyJoystick() {

        super();
        FXMLLoader fxl = new FXMLLoader();
        try {
            BorderPane joy= fxl.load(getClass().getResource("MyJoystick.fxml").openStream());
            mjc= fxl.getController();

            aileron=mjc.aileron;
            elevators=mjc.elevators;
            this.getChildren().add(joy);
        } catch (IOException e) {}

    }

    public void setVmc(ViewModelController vmc) {
        mjc.init(vmc);
    }
}
