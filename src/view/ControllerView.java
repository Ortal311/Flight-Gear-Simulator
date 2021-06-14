package view;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import viewModel.ViewModelController;

import java.util.Observable;
import java.util.Observer;

import view_AttributesList.AttributesList;
import view_Graphs.Graphs;
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
    Graphs graphs;

    @FXML
    AnchorPane adAnchorePane;

    ViewModelController vmc;

    void init(ViewModelController vmc) {
        this.vmc = vmc;
        myJoystick.setLayoutX(730);
        myJoystick.setLayoutY(20);
        attributesList.setLayoutX(18);
        attributesList.setLayoutY(25);
        playerButtons.setLayoutY(380);
        playerButtons.setLayoutX(9);
        timeBoard.setLayoutX(480);
        timeBoard.setLayoutY(285);
        graphs.setLayoutX(250);
        graphs.setLayoutY(15);
        adAnchorePane.setLayoutX(330);
        adAnchorePane.setLayoutY(200);

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

        graphs.value.bind(vmc.valueAxis);
        graphs.valueCorrelate.bind(vmc.valueCorrelate);
        graphs.timeStamp.bind(vmc.timeStamp);
        graphs.graphSpeed.bind(vmc.choiceSpeed);
        graphs.sizeTS.setValue(vmc.sizeTS.getValue());
        graphs.correlatedAttribute.setValue(vmc.correlateFeature.getValue());

        timeBoard.airSpeed.bind(vmc.airSpeed);
        timeBoard.altimeter.bind(vmc.altimeter);
        timeBoard.fd.bind(vmc.fd);
        timeBoard.pitch.bind(vmc.pitch);
        timeBoard.roll.bind(vmc.roll);
        timeBoard.yaw.bind(vmc.yaw);

        playerButtons.onOpen.addListener((o, ov, nv) -> vmc.openFile());
        playerButtons.onOpenXML.addListener((o, ov, nv) -> vmc.openXMLFile());
        playerButtons.onPlay.addListener((o, ov, nv) -> vmc.play());

        //graphs.DataOfAttUntilIndex.bind(vmc.getDataOfAtt());

        playerButtons.onAnomalyDetector.addListener((o, ov, nv) -> {
            vmc.loadAnomalyDetector();
            try {
                adAnchorePane.getChildren().setAll(vmc.getPainter().call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        playerButtons.onPause.addListener((o, ov, nv) -> vmc.pause());
        playerButtons.onStop.addListener((o, ov, nv) -> vmc.stop());
        playerButtons.onRewind.addListener((o, ov, nv) -> vmc.rewind());
        playerButtons.onForward.addListener((o, ov, nv) -> vmc.forward());

        //  vmc.attributeList.addListener((ListChangeListener) change -> attributesList.lst.addAll(change.getList()));

        attributesList.alc.lv.setItems(vmc.attributeList);

        graphs.selectedAttribute.bind(attributesList.alc.lv.getSelectionModel().selectedItemProperty());
        graphs.correlatedAttribute.bind(vmc.correlateFeature);
        vmc.chosenAttribute.bind(attributesList.alc.lv.getSelectionModel().selectedItemProperty());
    }

    @Override
    public void update(Observable o, Object arg) {
    }
}
