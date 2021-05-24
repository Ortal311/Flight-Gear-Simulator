package view_TimeBoard;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import viewModel.ViewModelController;

import java.io.IOException;

public class TimeBoard extends AnchorPane {
    TimeBoardController tbc;

    public TimeBoard() {
        super();
        FXMLLoader fxl = new FXMLLoader();
        try {
            AnchorPane times= fxl.load(getClass().getResource("TimeBoard.fxml").openStream());
            tbc= fxl.getController();
            this.getChildren().add(times);

        } catch (IOException e) {}
    }
    public void setVmc(ViewModelController vmc) {
        tbc.init(vmc);
    }
}
