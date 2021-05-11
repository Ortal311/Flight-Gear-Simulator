package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import viewModel.ViewModelController;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import view_AttributesList.AttributesList;
import view_PlayerButtons.PlayerButtons;
import view_joystick.MyJoystick;

public class ControllerView extends Pane implements Observer{

    @FXML
    PlayerButtons playerButtons;

    @FXML
    MyJoystick myJoystick;

    @FXML
    AttributesList attributesList;


    ViewModelController vmc;

    public ControllerView() {
    }

    void init(ViewModelController vmc)
    {
        myJoystick.setLayoutX(540);
        myJoystick.setLayoutY(25);
        attributesList.setLayoutX(20);
        attributesList.setLayoutY(25);
        playerButtons.setLayoutY(435);
        playerButtons.setLayoutX(5);


        this.vmc=vmc;
        vmc.throttle.bind(myJoystick.throttle);
        vmc.rudder.bind(myJoystick.rudder);
        vmc.aileron.bind(myJoystick.aileron);
        vmc.elevators.bind(myJoystick.elevators);

        myJoystick.throttle.bind(vmc.throttle);
        myJoystick.rudder.bind(vmc.rudder);
        myJoystick.aileron.bind(vmc.aileron);
        myJoystick.elevators.bind(vmc.elevators);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
