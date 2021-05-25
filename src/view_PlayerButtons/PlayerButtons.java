package view_PlayerButtons;

import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;
import viewModel.ViewModelController;

import java.io.IOException;

public class PlayerButtons extends AnchorPane {
    public DoubleProperty sliderTime, choiceSpeed;
    public  StringProperty timeFlight;
    public BooleanProperty onOpen, onOpenXML, onPlay, onPause, onSpeed, onStop,
            onRewind, onForward, onPlus15, onMinus15;


    public PlayerButtons() {
        super();
        FXMLLoader fxl = new FXMLLoader();
        sliderTime= new SimpleDoubleProperty();
        choiceSpeed=new SimpleDoubleProperty();
        timeFlight=new SimpleStringProperty();

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

        try {
            AnchorPane buttons= fxl.load(getClass().getResource("PlayerButtons.fxml").openStream());
            PlayerButtonsController pbc= fxl.getController();
            pbc.init();

            pbc.sliderTime.valueProperty().bind(sliderTime);
            choiceSpeed.bind((pbc.choiceSpeed.valueProperty()));
            pbc.timeFlight.textProperty().bind(timeFlight);
            onOpen.bind(pbc.onOpen);
            onOpenXML.bind(pbc.onOpenXML);
            onPlay.bind(pbc.onPlay);
            onPause.bind(pbc.onPause);
            onSpeed.bind(pbc.onSpeed);
            onStop.bind(pbc.onStop);
            onRewind.bind(pbc.onRewind);
            onForward.bind(pbc.onForward);
            onPlus15.bind(pbc.onPlus15);
            onMinus15.bind(pbc.onMinus15);

            this.getChildren().add(buttons);

        } catch (IOException e) {}
    }

}


