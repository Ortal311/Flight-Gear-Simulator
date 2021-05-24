package view;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import viewModel.ViewModelController;

import java.util.Observable;
import java.util.Observer;
import view_AttributesList.AttributesList;
import view_PlayerButtons.PlayerButtons;
import view_TimeBoard.TimeBoard;
import view_joystick.MyJoystick;

public class ControllerView extends Pane implements Observer{

    @FXML
    PlayerButtons playerButtons;

    @FXML
    MyJoystick myJoystick;

    @FXML
    AttributesList attributesList;

    @FXML
    TimeBoard timeBoard;
    ViewModelController vmc;

    void init(ViewModelController vmc)
    {
        this.vmc=vmc;
        playerButtons.setVmc(vmc);
        attributesList.setVmc(vmc);
        myJoystick.setVmc(vmc);
        timeBoard.setVmc(vmc);

        myJoystick.setLayoutX(540);
        myJoystick.setLayoutY(25);
        attributesList.setLayoutX(20);
        attributesList.setLayoutY(25);
        playerButtons.setLayoutY(435);
        playerButtons.setLayoutX(5);
        timeBoard.setLayoutX(540);
        timeBoard.setLayoutY(250);

        myJoystick.aileron.bind(vmc.aileron);
        myJoystick.elevators.bind(vmc.elevators);



    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
