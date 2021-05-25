package view_PlayerButtons;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PlayerButtonsController {

    @FXML
    Slider sliderTime;

    @FXML
    ChoiceBox choiceSpeed;

    @FXML
    TextField miliSec;

    @FXML
    TextField seconds;

    @FXML
    TextField minutes;

    // public DoubleProperty rate;
    public BooleanProperty onOpen, onOpenXML, onPlay, onPause, onSpeed, onStop,
            onRewind, onForward, onPlus15, onMinus15;

    public PlayerButtonsController() {
        onOpen= new SimpleBooleanProperty();
        onOpenXML= new SimpleBooleanProperty();
        onPlay= new SimpleBooleanProperty();
        onPause= new SimpleBooleanProperty();
        onSpeed= new SimpleBooleanProperty();
        onStop= new SimpleBooleanProperty();
        onRewind= new SimpleBooleanProperty();
        onForward= new SimpleBooleanProperty();
        onPlus15= new SimpleBooleanProperty();
        onMinus15= new SimpleBooleanProperty();

        onOpen.setValue(false);
        onOpenXML.setValue(false);
        onPlay.setValue(false);
        onPause.setValue(false);
        onSpeed.setValue(false);
        onStop.setValue(false);
        onRewind.setValue(false);
        onForward.setValue(false);
        onPlus15.setValue(false);
        onMinus15.setValue(false);
    }

    public void init() {
        ObservableList<Double> speedList = FXCollections.observableArrayList(0.5, 1.0, 1.5, 2.0, 2.5);
        choiceSpeed.setItems(speedList);
    }

    public void onOpen() {
        System.out.println(onOpen.getValue());
        if(onOpen.getValue()){
            onOpen.setValue(false);
        }
        else {
            onOpen.setValue(true);
        }
    }

    public void onOpenXML() {
        if(onOpenXML.getValue()){
            onOpenXML.setValue(false);
        }
        else {
            onOpenXML.setValue(true);
        }
    }

    public void onPlay() {
        if(onPlay.getValue()){
            onPlay.setValue(false);
        }
        else {
            onPlay.setValue(true);
        }
    }

//    public void onSpeed() {
//        // rate = (DoubleProperty) choiceSpeed.getValue();
//        // vmc.rate.bind(choiceSpeed.valueProperty());
//
//        // rate.bind(vmc.rate);
//
//        //this.vmc
//
//
//    }


    public void onPause() {
        if(onPause.getValue()){
            onPause.setValue(false);
        }
        else {
            onPause.setValue(true);
        }
    }

    public void onStop() {
        if(onStop.getValue()){
            onStop.setValue(false);
        }
        else {
            onStop.setValue(true);
        }
    }

    public void onRewind() {
        if(onRewind.getValue()){
            onRewind.setValue(false);
        }
        else {
            onRewind.setValue(true);
        }
    }

    public void onForward() {
        if(onForward.getValue()){
            onForward.setValue(false);
        }
        else {
            onForward.setValue(true);
        }
    }

    public void onPlus15() {
        if(onPlus15.getValue()){
            onPlus15.setValue(false);
        }
        else {
            onPlus15.setValue(true);
        }
    }

    public void onMinus15() {
        if(onMinus15.getValue()){
            onMinus15.setValue(false);
        }
        else {
            onMinus15.setValue(true);
        }
    }
}
