package view_PlayerButtons;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import view_joystick.MyJoystickController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerButtons extends AnchorPane {

    DoubleProperty lst;
    public PlayerButtons() {
        super();
        FXMLLoader fxl = new FXMLLoader();
        try {
            AnchorPane buttons= fxl.load(getClass().getResource("PlayerButtons.fxml").openStream());
            PlayerButtonsController pbc= fxl.getController();
            this.getChildren().add(buttons);

        } catch (IOException e) {}
    }
}


