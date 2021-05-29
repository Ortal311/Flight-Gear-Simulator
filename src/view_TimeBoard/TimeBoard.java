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

    public StringProperty altimeter,airSpeed,fd,pitch,roll,yaw;
    public DoubleProperty centerX, centerY, xAirSpeed, yAirSpeed,
            xPitch, yPitch, xRoll, yRoll, xYaw, yYaw;

    public TimeBoard() {
        super();
        FXMLLoader fxl = new FXMLLoader();
        altimeter= new SimpleStringProperty();
        airSpeed= new SimpleStringProperty();
        fd= new SimpleStringProperty();
        pitch= new SimpleStringProperty();
        roll= new SimpleStringProperty();
        yaw= new SimpleStringProperty();

        centerX = new SimpleDoubleProperty();
        centerY = new SimpleDoubleProperty();

//        xAirSpeed = new SimpleDoubleProperty();
//        yAirSpeed = new SimpleDoubleProperty();
        xPitch = new SimpleDoubleProperty();
        yPitch = new SimpleDoubleProperty();
        xRoll = new SimpleDoubleProperty();
        yRoll = new SimpleDoubleProperty();
        xYaw = new SimpleDoubleProperty();
        yYaw = new SimpleDoubleProperty();

        try {
            AnchorPane times= fxl.load(getClass().getResource("TimeBoard.fxml").openStream());
            TimeBoardController tbc= fxl.getController();
            this.getChildren().add(times);

            tbc.altimeter.textProperty().bind(altimeter);
            tbc.airSpeed.textProperty().bind(airSpeed);
            tbc.fd.textProperty().bind(fd);
//            tbc.pitch.textProperty().bind(pitch);
//            tbc.roll.textProperty().bind(roll);
//            tbc.yaw.textProperty().bind(yaw);


//            xAirSpeed.addListener((o, ov, nv) -> {
//                Double value = (nv.doubleValue() / 94) * 59;
//                tbc.lineAirSpeed.setEndX(32 * Math.sin((value * (2 * Math.PI) / 60)));
//            });
//
//            yAirSpeed.addListener((o, ov, nv) -> {
//                Double value = (nv.doubleValue() / 94) * 59;
//                tbc.lineAirSpeed.setEndY((-32) * Math.cos((value * (2 * Math.PI) / 60)));
//            });

            xPitch.addListener((o, ov, nv) -> {
                Double value = ((nv.doubleValue() + 10) / 27) * 59;
                tbc.linePitch.setEndX(32 * Math.sin((value * (2 * Math.PI) / 60)));
            });

            yPitch.addListener((o, ov, nv) -> {
                Double value = ((nv.doubleValue() + 10) / 27) * 59;
                tbc.linePitch.setEndY((-32) * Math.cos((value * (2 * Math.PI) / 60)));
            });

            xRoll.addListener((o, ov, nv) -> {
                Double value = ((nv.doubleValue() + 38) / 81) * 59;
                tbc.lineRoll.setEndX(32 * Math.sin((value * (2 * Math.PI) / 60)));
            });

            yRoll.addListener((o, ov, nv) -> {
                Double value = ((nv.doubleValue() + 38) / 81) * 59;
                tbc.lineRoll.setEndY((-32) * Math.cos((value * (2 * Math.PI) / 60)));
            });

            xYaw.addListener((o, ov, nv) -> {
                Double value = ((nv.doubleValue() + 29) / 120) * 59;
                tbc.lineYaw.setEndX(32 * Math.sin((value * (2 * Math.PI) / 60)));
            });

            yYaw.addListener((o, ov, nv) -> {
                Double value = ((nv.doubleValue() + 29) / 120) * 59;
                tbc.lineYaw.setEndY((-32) * Math.cos((value * (2 * Math.PI) / 60)));
            });


        } catch (IOException e) {}
    }
}
