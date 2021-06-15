package view_TimeBoard;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import viewModel.ViewModelController;

import java.io.IOException;

public class TimeBoard extends AnchorPane {

    public StringProperty altimeter, airSpeed, fd;
    public DoubleProperty pitch, roll, yaw;

    public TimeBoard() {
        super();
        FXMLLoader fxl = new FXMLLoader();
        altimeter= new SimpleStringProperty();
        airSpeed= new SimpleStringProperty();
        fd= new SimpleStringProperty();

        pitch = new SimpleDoubleProperty();
        roll = new SimpleDoubleProperty();
        yaw = new SimpleDoubleProperty();

        try {
            AnchorPane times= fxl.load(getClass().getResource("TimeBoard.fxml").openStream());
            TimeBoardController tbc= fxl.getController();
            this.getChildren().add(times);

            tbc.altimeter.textProperty().bind(altimeter);
            tbc.airSpeed.textProperty().bind(airSpeed);
            tbc.fd.textProperty().bind(fd);

//            tbc.pitch.setMinValue(pitchMin.doubleValue());
//            tbc.pitch.setMaxValue(pitchMax.doubleValue());

            this.pitch.addListener((o, ov, nv) -> tbc.pitch.setValue(pitch.doubleValue()));
            this.roll.addListener((o, ov, nv) -> tbc.roll.setValue(roll.doubleValue()));
            this.yaw.addListener((o, ov, nv) -> tbc.yaw.setValue(yaw.doubleValue()));

        } catch (IOException e) {}
    }
}
