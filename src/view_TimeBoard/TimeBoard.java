package view_TimeBoard;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import viewModel.ViewModelController;

import java.io.IOException;

public class TimeBoard extends AnchorPane {
    TimeBoardController tbc;
    public StringProperty altimeter,airSpeed,fd,pitch,roll,yaw;

    public TimeBoard() {
        super();
        FXMLLoader fxl = new FXMLLoader();
        altimeter= new SimpleStringProperty();
        airSpeed= new SimpleStringProperty();
        fd= new SimpleStringProperty();
        pitch= new SimpleStringProperty();
        roll= new SimpleStringProperty();
        yaw= new SimpleStringProperty();

        try {
            AnchorPane times= fxl.load(getClass().getResource("TimeBoard.fxml").openStream());
            tbc= fxl.getController();
            this.getChildren().add(times);

            tbc.altimeter.textProperty().bind(altimeter);
            tbc.airSpeed.textProperty().bind(airSpeed);
            tbc.fd.textProperty().bind(fd);
            tbc.pitch.textProperty().bind(pitch);
            tbc.roll.textProperty().bind(roll);
            tbc.yaw.textProperty().bind(yaw);



        } catch (IOException e) {}
    }
    /*
    public void setVmc(ViewModelController vmc) {
        tbc.init(vmc);
    }*/
}
