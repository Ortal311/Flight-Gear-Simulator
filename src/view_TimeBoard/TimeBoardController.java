package view_TimeBoard;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import viewModel.ViewModelController;

public class TimeBoardController {

    ViewModelController vmc;

    @FXML
    Text altimeter,airSpeed,fd,pitch,roll,yaw;



    public void init(ViewModelController vmc) {
        this.vmc=vmc;

        altimeter.textProperty().bind(vmc.altimeter);
        airSpeed.textProperty().bind(vmc.airSpeed);
        fd.textProperty().bind(vmc.fd);
        pitch.textProperty().bind(vmc.pitch);
        roll.textProperty().bind(vmc.roll);
        yaw.textProperty().bind(vmc.yaw);



    }
}
