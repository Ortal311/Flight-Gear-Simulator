package view;

import javafx.collections.ListChangeListener;
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
        myJoystick.rudder.bind(vmc.rudder);
        myJoystick.throttle.bind(vmc.throttle);

        playerButtons.timeFlight.bind(vmc.timeFlight);
        playerButtons.miliSec.bind(vmc.clock.miliSec.asString());
        playerButtons.seconds.bind(vmc.clock.seconds.asString());
        playerButtons.minutes.bind(vmc.clock.minutes.asString());
        playerButtons.sliderTime.bind(vmc.sliderTime);
        vmc.choiceSpeed.bind(playerButtons.choiceSpeed);

        timeBoard.airSpeed.bind(vmc.airSpeed);
        timeBoard.altimeter.bind(vmc.altimeter);
        timeBoard.fd.bind(vmc.fd);
        timeBoard.pitch.bind(vmc.pitch);
        timeBoard.roll.bind(vmc.roll);
        timeBoard.yaw.bind(vmc.yaw);

        playerButtons.onOpen.addListener(nv->vmc.openFile());
        playerButtons.onOpenXML.addListener(nv->vmc.openXMLFile());
        playerButtons.onPlay.addListener(nv->vmc.play());
        playerButtons.onPause.addListener(nv->vmc.pause());
//        playerButtons.onSpeed.addListener(nv->vmc.speedPlay());
        playerButtons.onStop.addListener(nv->vmc.stop());
        playerButtons.onRewind.addListener(nv->vmc.rewind());
        playerButtons.onForward.addListener(nv->vmc.forward());
        playerButtons.onPlus15.addListener(nv->vmc.plus15());
        playerButtons.onMinus15.addListener(nv->vmc.minus15());

        vmc.attributeList.addListener((ListChangeListener) change -> attributesList.lst.addAll(change.getList()));
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
