package view;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import viewModel.ViewModelController;
import java.util.Observable;
import java.util.Observer;
import view_AttributesList.AttributesList;
import view_Graphs.Graphs;
import view_Graphs.GraphsController;
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
    //GraphsController graphsController;

    void init(ViewModelController vmc)
    {
        this.vmc=vmc;

        myJoystick.setLayoutX(860);
        myJoystick.setLayoutY(25);
        attributesList.setLayoutX(20);
        attributesList.setLayoutY(25);
        playerButtons.setLayoutY(440);
        playerButtons.setLayoutX(5);
        timeBoard.setLayoutX(500);
        timeBoard.setLayoutY(360);
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


        //graphs.value.bind(attributesList.chosenAttribute);
        graphs.value.bind(vmc.valueAxis);
        graphs.valueCorrelate.bind(vmc.valueCorrelate);
        graphs.timeStamp.bind(vmc.timeStamp);
        graphs.graphSpeed.bind(vmc.choiceSpeed);
        graphs.sizeTS.setValue(vmc.sizeTS.getValue());


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

       // playerButtons.onPlay.addListener((o, ov, nv)->vmc.);

        playerButtons.onPause.addListener((o, ov, nv)->vmc.pause());
//        playerButtons.onSpeed.addListener(nv->vmc.speedPlay());
        playerButtons.onStop.addListener((o, ov, nv)->vmc.stop());
        playerButtons.onRewind.addListener((o, ov, nv)->vmc.rewind());
        playerButtons.onForward.addListener((o, ov, nv)->vmc.forward());

      //  vmc.attributeList.addListener((ListChangeListener) change -> attributesList.lst.addAll(change.getList()));

        attributesList.alc.lv.setItems(vmc.attributeList);
        graphs.selectedAttribute.bind(attributesList.alc.lv.getSelectionModel().selectedItemProperty());
        vmc.chosenAttribute.bind(attributesList.alc.lv.getSelectionModel().selectedItemProperty());

    }

    @Override
    public void update(Observable o, Object arg) {


    }
}
