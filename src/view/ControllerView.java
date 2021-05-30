package view;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import viewModel.ViewModelController;
import java.util.Observable;
import java.util.Observer;
import view_AttributesList.AttributesList;
import view_Graphs.Graphs;
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

    @FXML
    Graphs graphs;

    void init(ViewModelController vmc)
    {
        this.vmc=vmc;

        myJoystick.setLayoutX(540);
        myJoystick.setLayoutY(25);
        attributesList.setLayoutX(20);
        attributesList.setLayoutY(25);
        playerButtons.setLayoutY(440);
        playerButtons.setLayoutX(5);
        timeBoard.setLayoutX(85);
        timeBoard.setLayoutY(307);
        graphs.setLayoutX(230);
        graphs.setLayoutY(25);


        myJoystick.aileron.bind(vmc.aileron);
        myJoystick.elevators.bind(vmc.elevators);
        myJoystick.rudder.bind(vmc.rudder);
        myJoystick.throttle.bind(vmc.throttle);

        playerButtons.timeFlight.bind(vmc.timeFlight);
        playerButtons.miliSec.bind(vmc.clock.miliSec.asString());
        playerButtons.seconds.bind(vmc.clock.seconds.asString());
        playerButtons.minutes.bind(vmc.clock.minutes.asString());
        //playerButtons.sliderTime.bind(vmc.sliderTime);
        playerButtons.sliderTime.bindBidirectional(vmc.sliderTime);
        vmc.choiceSpeed.bind(playerButtons.choiceSpeed);

        timeBoard.airSpeed.bind(vmc.airSpeed);
        timeBoard.altimeter.bind(vmc.altimeter);
        timeBoard.fd.bind(vmc.fd);
//        timeBoard.pitch.bind(vmc.pitch);
//        timeBoard.roll.bind(vmc.roll);
//        timeBoard.yaw.bind(vmc.yaw);

//        timeBoard.xAirSpeed.bind(vmc.airSpeed1);
//        timeBoard.yAirSpeed.bind(vmc.airSpeed1);
        timeBoard.xPitch.bind(vmc.pitch1);
        timeBoard.yPitch.bind(vmc.pitch1);
        timeBoard.xRoll.bind(vmc.roll1);
        timeBoard.yRoll.bind(vmc.roll1);
        timeBoard.xYaw.bind(vmc.yaw1);
        timeBoard.yYaw.bind(vmc.yaw1);

        playerButtons.onOpen.addListener((o, ov, nv)->vmc.openFile());
        playerButtons.onOpenXML.addListener((o, ov, nv)->vmc.openXMLFile());
        playerButtons.onPlay.addListener((o, ov, nv)->vmc.play());
        playerButtons.onPause.addListener((o, ov, nv)->vmc.pause());
//        playerButtons.onSpeed.addListener(nv->vmc.speedPlay());
        playerButtons.onStop.addListener((o, ov, nv)->vmc.stop());
        playerButtons.onRewind.addListener((o, ov, nv)->vmc.rewind());
        playerButtons.onForward.addListener((o, ov, nv)->vmc.forward());

        vmc.attributeList.addListener((ListChangeListener) change -> attributesList.lst.addAll(change.getList()));
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
