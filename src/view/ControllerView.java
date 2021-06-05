package view;

import algo.SimpleAnomalyDetector;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import viewModel.ViewModelController;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import view_AttributesList.AttributesList;
import view_Graphs.Graphs;
import view_Graphs.GraphsController;
import view_PlayerButtons.PlayerButtons;
import view_TimeBoard.TimeBoard;
import view_joystick.MyJoystick;

public class ControllerView extends Pane implements Observer {

    @FXML
    PlayerButtons playerButtons;

    @FXML
    MyJoystick myJoystick;

    @FXML
    AttributesList attributesList;

    @FXML
    TimeBoard timeBoard;

    @FXML
    SimpleAnomalyDetector graphALG;

    @FXML
    Graphs graphs;

    @FXML
    SimpleAnomalyDetector ad;

    ViewModelController vmc;
    //GraphsController graphsController;

    void init(ViewModelController vmc) {
        this.vmc = vmc;

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

        if(Objects.isNull(ad)){
            graphALG.setLayoutX(500);
            graphALG.setLayoutY(200);
        }

        myJoystick.aileron.bind(vmc.aileron);
        myJoystick.elevators.bind(vmc.elevators);
        myJoystick.rudder.bind(vmc.rudder);
        myJoystick.throttle.bind(vmc.throttle);

        playerButtons.timeFlight.bind(vmc.timeFlight);
        playerButtons.miliSec.bind(vmc.clock.miliSec.asString());
        playerButtons.seconds.bind(vmc.clock.seconds.asString());
        playerButtons.minutes.bind(vmc.clock.minutes.asString());
        playerButtons.sliderTime.bindBidirectional(vmc.sliderTime);
        vmc.choiceSpeed.bind(playerButtons.choiceSpeed);


        //graphs.value.bind(attributesList.chosenAttribute);
        graphs.value.bind(vmc.valueAxis);
        graphs.valueCorrelate.bind(vmc.valueCorrelate);
        graphs.timeStamp.bind(vmc.timeStamp);
        graphs.graphSpeed.bind(vmc.choiceSpeed);
        graphs.sizeTS.setValue(vmc.sizeTS.getValue());
        graphs.x1.bind(vmc.x1);
        graphs.x2.bind(vmc.x2);
        graphs.y1.bind(vmc.y1);
        graphs.y2.bind(vmc.y2);


        timeBoard.airSpeed.bind(vmc.airSpeed);
        timeBoard.altimeter.bind(vmc.altimeter);
        timeBoard.fd.bind(vmc.fd);
        timeBoard.xPitch.bind(vmc.pitch);
        timeBoard.yPitch.bind(vmc.pitch);
        timeBoard.xRoll.bind(vmc.roll);
        timeBoard.yRoll.bind(vmc.roll);
        timeBoard.xYaw.bind(vmc.yaw);
        timeBoard.yYaw.bind(vmc.yaw);

        playerButtons.onOpen.addListener((o, ov, nv) -> vmc.openFile());
        playerButtons.onOpenXML.addListener((o, ov, nv) -> vmc.openXMLFile());
        playerButtons.onPlay.addListener((o, ov, nv) -> vmc.play());
        playerButtons.onAnomalyDetector.addListener((o, ov, nv) -> {
            vmc.loadAnomalyDetector();
            vmc.r.run();
        });
        playerButtons.onPause.addListener((o, ov, nv) -> vmc.pause());
        playerButtons.onStop.addListener((o, ov, nv) -> vmc.stop());
        playerButtons.onRewind.addListener((o, ov, nv) -> vmc.rewind());
        playerButtons.onForward.addListener((o, ov, nv) -> vmc.forward());

        //  vmc.attributeList.addListener((ListChangeListener) change -> attributesList.lst.addAll(change.getList()));

        attributesList.alc.lv.setItems(vmc.attributeList);
        graphs.selectedAttribute.bind(attributesList.alc.lv.getSelectionModel().selectedItemProperty());
        vmc.chosenAttribute.bind(attributesList.alc.lv.getSelectionModel().selectedItemProperty());
    }

    @Override
    public void update(Observable o, Object arg) {}
}
