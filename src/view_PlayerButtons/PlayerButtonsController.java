package view_PlayerButtons;

import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.text.Text;
import viewModel.ViewModelController;

public class PlayerButtonsController {

    @FXML
    Slider sliderTime;

    @FXML
    ChoiceBox choiceSpeed;

    @FXML
    Text timeFlight;


    ViewModelController vmc;

    // public DoubleProperty rate;


    public void init(ViewModelController vmc) {
        this.vmc = vmc;
        sliderTime.valueProperty().bind(vmc.sliderTime);

        timeFlight.textProperty().bind(vmc.timeFlight);

        // vmc.rate.bind(choiceSpeed.valueProperty());
        choiceSpeed.valueProperty().bind(vmc.rate);
        ObservableList<String> speedList = FXCollections.observableArrayList("0.5", "1.0", "1.5", "2.0", "2.5");


        choiceSpeed.setItems(speedList);

    }

    public void onOpen() {
        if (this.vmc == null)
            System.out.println("vmc is null");
        this.vmc.openFile();
    }

    public void onOpenXML() {
        if (this.vmc == null)
            System.out.println("vmc is null");
        this.vmc.openXMLFile();
    }

    public void onPlay() {
        if (this.vmc == null)
            System.out.println("vmc is null");
        this.vmc.play();
        //vmc.rate.bind()
    }

    public void onSpeed() {
        //  rate = (DoubleProperty) choiceSpeed.getValue();
        // vmc.rate.bind(choiceSpeed.valueProperty());

        // rate.bind(vmc.rate);

        //this.vmc
    }


    public void onPause() {
        this.vmc.pause();
    }

    public void onStop() {
        this.vmc.stop();
    }

    public void onRewind() {
        this.vmc.rewind();
    }

    public void onForward() {
        this.vmc.forward();
    }

    public void onPlus15() {
        this.vmc.plus15();
    }

    public void onMinus15() {
        this.vmc.minus15();
    }

}
